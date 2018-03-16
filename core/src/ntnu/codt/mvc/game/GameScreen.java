package ntnu.codt.mvc.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import ntnu.codt.CoDT;
import ntnu.codt.mvc.BaseScreen;

public class GameScreen extends BaseScreen {
  private final GameModel gameModel;
  private final GameController gameController;
  private final GameView gameView;
  private TiledMap map;
  private OrthogonalTiledMapRenderer renderer;

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

  public void show(){
    map = new TmxMapLoader().load("tiledmap.tmx");
    renderer = new OrthogonalTiledMapRenderer(map);
  }

}
