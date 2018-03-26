package ntnu.codt.systems;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import ntnu.codt.components.AttackComponent;
import ntnu.codt.components.PositionComponent;
import ntnu.codt.components.TextureComponent;
import ntnu.codt.components.TransformComponent;
import ntnu.codt.components.VelocityComponent;


public class TowerSystem extends IteratingSystem {
  private ComponentMapper<AttackComponent> am;
  private ComponentMapper<PositionComponent> pm;
  private ComponentMapper<TransformComponent> trm;
  private ComponentMapper<TextureComponent> tem;
  private Array<Entity> queue;
  private PooledEngine engine;

  public TowerSystem(PooledEngine engine) {
    super(Family.all(AttackComponent.class, PositionComponent.class, TransformComponent.class).get());

    trm = ComponentMapper.getFor(TransformComponent.class);
    pm = ComponentMapper.getFor(PositionComponent.class);
    am = ComponentMapper.getFor(AttackComponent.class);

    queue = new Array<Entity>();
    this.engine = engine;

  }

  public void update(float deltaTime) {
    super.update(deltaTime);

    for (Entity entity : queue) {
      PositionComponent pc = pm.get(entity);
      AttackComponent ac = am.get(entity);
      if (ac.lastShot -  System.currentTimeMillis() < -1000 && ac.creepsInRange.size > 0) {
        System.out.println("tower has something in range");
        if (ac.attackRadius.contains(ac.creepsInRange.get(0).getComponent(PositionComponent.class).pos.x, ac.creepsInRange.get(0).getComponent(PositionComponent.class).pos.y)) {
          System.out.println("trying to create attack");
          createAttack(pc.pos.x,
                        pc.pos.y,
                  ac.creepsInRange.get(0).getComponent(PositionComponent.class).pos.x,
                  ac.creepsInRange.get(0).getComponent(PositionComponent.class).pos.y,
                  ac.creepsInRange.get(0).getComponent(VelocityComponent.class).velocity,
                  ac.attackRadius.radius,
                  ac.attackDamage);
                  ac.lastShot = System.currentTimeMillis();

        }
          else{
          System.out.println("popping");
            ac.creepsInRange.pop();
          }
        }
      }
      queue.clear();
    }


  public Entity createAttack(float x, float y, float creepX, float creepY, Vector3 velocity, float radius, int attackDamage){
    Entity entity = engine.createEntity();
    AttackComponent at = engine.createComponent(AttackComponent.class);
    PositionComponent pm = engine.createComponent(PositionComponent.class);
    TextureComponent tem = engine.createComponent(TextureComponent.class);
    VelocityComponent vc = engine.createComponent(VelocityComponent.class);
    TransformComponent tc = engine.createComponent(TransformComponent.class);

    tem.region = new TextureRegion(new Texture(Gdx.files.internal("projectile.png")));

    pm.pos = new Vector3(x,y,0);
    at.attackDamage = attackDamage;
    at.attackRadius = new Circle(x,y,radius);
    //TODO create accurate shooting vector
    Vector3 temp = new Vector3((creepX-x), (creepY-y),0);
    temp = new Vector3((temp.x/temp.len())*200, (temp.y/temp.len())*200,0);
    if(temp.len() < 60){
      temp = new Vector3(temp.x+velocity.x,temp.y,0);
    }
    else{
      temp = new Vector3((float) (temp.x+(velocity.x*1.2)), (float) (temp.y+(velocity.y*1.2)),0);
    }


    vc.velocity = temp;//new Vector3((temp.x/temp.len())*200, (temp.y/temp.len())*200,0);

    entity.add(at);
    entity.add(pm);
    entity.add(tem);
    entity.add(vc);
    entity.add(tc);

    engine.addEntity(entity);
    System.out.println("attack created");
    return entity;

  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    queue.add(entity);
  }
}
