package ntnu.codt.mvc.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import ntnu.codt.CoDT;
import ntnu.codt.assets.Assets;
import ntnu.codt.mvc.Controller;
import ntnu.codt.mvc.View;


public class MenuView implements View {

  private Stage stage;
  private Assets assets;
  private Skin skin;
  private int screenHeight, screenWidth;
  private MenuController menuController;
  private CoDT game;



  public MenuView(CoDT game){
    this.game = game;
    this.assets = game.assets;
    this.menuController = menuController;
    this.stage = new Stage();
  }

  @Override
  public void render(float deltaTime) {

    drawUI();

  }

  public void drawUI() {

    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    stage.act();
    stage.getBatch().begin();
    // Draw different textures from batch
    stage.getBatch().end();
    stage.draw();
  }

  public void loadStage(Stage stage){
    this.screenHeight = Gdx.graphics.getHeight();
    this.screenWidth = Gdx.graphics.getWidth();

    this.skin = new Skin();
    this.stage = new Stage();

    skin.add("playBtnUp", assets.ui.playButtonUp, TextureRegion.class);
    skin.add("playBtnDown", assets.ui.playButtonDown, TextureRegion.class);
    skin.add("settingsBtnUp", assets.ui.settingsButtonUp, TextureRegion.class);
    skin.add("settingsBtnDown", assets.ui.settingsButtonDown, TextureRegion.class);

    ImageButton playBtn = new ImageButton(skin.getDrawable("playBtnUp"), skin.getDrawable("playBtnDown"));
    ImageButton settingsBtn = new ImageButton(skin.getDrawable("settingsBtnUp"), skin.getDrawable("settingsBtnDown"));
    stage.addActor(playBtn);
    stage.addActor(settingsBtn);


    //menuView.loadStage(stage);

    playBtn.setPosition(screenWidth/2 - playBtn.getWidth()/2, screenHeight*70/100 - playBtn.getHeight()/2);
    settingsBtn.setPosition(screenWidth/2 - settingsBtn.getWidth()/2, screenHeight*60/100 - settingsBtn.getHeight()/2);



    playBtn.addListener(new ChangeListener() {
      public void changed (ChangeEvent event, Actor actor) {
        System.out.println("PlayBtn Clicked!");
        game.goToGameScreen();
      }
    });

    settingsBtn.addListener(new ChangeListener() {
      public void changed (ChangeEvent event, Actor actor) {
        System.out.println("SettingsBtn Clicked!");
        game.goToSettingScreen();
      }
    });


    this.stage = stage;

  }

  public void show(){
    Gdx.input.setInputProcessor(stage);
  }

}
