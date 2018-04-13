package ntnu.codt.mvc.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

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


    mp = new InputMultiplexer();

    mp.addProcessor(gameView.getUi());
    mp.addProcessor(gameController);




/*    this.stage = new Stage();
    this.skin = gameModel.skin;
    screenHeight = Gdx.graphics.getHeight();
    screenWidth = Gdx.graphics.getWidth();




    dragImage.addListener(new DragListener() {
      public void touchDragged(InputEvent event, float x, float y, int pointer) {

        float xx = dragImage.getX();
        float yy = dragImage.getY();
        dragImage.moveBy(x - dragImage.getWidth() / 2, y - dragImage.getHeight() / 2);
        //System.out.println("dragged image coords:" + xx + "," + yy);

        if ((screenHeight - xx - yy) > 0) {
          System.out.println("under line");
          dragImage.setDrawable(skin.getDrawable("2"));
        } else {
          System.out.println("above line");
          dragImage.setDrawable(skin.getDrawable("1"));
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


    //stage.addActor(playBtn);
    stage.addActor(dragImage);*/



  }

  @Override
  public void show() {

    Gdx.input.setInputProcessor(mp);
  }

  @Override
  public void render(float delta) {
    gameView.render(delta);
  }

}



