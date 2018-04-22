package ntnu.codt.mvc.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;

import ntnu.codt.CoDT;
import ntnu.codt.mvc.BaseScreen;

public class GameScreen extends BaseScreen {
  private final GameModel gameModel;
  private final GameController gameController;
  private final GameView gameView;

  private InputMultiplexer mp;

  public GameScreen(CoDT game) {
    super(game);

    this.gameModel = new GameModel(game);
    this.gameView = new GameView(game, gameModel);
    this.gameController = new GameController(game, gameModel, gameView);

    game.client.setReceiveEndpoint(gameController);
    mp = new InputMultiplexer();
    mp.addProcessor(gameView.getUi());
    mp.addProcessor(gameController);
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(mp);
  }

  @Override
  public void render(float delta) {
    gameController.update(delta);
    gameModel.update(delta);
    gameView.render(delta);
  }

  @Override
  public void resize(int x, int y) {
    gameModel.viewport.update(x, y, true);
    gameView.getUi().getViewport().update(x, y, true);
    //gameModel.camera.position.set(gameModel.camera.viewportWidth/2, gameModel.camera.viewportHeight/2,0);
  }

}



