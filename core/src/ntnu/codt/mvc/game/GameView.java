package ntnu.codt.mvc.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;


import ntnu.codt.CoDT;
import ntnu.codt.components.PlayerComponent;
import ntnu.codt.core.observer.Observer;
import ntnu.codt.entities.Towers;
import ntnu.codt.graphics.Pixmap;
import ntnu.codt.mvc.View;
import ntnu.codt.ui.TowerButton;

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
  private Array<TowerButton> towerBtnList;



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
    this.skin = game.assets.skin;

    randomDataVector = new Vector3();
    this.screenHeight = Gdx.graphics.getHeight();
    this.screenWidth = Gdx.graphics.getWidth();
    loadUi();
  }

  public void updateModel(float deltaTime) {
    gameModel.update(deltaTime);
  }


  public void render(float deltaTime) {

    game.batch.enableBlending();
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

    towerBtnList = new Array<TowerButton>();
    int i = 0;
    for(Towers tower: Towers.values()){

      Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGB888);

      pixmap.setColor(Color.BLUE);
      pixmap.fill();
      pixmap.setColor(Color.BLACK);
      pixmap.drawRectangle(0, 0, 100, 100, 5);

      Texture texture = new Texture(pixmap);

      skin.add("rect", texture);

      Texture t = new Texture(Gdx.files.internal(tower.texture));

      skin.add("tower"+i, t);

      // Merge tower texture and rectangle to create a button texture
      t.getTextureData().prepare();
      pixmap.drawPixmap(t.getTextureData().consumePixmap(), 15,15);

      skin.add("towerTex"+i, new Texture(pixmap));
      TowerButton imgBtn = new TowerButton(skin.getDrawable("towerTex"+i), skin.getDrawable("tower"+i), tower);
      TowerButton imgBtn2 = new TowerButton(skin.getDrawable("towerTex"+i), tower);
      imgBtn.setPosition(screenWidth * 8 / 10 - imgBtn.getWidth() / 2, screenHeight * (5+2*i)/10 - imgBtn.getHeight() / 2);
      imgBtn2.setPosition(screenWidth * 8 / 10 - imgBtn2.getWidth() / 2, screenHeight * (5+2*i)/10 - imgBtn2.getHeight() / 2);

      towerBtnList.add(imgBtn);
      ui.addActor(imgBtn2);
      ui.addActor(imgBtn);
      i++;
    }
    // Get Player funds here

    TextField.TextFieldStyle textStyle = new TextField.TextFieldStyle();
    textStyle.font = new BitmapFont();
    textStyle.fontColor = Color.BLACK;

    int funds = gameModel.player1.getComponent(PlayerComponent.class).funds;
    TextField cashAmount = new TextField("" + funds, textStyle);
    cashAmount.setPosition(screenWidth/2, screenHeight * 9/10);
    ui.addActor(cashAmount);

  }

  public Stage getUi(){
    return ui;
  }

  public Array<TowerButton> getTowerBtnList(){
    return towerBtnList;
  }

}
