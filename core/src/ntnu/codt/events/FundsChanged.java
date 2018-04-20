package ntnu.codt.events;

import ntnu.codt.core.eventhandler.Event;

/**
 * Created by oddmrog on 16.04.18.
 */

public class FundsChanged extends Event {

  public final int amount;

  public FundsChanged(int amount) {
    this.amount = amount;
  }
}
