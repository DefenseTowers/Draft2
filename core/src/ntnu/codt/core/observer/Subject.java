package ntnu.codt.core.observer;


import java.util.ArrayList;
import java.util.List;

public class Subject<I> {

  private final List<Observer<I>> observers = new ArrayList<Observer<I>>();

  public void subscribe(Observer<I> obs) {
    observers.add(obs);
  }

  public void unsubscribe(Observer<I> obs) {
    observers.remove(obs);
  }

  /**
   * Command pattern :)
   */
  protected void publish(I input) {
    for (Observer<I> obs : observers) {
      obs.call(input);
    }
  }

}
