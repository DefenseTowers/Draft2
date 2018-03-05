package ntnu.codt.mvc.game;


import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import ntnu.codt.CoDT;
import ntnu.codt.core.observer.Observer;
import ntnu.codt.mvc.View;

public class GameView implements View {
  private final int VIEWPORT_WIDTH = 1280;
  private final int VIEWPORT_HEIGHT = 720;
  private final CoDT game;
  private final PooledEngine engine;
  private Vector3 randomDataVector;
  private OrthographicCamera camera;

  public final Observer<Vector3> touch = new Observer<Vector3>() {
    @Override
    public void call(Vector3 input) {
      randomDataVector.set(input);
    }
  };

  public GameView(CoDT game, PooledEngine engine) {
    this.game = game;
    this.engine = engine;
    randomDataVector = new Vector3();
    camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    camera.position.set(VIEWPORT_WIDTH / 2, VIEWPORT_HEIGHT / 2, 0);
  }

  public void update(float deltaTime) {

  }

  public void drawUI() {
    camera.update();

    game.batch.setProjectionMatrix(camera.combined);
    game.batch.begin();
    game.batch.setShader(game.assets.fonts.shader);
    game.assets.fonts.fontMedium.draw(game.batch, Float.toString(randomDataVector.x), VIEWPORT_WIDTH / 2, VIEWPORT_HEIGHT / 2);
    game.assets.fonts.fontMedium.draw(game.batch, Float.toString(randomDataVector.y), VIEWPORT_WIDTH / 2, VIEWPORT_HEIGHT / 2 - 50.0f);
    game.batch.setShader(null);
    game.batch.end();
  }

  public void render(float deltaTime) {
    update(deltaTime);
    drawUI();

  }

}
