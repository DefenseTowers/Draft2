package ntnu.codt.mvc.menu;

import com.badlogic.gdx.scenes.scene2d.Stage;

import ntnu.codt.CoDT;
import ntnu.codt.mvc.BaseScreen;

/**
 * Created by oddmrog on 06.04.18.
 */

public class SettingScreen extends BaseScreen {

  private Stage stage;
  private MenuController controller;

  public SettingScreen(CoDT game){
    super(game);
    this.controller = new MenuController(game);
  }

  @Override
  public void render(float deltaTime) {

  }

  @Override
  public void show() {

  }
}
