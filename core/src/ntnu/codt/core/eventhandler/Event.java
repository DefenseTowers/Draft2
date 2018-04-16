package ntnu.codt.core.eventhandler;

import java.util.HashMap;
import java.util.Map;

public class Event {

  private static Map<
      Class<? extends Event>,
      ListenerList
      > classListenerMap = new HashMap<Class<? extends Event>, ListenerList>();

  public Event() {

  }

  public ListenerList getListenerList() {
    return getListenerList(this.getClass());
  }

  public static ListenerList getListenerList(final Class<? extends Event> c) {
    if (!classListenerMap.containsKey(c)) {
      classListenerMap.put(c, new ListenerList());
    }
    return classListenerMap.get(c);
  }

  public String getEventName() {
    return getClass().getSimpleName();
  }

}