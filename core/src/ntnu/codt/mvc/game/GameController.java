package ntnu.codt.mvc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ntnu.codt.CoDT;
import ntnu.codt.core.observer.Subject;
import ntnu.codt.mvc.Controller;

public class GameController extends Controller {
  private final GameModel model;
  private Subject<Void> subjectTouch;
  private GameView view;
  private Stage stage;
  private InputMultiplexer multiplexer;


  public GameController(CoDT game, GameModel model) {
    super(game);
    this.model = model;
    this.view = new GameView(game, model);

    stage = view.getStage();
    subjectTouch = new Subject<Void>();

    multiplexer = new InputMultiplexer();

    multiplexer.addProcessor(stage);
    multiplexer.addProcessor(this);

    Gdx.input.setInputProcessor(multiplexer);


    //stage.getActors().get(0).addListener();
  }

  public void update(float deltaTime) {
/*    if (Gdx.input.isTouched()) {
      model.touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      model.camera.unproject(model.touchPoint);
      subjectTouch.publish(null);
    }*/
  }

  @Override
  public boolean touchDown(int x, int y, int pointer, int button) {
    model.touchPoint.set(x, y, 0);
    model.camera.unproject(model.touchPoint);
    System.out.println("Just touched " + x + y);
    return true;

  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return super.touchDragged(screenX, screenY, pointer);
  }


}
