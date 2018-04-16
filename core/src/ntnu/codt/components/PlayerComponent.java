package ntnu.codt.components;

import com.badlogic.ashley.core.Component;

public class PlayerComponent implements Component {
  public static final int FACTION1 = 1;
  public static final int FACTION2 = 2;
  public int funds;
  public int health;
  public int faction;
  public String name;
  public int score;
}
