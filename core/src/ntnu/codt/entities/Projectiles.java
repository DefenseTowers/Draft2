package ntnu.codt.entities;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import ntnu.codt.components.*;
import ntnu.codt.core.prototype.Prototype5;

import static java.lang.Math.abs;

public enum Projectiles implements Prototype5<Entity, PooledEngine, Vector3, Vector3, Vector3, Entity> {

  FIRE(10, 200, false, false),
  ICE(10, 400, false, false),
  LIGHTNING(10, 150, true, true);

  private final int damage;
  private final float velocity;
  private final boolean ms;
  private final boolean ft;
  //private Vector2 creepVelocity;
  private IntMap<Animation<TextureRegion>> animations;

  Projectiles(int damage, float velocity, boolean ms, boolean ft) {
    this.damage = damage;
    this.velocity = velocity;
    this.ms = ms;
    this.ft = ft;

  }

  public void setAnimation(Animation animation) {
    this.animations = new IntMap<Animation<TextureRegion>>();
    this.animations.put(0, animation);
  }

  public void setAnimations(IntMap<Animation<TextureRegion>> animations) {
    this.animations = animations;
  }

  @Override
  public Entity copy(PooledEngine engine, Vector3 pos, Vector3 creepPos, Vector3 creepVel, Entity target) {
    Entity entity = engine.createEntity();

    ProjectileComponent prc = engine.createComponent(ProjectileComponent.class);
    PositionComponent pom = engine.createComponent(PositionComponent.class);
    TextureComponent tem = engine.createComponent(TextureComponent.class);
    VelocityComponent vc = engine.createComponent(VelocityComponent.class);
    TransformComponent tc = engine.createComponent(TransformComponent.class);
    AnimationComponent ac = engine.createComponent(AnimationComponent.class);
    StateComponent sc = engine.createComponent(StateComponent.class);

    if (ms) {
      sc.set(MathUtils.random(this.animations.size - 1));
//      sc.set(2);
    } else {
      sc.set(0);
    }
    ac.animations = this.animations;
//    ac.animations.put(0, this.animation);
//    sc.set(0);

    pom.pos = new Vector3(pos.x,pos.y,0);
    prc.damage = this.damage;
    prc.target = target;
    prc.ft = this.ft;
    tc.rotation = 0;
    tc.scale = new Vector2(1, 1);

    //TODO create accurate shooting vector

    // The vector that extends from the missile to the target
    Vector3 targetOffset = new Vector3(creepPos.x - pos.x, creepPos.y - pos.y,0);

    // The distance from the missile to the target
    float targetDistance = targetOffset.len();

    // Normalize the offset vector into a direction - same as doing TargetOffset.normalized
    Vector3 targetDirection = new Vector3(targetOffset.x/targetDistance,targetOffset.y/targetDistance,0);

    // How fast the target and missle are moving relative to one another
    Vector3 missileVelocity = new Vector3(targetDirection.x*(this.velocity),targetDirection.y*(this.velocity),0);
    Vector3 targetVelocity = new Vector3(creepVel.x,creepVel.y,0);
    Vector3 relativeVelocity = new Vector3(missileVelocity.x-targetVelocity.x,missileVelocity.y-targetVelocity.y,0);

    // How fast the target is moving away from the missile
    float relativeSpeed = Vector3.dot(relativeVelocity.x,relativeVelocity.y,0,targetDirection.x,targetDirection.y,0);

    // Estimate of how long it will take our missile to catch up to the target
    float interceptTime = (targetDistance)/relativeSpeed;

    //Estimate of where the target will be
    Vector3 interceptLocation = new Vector3(creepPos.x+(targetVelocity.x*(interceptTime)),creepPos.y+(targetVelocity.y*interceptTime),0);

    // Aim the missile towards this location
    Vector2 aimDirection = new Vector2(interceptLocation.x - pos.x, interceptLocation.y - pos.y);
    prc.targetDistance = new Vector2(abs(aimDirection.x), abs(aimDirection.y));

    float aimDirectionLen = aimDirection.len();
    aimDirection = new Vector2(aimDirection.x/aimDirectionLen,aimDirection.y/aimDirectionLen);
//    float rotation = (float)atan2(aimDirection.x,aimDirection.y);

    //tem.region = new TextureRegion(new Texture(Gdx.files.internal("projectiles/fire.png")));
    tc.rotation = aimDirection.angleRad();
    tc.scale.set(0.5f, 0.5f);

    vc.velocity = new Vector3(aimDirection.x*this.velocity,aimDirection.y*this.velocity,0);
    vc.maxVel = this.velocity;

    entity.add(prc);
    entity.add(pom);
    entity.add(tem);
    entity.add(vc);
    entity.add(tc);
    entity.add(ac);
    entity.add(sc);

    engine.addEntity(entity);
    return entity;
  }

}
