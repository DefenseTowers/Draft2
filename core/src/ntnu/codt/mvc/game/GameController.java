package ntnu.codt.mvc.game;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import ntnu.codt.CoDT;
import ntnu.codt.EntityFactory;
import ntnu.codt.components.PositionComponent;
import ntnu.codt.core.observer.Subject;
import ntnu.codt.mvc.Controller;
import ntnu.codt.systems.AnimationSystem;
import ntnu.codt.systems.RenderSystem;
import ntnu.codt.systems.TowerSystem;

public class GameController extends Controller {

  private final int VIEWPORT_WIDTH = 1280;
  private final int VIEWPORT_HEIGHT = 720;
  private final CoDT game;
  private final PooledEngine engine;
  private Entity testEntity;
  private EntityFactory factory;
  private GameView gameView;
  private OrthographicCamera camera;
  private Subject<Vector3> subject;
  private Vector3 touchPoint;

  public GameController(CoDT game) {
    this.game = game;
    touchPoint = new Vector3();
    camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    camera.position.set(VIEWPORT_WIDTH / 2, VIEWPORT_HEIGHT / 2, 0);


    engine = new PooledEngine();

    engine.addSystem(new RenderSystem(game.batch, game.shape, game.assets));
    engine.addSystem(new TowerSystem());
    engine.addSystem(new AnimationSystem());

    subject = new Subject<Vector3>();

    gameView = new GameView(game, engine);
    subject.subscribe(gameView.touch);

    factory = new EntityFactory(engine);

    testEntity = factory.createTestEntity();

  }

  public void update(float deltaTime) {

    engine.update(deltaTime);

    if (Gdx.input.justTouched()) {
      touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      camera.unproject(touchPoint);
      subject.publish(touchPoint);

      PositionComponent pm = testEntity.getComponent(PositionComponent.class);
      pm.pos.set(touchPoint);
    }

  }

  @Override
  public void render(float deltaTime) {
    update(deltaTime);

    camera.update();

    gameView.render(deltaTime);
  }

}
