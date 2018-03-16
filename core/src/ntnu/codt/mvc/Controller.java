package ntnu.codt.mvc;


import com.badlogic.gdx.InputAdapter;
import ntnu.codt.CoDT;

public abstract class Controller extends InputAdapter {
  protected final CoDT game;

  public Controller(CoDT game) {
    this.game = game;
  }

  public abstract void update(float deltaTime);

}
