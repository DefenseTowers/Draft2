package ntnu.codt.systems;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import ntnu.codt.components.*;

import static java.lang.Math.abs;
import static java.lang.Math.atan2;


public class TowerSystem extends IteratingSystem {
  private ComponentMapper<AttackComponent> am;
  private ComponentMapper<PositionComponent> pm;
  private ComponentMapper<BoundsComponent> bm;
  private ComponentMapper<TransformComponent> trm;
  private ComponentMapper<TextureComponent> tem;
  private Array<Entity> queue;
  private PooledEngine engine;

  public TowerSystem(PooledEngine engine) {
    super(Family.all(AttackComponent.class, PositionComponent.class, TransformComponent.class, BoundsComponent.class).get());

    trm = ComponentMapper.getFor(TransformComponent.class);
    pm = ComponentMapper.getFor(PositionComponent.class);
    am = ComponentMapper.getFor(AttackComponent.class);
    bm = ComponentMapper.getFor(BoundsComponent.class);

    queue = new Array<Entity>();
    this.engine = engine;

  }

  public void update(float deltaTime) {
    super.update(deltaTime);
    for (Entity entity : queue) {
      PositionComponent pc = pm.get(entity);
      AttackComponent ac = am.get(entity);
      if (ac.lastShot -  System.currentTimeMillis() < -ac.reloadTime && ac.creepsInRange.size() > 0) {
        if (ac.attackRadius.contains(ac.creepsInRange.get(0).getComponent(PositionComponent.class).pos.x, ac.creepsInRange.get(0).getComponent(PositionComponent.class).pos.y)) {
          createAttack(deltaTime,pc.pos.x,
                        pc.pos.y,
                  ac.creepsInRange.get(0).getComponent(PositionComponent.class).pos.x,
                  ac.creepsInRange.get(0).getComponent(PositionComponent.class).pos.y,
                  ac.creepsInRange.get(0).getComponent(VelocityComponent.class).velocity,
                  ac.attackRadius.radius,
                  ac.attackDamage,
                  ac.attackVelocity,
                  ac.creepsInRange.get(0));
                  ac.lastShot = System.currentTimeMillis();



        } else {
            ac.creepsInRange.remove(0);
        }
      }
    }
    queue.clear();
  }


  public Entity createAttack(float deltaTime, float x, float y, float creepX, float creepY, Vector3 creepVelocity, float radius, int attackDamage, float attackVelocity, Entity target){
    Entity entity = engine.createEntity();
    ProjectileComponent prc = engine.createComponent(ProjectileComponent.class);
    PositionComponent pom = engine.createComponent(PositionComponent.class);
    TextureComponent tem = engine.createComponent(TextureComponent.class);
    VelocityComponent vc = engine.createComponent(VelocityComponent.class);
    TransformComponent tc = engine.createComponent(TransformComponent.class);

    pom.pos = new Vector3(x,y,0);
    prc.damage = attackDamage;
    prc.target = target;
    tc.rotation = 0;
    tc.scale = new Vector2(1, 1);

    //TODO create accurate shooting vector

    // The vector that extends from the missile to the target
    Vector3 targetOffset = new Vector3(creepX-x, creepY-y,0);

    // The distance from the missile to the target
    float targetDistance = targetOffset.len();

    // Normalize the offset vector into a direction - same as doing TargetOffset.normalized
    Vector3 targetDirection = new Vector3(targetOffset.x/targetDistance,targetOffset.y/targetDistance,0);

    // How fast the target and missle are moving relative to one another
    Vector3 missileVelocity = new Vector3(targetDirection.x*(attackVelocity),targetDirection.y*(attackVelocity),0);
    Vector3 targetVelocity = new Vector3(creepVelocity.x,creepVelocity.y,0);
    Vector3 relativeVelocity = new Vector3(missileVelocity.x-targetVelocity.x,missileVelocity.y-targetVelocity.y,0);

    // How fast the target is moving away from the missile
    float relativeSpeed = Vector3.dot(relativeVelocity.x,relativeVelocity.y,0,targetDirection.x,targetDirection.y,0);

    // Estimate of how long it will take our missile to catch up to the target
    float interceptTime = (targetDistance)/relativeSpeed;

    //Estimate of where the target will be
    Vector3 interceptLocation = new Vector3(creepX+(targetVelocity.x*(interceptTime)),creepY+(targetVelocity.y*interceptTime),0);

    // Aim the missile towards this location
    Vector2 aimDirection = new Vector2(interceptLocation.x-x,interceptLocation.y-y);
    prc.targetDistance = new Vector2(abs(aimDirection.x), abs(aimDirection.y));

    float aimDirectionLen = aimDirection.len();
    aimDirection = new Vector2(aimDirection.x/aimDirectionLen,aimDirection.y/aimDirectionLen);
//    float rotation = (float)atan2(aimDirection.x,aimDirection.y);

    tem.region = new TextureRegion(new Texture(Gdx.files.internal("projectiles/fire.png")));
    tc.rotation = aimDirection.angleRad();
    tc.scale.set(0.5f, 0.5f);

    vc.velocity = new Vector3(aimDirection.x*attackVelocity,aimDirection.y*attackVelocity,0);

    entity.add(prc);
    entity.add(pom);
    entity.add(tem);
    entity.add(vc);
    entity.add(tc);

    engine.addEntity(entity);
    return entity;

  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    queue.add(entity);
  }
}
