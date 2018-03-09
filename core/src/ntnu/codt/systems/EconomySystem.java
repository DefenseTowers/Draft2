package ntnu.codt.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import ntnu.codt.components.PlayerComponent;


/**
 * Created by oddmrog on 05.03.18.
 */

public class EconomySystem extends IteratingSystem {


  private ComponentMapper<PlayerComponent> pm;

  public EconomySystem(){
    super(Family.all(PlayerComponent.class).get());

    pm = ComponentMapper.getFor(PlayerComponent.class);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    int funds = pm.get(entity).funds;
  }
}
