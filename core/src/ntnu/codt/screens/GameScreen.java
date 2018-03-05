package ntnu.codt.screens;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import ntnu.codt.CoDT;
import ntnu.codt.EntityFactory;
import ntnu.codt.components.PositionComponent;
import ntnu.codt.systems.AnimationSystem;
import ntnu.codt.systems.RenderSystem;
import ntnu.codt.systems.TowerSystem;

public class GameScreen extends ScreenAdapter {
  private final int VIEWPORT_WIDTH = 1280;
  private final int VIEWPORT_HEIGHT = 720;
  private final CoDT game;
  private final PooledEngine engine;
  private Vector3 touchPoint;
  private OrthographicCamera camera;
  private EntityFactory factory;
  private Entity testEntity;

  public GameScreen(CoDT game) {
    this.game = game;
    touchPoint = new Vector3();
    camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    camera.position.set(VIEWPORT_WIDTH / 2, VIEWPORT_HEIGHT / 2, 0);

    engine = new PooledEngine();

    engine.addSystem(new RenderSystem(game.batch, game.shape, game.assets));
    engine.addSystem(new TowerSystem());
    engine.addSystem(new AnimationSystem());

    factory = new EntityFactory(engine);

    testEntity = factory.createTestEntity();

  }

  public void update(float deltaTime) {

    engine.update(deltaTime);

    if (Gdx.input.justTouched()) {
      touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      camera.unproject(touchPoint);

      PositionComponent pm = testEntity.getComponent(PositionComponent.class);
      pm.pos.set(touchPoint);
    }

  }

  public void drawUI() {
    camera.update();

    game.batch.setProjectionMatrix(camera.combined);
    game.batch.begin();
    game.batch.setShader(game.assets.fonts.shader);
    game.assets.fonts.fontMedium.draw(game.batch, Float.toString(touchPoint.x), VIEWPORT_WIDTH / 2, VIEWPORT_HEIGHT / 2);
    game.assets.fonts.fontMedium.draw(game.batch, Float.toString(touchPoint.y), VIEWPORT_WIDTH / 2, VIEWPORT_HEIGHT / 2 - 50.0f);
    game.batch.setShader(null);
    game.batch.end();
  }

  @Override
  public void render(float deltaTime) {
    update(deltaTime);
    drawUI();

  }

}
