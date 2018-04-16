package ntnu.codt.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

import ntnu.codt.core.eventhandler.Event;
import ntnu.codt.entities.Towers;

/**
 * Created by oddmrog on 16.04.18.
 */

public class TowerPlaced extends Event {

  public final Towers tower;
  public final Vector3 pos;

  public TowerPlaced(Towers tower, Vector3 pos){
    this.tower = tower;
    this.pos = pos;
  }

}
