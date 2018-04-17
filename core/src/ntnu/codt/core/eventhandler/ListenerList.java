package ntnu.codt.core.eventhandler;

import java.util.HashSet;
import java.util.Iterator;

public class ListenerList implements Iterable<Listener> {

  private final HashSet<Listener> listeners = new HashSet<Listener>();

  public void register(final Listener listener) {
    listeners.add(listener);
  }

  public void unregister(final Listener listener) {
    listeners.remove(listener);
  }

  public HashSet<Listener> getListeners() {
    return listeners;
  }

  @Override
  public Iterator<Listener> iterator() {
    return listeners.iterator();
  }

}
