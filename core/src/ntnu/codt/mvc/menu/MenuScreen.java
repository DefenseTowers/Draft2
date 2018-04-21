package ntnu.codt.mvc.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

import ntnu.codt.CoDT;
import ntnu.codt.core.network.StartEndpoint;
import ntnu.codt.mvc.BaseScreen;


public class MenuScreen extends BaseScreen implements StartEndpoint {

  private MenuView menuView;
  private MenuController menuController;
  private Stage stage = new Stage();

  public MenuScreen(CoDT game) {
    super(game);
    menuController = new MenuController(game);
    menuView = new MenuView(game);

    game.client.setStartEndpoint(this);

    //game.client.joinGame();

    menuView.loadStage(stage);
  }

  @Override
  public void show() {

    menuView.show();
    //Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void render(final float deltaTime) {
    menuView.render(deltaTime);
  }


  @Override
  public void setGameScreen() {
    Gdx.app.postRunnable(new Runnable() {
      @Override
      public void run() {
        game.goToGameScreen();
      }
    });
  }

  @Override
  public void setMainScreen() {

  }

  @Override
  public void setWaitingScreen() {

  }

}
