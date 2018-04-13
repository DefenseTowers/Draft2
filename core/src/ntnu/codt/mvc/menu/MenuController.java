package ntnu.codt.mvc.menu;


import com.badlogic.gdx.math.Vector3;

import ntnu.codt.CoDT;
import ntnu.codt.mvc.Controller;
import ntnu.codt.mvc.game.GameScreen;


public class MenuController extends Controller {

  private Vector3 touchPoint;
  private MenuView menuView;

  public MenuController(CoDT game) {
    super(game);
    touchPoint = new Vector3();
    menuView = new MenuView(game);
  }

  @Override
  public void update(float deltaTime) {


  }

  public void goToGameScreen(){
    game.setScreen(new GameScreen(game));
  }

  public void goToSettingScreen(){
    game.setScreen(new SettingScreen(game));
  }

}
