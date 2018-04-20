package ntnu.codt.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import com.badlogic.gdx.Gdx;

import ntnu.codt.CoDT;
import ntnu.codt.components.AllegianceComponent;
import ntnu.codt.components.PlayerComponent;
import ntnu.codt.core.eventhandler.Subscribe;
import ntnu.codt.events.CreepDied;
import ntnu.codt.events.FundsChanged;

public class EconomySystem extends IteratingSystem {
  private final String TAG = EconomySystem.class.getName();
  private ComponentMapper<PlayerComponent> pm;
  private ComponentMapper<AllegianceComponent> am;


  public EconomySystem(){
    super(Family.all(PlayerComponent.class).get());
    pm = ComponentMapper.getFor(PlayerComponent.class);
    am = ComponentMapper.getFor(AllegianceComponent.class);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {

  }


  @Subscribe
  @SuppressWarnings("unused")
  public void creepDied(CreepDied event) {
    Gdx.app.debug(TAG, "Creep died");
    Entity owner = event.owner;
    PlayerComponent pc = pm.get(owner);
    pc.funds += event.bounty;
    CoDT.EVENT_BUS.post(new FundsChanged(pc.funds));

  }

  @Subscribe
  public void updateFunds(FundsChanged event){


  }

  public boolean sufficientFunds(Entity player, int cost){

    PlayerComponent pc = pm.get(player);
    boolean sufficient = false;
    if(pc.funds >= cost){
      sufficient = true;
    }
    return sufficient;
  }

  public void doTransaction(Entity player, int cost){
    PlayerComponent pc = pm.get(player);
    pc.funds = pc.funds - cost;
    CoDT.EVENT_BUS.post(new FundsChanged(pc.funds));
  }

}
