package ntnu.codt.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

public class ProjectileComponent implements Component {
  public Entity target;
  public Vector2 targetDistance;
  public int damage;
}
