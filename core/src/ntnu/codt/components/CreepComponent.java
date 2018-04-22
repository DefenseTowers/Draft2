package ntnu.codt.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CreepComponent implements Component {
  public TextureRegion[] regions;
  public int bounty;
  public int cost;
}
