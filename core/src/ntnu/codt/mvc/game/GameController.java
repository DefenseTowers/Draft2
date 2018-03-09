package ntnu.codt.mvc.game;


import com.badlogic.gdx.Gdx;
import ntnu.codt.CoDT;
import ntnu.codt.core.observer.Subject;
import ntnu.codt.mvc.Controller;

public class GameController extends Controller {
  private final GameModel model;
  private Subject<Void> subjectTouch;

  public GameController(CoDT game, GameModel model) {
    super(game);
    this.model = model;

    subjectTouch = new Subject<Void>();
  }

  public void update(float deltaTime) {
    if (Gdx.input.isTouched()) {
      model.touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      model.camera.unproject(model.touchPoint);
      subjectTouch.publish(null);
    }
  }

  @Override
  public boolean touchDown(int x, int y, int pointer, int button) {
    model.touchPoint.set(x, y, 0);
    model.camera.unproject(model.touchPoint);
    return true;
  }

}
