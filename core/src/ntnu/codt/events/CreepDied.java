package ntnu.codt.events;


import com.badlogic.ashley.core.Entity;

import ntnu.codt.core.eventhandler.Event;
import ntnu.codt.entities.Player;


public class CreepDied extends Event {
  public final int bounty;
  public final Entity owner;

  public CreepDied(int bounty, Entity owner) {
    this.bounty = bounty;
    this.owner = owner;
  }
}
