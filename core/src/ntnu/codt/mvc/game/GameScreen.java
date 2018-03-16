package ntnu.codt.mvc.game;


import com.badlogic.gdx.Gdx;
import ntnu.codt.CoDT;
import ntnu.codt.mvc.BaseScreen;

public class GameScreen extends BaseScreen {
  private final GameModel gameModel;
  private final GameController gameController;
  private final GameView gameView;

  public GameScreen(CoDT game) {
    super(game);

    gameModel = new GameModel(game);
    gameController = new GameController(game, gameModel);
    gameView = new GameView(game, gameModel);

    Gdx.input.setInputProcessor(gameController);
  }

  @Override
  public void render(float deltaTime) {
    gameController.update(deltaTime);
    gameModel.update(deltaTime);
    gameView.render(deltaTime);
  }

}