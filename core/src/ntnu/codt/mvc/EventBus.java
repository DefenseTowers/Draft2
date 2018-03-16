package ntnu.codt.mvc;


import ntnu.codt.core.observer.Observer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventBus {

  private final Map<Class<?>, Observer<?>> bus = new HashMap<Class<?>, Observer<?>>();

  public EventBus() {

  }

  public <T> void subscribe(Observer<T> obs, Class<T> c) {

  }

  public void unsubscribe() {

  }

  public void publish() {

  }

}
