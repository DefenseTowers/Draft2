package ntnu.codt.systems;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import ntnu.codt.components.AttackComponent;
import ntnu.codt.components.PositionComponent;

public class TowerSystem extends IteratingSystem {
  private ComponentMapper<AttackComponent> am;
  private ComponentMapper<PositionComponent> pm;

  public TowerSystem() {
    super(Family.all(AttackComponent.class, PositionComponent.class).get());

    am = ComponentMapper.getFor(AttackComponent.class);
    pm = ComponentMapper.getFor(PositionComponent.class);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {

  }
}
