package ntnu.codt.mvc.game;


import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import ntnu.codt.CoDT;
import ntnu.codt.EntityFactory;
import ntnu.codt.systems.AnimationSystem;
import ntnu.codt.systems.RenderSystem;
import ntnu.codt.systems.TowerSystem;

public class GameModel {
  private final EntityFactory entityFactory;
  public final OrthographicCamera camera;
  public final PooledEngine engine;
  public final Vector3 touchPoint;

  public GameModel(CoDT game) {
    touchPoint = new Vector3();
    camera = new OrthographicCamera(1280, 720);
    camera.position.set(1280 / 2, 720 / 2, 0);

    engine = new PooledEngine();

    engine.addSystem(new RenderSystem(game.batch, game.shape, game.assets));
    engine.addSystem(new TowerSystem());
    engine.addSystem(new AnimationSystem());

    entityFactory = new EntityFactory(engine);

    entityFactory.createTestEntity();
  }

  public void update(float deltaTime) {
    camera.update();
    engine.update(deltaTime);
  }


}
