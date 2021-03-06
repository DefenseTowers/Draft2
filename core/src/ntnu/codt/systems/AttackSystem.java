package ntnu.codt.systems;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector3;
import ntnu.codt.components.*;

import static java.lang.Math.abs;

public class AttackSystem extends IteratingSystem {
  private ComponentMapper<PositionComponent> pom;
  private ComponentMapper<ProjectileComponent> prm;
  private ComponentMapper<TransformComponent> trm;
  private ComponentMapper<TextureComponent> tem;
  private ComponentMapper<VelocityComponent> vm;
  private PooledEngine engine;

  public AttackSystem(PooledEngine engine) {
    super(Family.all(ProjectileComponent.class, PositionComponent.class, VelocityComponent.class).get());

    trm = ComponentMapper.getFor(TransformComponent.class);
    pom = ComponentMapper.getFor(PositionComponent.class);
    prm = ComponentMapper.getFor(ProjectileComponent.class);
    vm = ComponentMapper.getFor(VelocityComponent.class);


    this.engine = engine;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PositionComponent poc = pom.get(entity);
    VelocityComponent vc = vm.get(entity);
    ProjectileComponent prc = prm.get(entity);

    if (prc.targetDistance.x < 0 && prc.targetDistance.y < 0 && prc.target != null) {
      HealthComponent hc = prc.target.getComponent(HealthComponent.class);
      if (hc != null) {
        hc.health -= prc.damage;
      }
      prc.target = null;
      if (!entity.isScheduledForRemoval()) {
        engine.removeEntity(entity);
      }
    } else if (poc.pos.x < 1280 && poc.pos.x > 0 && poc.pos.y < 720 && poc.pos.y > 0) {
      prc.targetDistance.sub(abs(vc.velocity.x * deltaTime), abs(vc.velocity.y * deltaTime));

      if (prc.ft && prc.target.getComponent(PositionComponent.class) != null) {
        Vector3 tPos = prc.target.getComponent(PositionComponent.class).pos;
        Vector3 vv = new Vector3(poc.pos);
        vv.sub(tPos).setLength(vc.maxVel);
        poc.pos.sub(vv.x * deltaTime, vv.y * deltaTime, 0);
      } else {
        poc.pos.x += vc.velocity.x * deltaTime;
        poc.pos.y += vc.velocity.y * deltaTime;
      }
    } else {
      prc.target = null;
      if (!entity.isScheduledForRemoval()) {
        engine.removeEntity(entity);
      }
    }
  }

}
