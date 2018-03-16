package ntnu.codt.mvc;

import com.badlogic.gdx.ScreenAdapter;
import ntnu.codt.CoDT;


public abstract class BaseScreen extends ScreenAdapter {
  protected final CoDT game;

  public BaseScreen(CoDT game) {
    this.game = game;
  }

  @Override
  public abstract void render(float deltaTime);

}
