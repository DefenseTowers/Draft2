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
import com.badlogic.gdx.utils.Array;
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
  private Stage ui;
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

    randomDataVector = new Vector3();
    this.screenHeight = Gdx.graphics.getHeight();
    this.screenWidth = Gdx.graphics.getWidth();
    loadUi();
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
    ui.draw();
  }

  public void loadUi(){

    this.ui = new Stage();
    this.skin = gameModel.skin;
    ImageButton playBtn = new ImageButton(skin.getDrawable("1"), skin.getDrawable("2"));
    playBtn.setPosition(screenWidth * 9 / 10 - playBtn.getWidth() / 2, screenHeight * 70 / 100 - playBtn.getHeight() / 2);

    final Image dragImage = new Image(skin.getDrawable("1"));
    dragImage.setPosition(screenWidth * 8 / 10 - playBtn.getWidth() / 2, screenHeight * 70 / 100 - playBtn.getHeight() / 2);


    ui.addActor(playBtn);
    ui.addActor(dragImage);

  }

  public Stage getUi(){
    return ui;
  }

}
