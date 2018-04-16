package ntnu.codt.core.eventhandler;


import com.badlogic.gdx.utils.Disposable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class EventBus implements Disposable {

  public EventBus() {

  }

  @Override
  public void dispose() {
    try {
      Field field = Event.class.getDeclaredField("classListenerMap");
      field.setAccessible(true);
      field.set(null, new HashMap<Class<? extends Event>, ListenerList>());
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  public void register(final Object object) {
    try {
      for (final Method method : object.getClass().getMethods()) {
        if (method.isAnnotationPresent(Subscribe.class)) {
          final Class<?>[] params = method.getParameterTypes();
          if (params.length == 1) {
            final Class<?> argClass = params[0];
            if (Event.class.isAssignableFrom(argClass)) {
              final Listener listener = new Listener(object, method);
              Event.getListenerList(argClass.asSubclass(Event.class)).register(listener);
            } else {
              throw new IllegalArgumentException("Argument is not a subclass of Event");
            }
          } else {
            throw new IllegalArgumentException("@Subscribe only allows the method to have one argument");
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void unregister(final Object object) {
    try {
      for (final Method method : object.getClass().getMethods()) {
        if (method.isAnnotationPresent(Subscribe.class)) {
          final Class<?>[] params = method.getParameterTypes();
          if (params.length == 1) {
            final Class<?> argClass = params[0];
            if (Event.class.isAssignableFrom(argClass)) {
              final Listener listener = new Listener(object, method);
              Event.getListenerList(argClass.asSubclass(Event.class)).unregister(listener);
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void post(final Event event) {
    for (Listener listener : event.getListenerList()) {
      try {
        listener.METHOD.invoke(listener.LISTENER, event);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}