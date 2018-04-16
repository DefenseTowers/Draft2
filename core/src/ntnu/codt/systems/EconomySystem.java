package ntnu.codt.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import ntnu.codt.components.PlayerComponent;

public class EconomySystem extends IteratingSystem {


  private ComponentMapper<PlayerComponent> pm;

  public EconomySystem(){
    super(Family.all(PlayerComponent.class).get());

    pm = ComponentMapper.getFor(PlayerComponent.class);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    pm.get(entity).funds = pm.get(entity).funds + 1;
  }
}
