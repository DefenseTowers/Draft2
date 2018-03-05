package ntnu.codt.systems;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import ntnu.codt.components.DefenseComponent;
import ntnu.codt.components.HealthComponent;
import ntnu.codt.components.PositionComponent;
import ntnu.codt.components.SpeedComponent;

public class CreepSystem extends IteratingSystem {
  private ComponentMapper<HealthComponent> hm;
  private ComponentMapper<DefenseComponent> dm;
  private ComponentMapper<SpeedComponent> sm;
  private ComponentMapper<PositionComponent> pm;

  public CreepSystem() {
    super(Family.all(HealthComponent.class, DefenseComponent.class, SpeedComponent.class, PositionComponent.class).get());

    hm = ComponentMapper.getFor(HealthComponent.class);
    dm = ComponentMapper.getFor(DefenseComponent.class);
    sm = ComponentMapper.getFor(SpeedComponent.class);
    pm = ComponentMapper.getFor(PositionComponent.class);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {

  }
}
