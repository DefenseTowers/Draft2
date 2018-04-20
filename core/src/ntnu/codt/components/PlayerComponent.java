package ntnu.codt.components;

import com.badlogic.ashley.core.Component;
import ntnu.codt.entities.Player;

public class PlayerComponent implements Component {
  public int funds;
  public int health;
  public String name;
  public int score;
}
