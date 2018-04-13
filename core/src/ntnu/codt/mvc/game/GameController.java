package ntnu.codt.mvc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

import ntnu.codt.CoDT;
import ntnu.codt.core.observer.Subject;
import ntnu.codt.mvc.Controller;

public class GameController extends Controller {
  private final GameModel model;
  private Subject<Void> subjectTouch;
  private GameView view;
  private Stage ui;



  public GameController(CoDT game, GameModel model, GameView gameView) {
    super(game);
    this.model = model;
    subjectTouch = new Subject<Void>();
    this.ui = gameView.getUi();

    setListeners();

  }

  public void update(float deltaTime) {
    if (Gdx.input.isTouched()) {
      model.touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      model.camera.unproject(model.touchPoint);
      subjectTouch.publish(null);

    }

  }

  @Override
  public boolean touchDown ( int x, int y, int pointer, int button){
    model.touchPoint.set(x, y, 0);
    model.camera.unproject(model.touchPoint);
    System.out.println("Just touched " + x + y);
    return true;
  }

  @Override
  public boolean touchDragged ( int screenX, int screenY, int pointer){
    return super.touchDragged(screenX, screenY, pointer);
  }


  public void setListeners() {

    final int screenHeight = Gdx.graphics.getHeight();
    final int screenWidth = Gdx.graphics.getWidth();
    final Actor dragImage = ui.getActors().get(0);
    final Actor playBtn = ui.getActors().get(1);

    //System.out.println(ui.getActors().toString());


    dragImage.addListener(new DragListener() {
      public void touchDragged(InputEvent event, float x, float y, int pointer) {
        float xx = dragImage.getX();
        float yy = dragImage.getY();
        dragImage.moveBy(x - dragImage.getWidth() / 2, y - dragImage.getHeight() / 2);
        //System.out.println("dragged image coords:" + xx + "," + yy);

        if ((screenHeight - xx - yy) > 0) {
          System.out.println("under lgiine" + screenHeight + "x: " + xx + "y: " + yy);
          //dragImage.setDrawable(skin.getDrawable("2"));


        } else {
          System.out.println("above line");
          //dragImage.setDrawable(skin.getDrawable("1"));
        }
      }

      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        dragImage.setX(playBtn.getX());
        dragImage.setY(playBtn.getY());
        // Add a tower at this touchpoint if legal position and sufficient funds
      }

      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        return true;
      }
    });

  }
}
