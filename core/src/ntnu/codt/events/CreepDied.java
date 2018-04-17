package ntnu.codt.events;

import ntnu.codt.core.eventhandler.Event;


public class CreepDied extends Event {
  public final int bounty;
  public final int faction;

  public CreepDied(int bounty, int faction) {
    this.bounty = bounty;
    this.faction = faction;
  }
}
