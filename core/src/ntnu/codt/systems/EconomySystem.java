package ntnu.codt.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import com.badlogic.gdx.Gdx;
import ntnu.codt.components.PlayerComponent;
import ntnu.codt.core.eventhandler.Subscribe;
import ntnu.codt.events.CreepDied;

import java.util.LinkedList;

public class EconomySystem extends IteratingSystem {
  private final String TAG = EconomySystem.class.getName();
  private ComponentMapper<PlayerComponent> pm;
  private LinkedList<Action> ecoQ = new LinkedList<Action>();

  public EconomySystem(){
    super(Family.all(PlayerComponent.class).get());

    pm = ComponentMapper.getFor(PlayerComponent.class);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PlayerComponent pc = pm.get(entity);
//    pc.funds += 1;

    for (Action a : ecoQ) {
      if (pc.faction == a.faction) {
        pc.funds += a.value;
        ecoQ.remove(a);
      }
    }
  }

  @Subscribe
  @SuppressWarnings("unused")
  public void creepDied(CreepDied event) {
    ecoQ.add(new Action(event.bounty, event.faction));
    Gdx.app.debug(TAG, "Creep died");
  }

  private class Action {
    public final int value;
    public final int faction;

    public Action(int value, int faction) {
      this.value = value;
      this.faction = faction;
    }
  }

}
