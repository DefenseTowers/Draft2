package ntnu.codt.mvc.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
import ntnu.codt.ui.MoneyField;
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

      // Create UI button from tower texture
      Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGB888);
      pixmap.setColor(Color.BLUE);
      pixmap.fill();
      pixmap.setColor(Color.BLACK);
      pixmap.drawRectangle(0, 0, 100, 100, 5);
      Texture texture = new Texture(pixmap);
      skin.add("rect", texture);
      Texture t = new Texture(tower.textureRegion.getTexture().getTextureData());
      t.getTextureData().prepare();

      com.badlogic.gdx.graphics.Pixmap tempPix = t.getTextureData().consumePixmap();
      for (int j = 0; j < tower.textureRegion.getRegionWidth(); j++) {
        for (int k = 0; k < tower.textureRegion.getRegionHeight(); k++) {
          pixmap.drawPixel(
              j + ((pixmap.getWidth() - tower.textureRegion.getRegionWidth()) / 2),
              k + ((pixmap.getHeight() - tower.textureRegion.getRegionHeight()) / 2),
              tempPix.getPixel(j + tower.textureRegion.getRegionX(), k + tower.textureRegion.getRegionY())
          );
        }
      }

      skin.add("tower"+i, tower.textureRegion, TextureRegion.class);
      skin.add("towerTex"+i, new Texture(pixmap));


      // Create and add range image to towerbutton
      int diameter = 2*(int)tower.radius;
      int radius = diameter/2;
      com.badlogic.gdx.graphics.Pixmap p = new Pixmap(diameter, diameter, Pixmap.Format.RGBA8888);
      p.setColor(255, 255, 255, 0.2f);
      p.fillCircle(radius,radius, diameter/2);
      p.setBlending(com.badlogic.gdx.graphics.Pixmap.Blending.None);
      Texture tex = new Texture(p);
      Image attackRange = new Image(tex);
      attackRange.setVisible(false);

      // Create UI buttons
      TowerButton imgBtn = new TowerButton(skin.getDrawable("towerTex"+i), skin.getDrawable("tower"+i), tower, attackRange);
      TowerButton imgBtn2 = new TowerButton(skin.getDrawable("towerTex"+i), tower, attackRange);
      imgBtn.setPosition(screenWidth * 8 / 10 - imgBtn.getWidth() / 2, screenHeight * (5+2*i)/10 - imgBtn.getHeight() / 2);
      imgBtn2.setPosition(screenWidth * 8 / 10 - imgBtn2.getWidth() / 2, screenHeight * (5+2*i)/10 - imgBtn2.getHeight() / 2);

      towerBtnList.add(imgBtn);
      ui.addActor(attackRange);
      ui.addActor(imgBtn2);
      ui.addActor(imgBtn);
      i++;
    }

    // Create UI element for money
    TextField.TextFieldStyle textStyle = new TextField.TextFieldStyle();
    textStyle.font = new BitmapFont();
    textStyle.fontColor = Color.BLACK;
    int funds = gameModel.player1.getComponent(PlayerComponent.class).funds;
    MoneyField moneyField = new MoneyField("" + funds, textStyle);
    moneyField.setPosition(screenWidth/2, screenHeight * 9/10);
    ui.addActor(moneyField);

  }

  public Stage getUi(){
    return ui;
  }

  public Array<TowerButton> getTowerBtnList(){
    return towerBtnList;
  }

}
