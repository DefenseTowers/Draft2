package ntnu.codt.mvc.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.net.MulticastSocket;

import ntnu.codt.CoDT;
import ntnu.codt.core.observer.Observer;
import ntnu.codt.mvc.View;

public class GameView implements View {
  private final int VIEWPORT_WIDTH = 1280;
  private final int VIEWPORT_HEIGHT = 720;
  private final CoDT game;
  private final GameModel gameModel;
  private Vector3 randomDataVector;
  private final OrthographicCamera camera;
  private Stage stage;
  private Skin skin;
  private int screenHeight, screenWidth;


  public final Observer<Vector3> touch = new Observer<Vector3>() {
    @Override
    public void call(Vector3 input) {
      randomDataVector.set(input);
    }
  };

  public GameView(CoDT game, GameModel gameModel) {
    this.game = game;
    this.gameModel = gameModel;
    this.camera = gameModel.camera;
    this.stage = new Stage();
    randomDataVector = new Vector3();

    Skin skin = gameModel.skin;
    int screenHeight = Gdx.graphics.getHeight();
    int screenWidth = Gdx.graphics.getWidth();

    ImageButton towerBtn = new ImageButton(skin.getDrawable("1"), skin.getDrawable("1"));
    ImageButton towerBtn2 = new ImageButton(skin.getDrawable("2"), skin.getDrawable("2"));

    stage.addActor(towerBtn);
    stage.addActor(towerBtn2);

    towerBtn.setPosition(screenWidth * 4 / 5 - towerBtn.getWidth() / 2, screenHeight * 70 / 100 - towerBtn.getHeight() / 2);
    towerBtn2.setPosition(screenWidth * 4 / 5 - towerBtn2.getWidth() / 2, screenHeight * 60 / 100 - towerBtn2.getHeight() / 2);


  }

  public void updateModel(float deltaTime) {
    gameModel.update(deltaTime);
  }


  public void render(float deltaTime) {

    gameModel.renderer.render();
    updateModel(deltaTime);
    game.batch.setProjectionMatrix(camera.combined);
    game.batch.begin();
    game.batch.setShader(game.assets.fonts.shader);
    game.assets.fonts.fontMedium.draw(game.batch, Float.toString(randomDataVector.x), VIEWPORT_WIDTH / 2, VIEWPORT_HEIGHT / 2);
    game.assets.fonts.fontMedium.draw(game.batch, Float.toString(randomDataVector.y), VIEWPORT_WIDTH / 2, VIEWPORT_HEIGHT / 2 - 50.0f);
    game.batch.setShader(null);
    game.batch.end();
    stage.draw();
  }



  public void loadStage(){

    this.stage = new Stage();
    this.skin = gameModel.skin;
    screenHeight = Gdx.graphics.getHeight();
    screenWidth = Gdx.graphics.getWidth();


    final ImageButton playBtn = new ImageButton(skin.getDrawable("1"), skin.getDrawable("2"));
    playBtn.setPosition(screenWidth * 9 / 10 - playBtn.getWidth() / 2, screenHeight * 70 / 100 - playBtn.getHeight() / 2);

    final Image dragImage = new Image(skin.getDrawable("1"));
    dragImage.setPosition(screenWidth * 9 / 10 - playBtn.getWidth() / 2, screenHeight * 70 / 100 - playBtn.getHeight() / 2);

    dragImage.addListener(new DragListener() {
      public void touchDragged(InputEvent event, float x, float y, int pointer) {
        float xx = dragImage.getX();
        float yy = dragImage.getY();
        dragImage.moveBy(x - dragImage.getWidth() / 2, y - dragImage.getHeight() / 2);
        //System.out.println("dragged image coords:" + xx + "," + yy);

        if ((screenHeight - xx - yy) > 0) {
          System.out.println("under lgiine" + screenHeight + "x: " + xx + "y: " + yy );
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
    stage.addActor(dragImage);

  }

  public void show(){
    //Gdx.input.setInputProcessor(stage);
  }

  public Stage getStage(){
    return this.stage;
  }
}
