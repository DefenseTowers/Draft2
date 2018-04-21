package ntnu.codt.mvc.menu;

import com.badlogic.gdx.scenes.scene2d.Stage;

import ntnu.codt.CoDT;
import ntnu.codt.mvc.BaseScreen;


public class MenuScreen extends BaseScreen{

  private MenuView menuView;
  private MenuController menuController;
  private Stage stage = new Stage();

  public MenuScreen(CoDT game) {
    super(game);
    menuController = new MenuController(game);
    menuView = new MenuView(game);
    menuView.loadStage(stage);
  }

  @Override
  public void show() {

    menuView.show();

  }

  @Override
  public void render(final float deltaTime) {
    menuView.render(deltaTime);
  }




}
