package ntnu.codt.systems;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;

import ntnu.codt.components.*;

public class TowerSystem extends IteratingSystem {
  private ComponentMapper<AttackComponent> am;
  private ComponentMapper<PositionComponent> pm;
  private ComponentMapper<VelocityComponent> vm;
  private PooledEngine engine;

  public TowerSystem(PooledEngine engine) {
    super(Family.all(TowerComponent.class, AttackComponent.class, PositionComponent.class, TransformComponent.class, BoundsComponent.class).get());

    pm = ComponentMapper.getFor(PositionComponent.class);
    am = ComponentMapper.getFor(AttackComponent.class);
    vm = ComponentMapper.getFor(VelocityComponent.class);

    this.engine = engine;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PositionComponent pc = pm.get(entity);
    AttackComponent ac = am.get(entity);
    if (ac.lastShot - System.currentTimeMillis() < -ac.reloadTime && ac.creepsInRange.size() > 0) {
      if (ac.attackRadius.contains(ac.creepsInRange.get(0).getComponent(PositionComponent.class).pos.x, ac.creepsInRange.get(0).getComponent(PositionComponent.class).pos.y)) {
        ac.projectile.copy(
            engine,
            pc.pos,
            pm.get(ac.creepsInRange.get(0)).pos,
            vm.get(ac.creepsInRange.get(0)).velocity,
            ac.creepsInRange.get(0)
        );
        ac.lastShot = System.currentTimeMillis();
      } else {
        ac.creepsInRange.remove(0);
      }
    }
  }
}
