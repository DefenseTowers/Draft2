package ntnu.codt.mvc.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.audio.Sound;


import ntnu.codt.CoDT;
import ntnu.codt.assets.Assets;
import ntnu.codt.mvc.Controller;
import ntnu.codt.mvc.View;

import static ntnu.codt.CoDT.soundON;


public class MenuView implements View {

  private Stage stage;
  private Assets assets;
  private Skin skin;
  private int screenHeight, screenWidth;
  private CoDT game;



  public MenuView(CoDT game){
    this.game = game;
    this.assets = game.assets;
    this.stage = new Stage();

  }

  @Override
  public void render(float deltaTime) {
    stage.act();
    stage.getBatch().begin();
    stage.getBatch().draw(game.assets.menuScreen, 0, 0, screenWidth, screenHeight);
    stage.getBatch().end();
    stage.draw();


  }


  public void loadStage(Stage stage){
    this.screenHeight = Gdx.graphics.getHeight();
    this.screenWidth = Gdx.graphics.getWidth();

    this.skin = new Skin();
    this.stage = new Stage();

    final Sound pressed = Gdx.audio.newSound(Gdx.files.internal("sounds/Click_Standard_02.wav"));


    skin.add("playBtnUp", assets.ui.playButtonUp, TextureRegion.class);
    skin.add("playBtnDown", assets.ui.playButtonDown, TextureRegion.class);
    skin.add("settingsBtnUp", assets.ui.settingsButtonUp, TextureRegion.class);
    skin.add("settingsBtnDown", assets.ui.settingsButtonDown, TextureRegion.class);

    ImageButton playBtn = new ImageButton(skin.getDrawable("playBtnUp"), skin.getDrawable("playBtnDown"));
    ImageButton settingsBtn = new ImageButton(skin.getDrawable("settingsBtnUp"), skin.getDrawable("settingsBtnDown"));
    stage.addActor(playBtn);
    stage.addActor(settingsBtn);

    playBtn.setPosition(screenWidth/2 - playBtn.getWidth()/2, screenHeight*70/100 - playBtn.getHeight()/2);
    settingsBtn.setPosition(screenWidth/2 - settingsBtn.getWidth()/2, screenHeight*60/100 - settingsBtn.getHeight()/2);


    Texture quickGameUp = new Texture(Gdx.files.internal("button_quick-game_up.png"));
    Texture quickGameDown = new Texture(Gdx.files.internal("button_quick-game_down.png"));
    game.assets.skin.add("quickGameUp", quickGameUp);
    game.assets.skin.add("quickGameDown", quickGameDown);



    playBtn.addListener(new ChangeListener() {
      public void changed (ChangeEvent event, Actor actor) {
        System.out.println("PlayBtn Clicked!");
        if(soundON){pressed.play();}
        //game.goToGameScreen();
        game.goToLoadingScreen();
        game.client.joinGame();
      }
    });

    settingsBtn.addListener(new ChangeListener() {
      public void changed (ChangeEvent event, Actor actor) {
        System.out.println("SettingsBtn Clicked!");
        if(soundON){pressed.play();}
        game.goToSettingScreen();
      }
    });

    this.stage = stage;

  }
  public void show(){
    Gdx.input.setInputProcessor(stage);
  }

}
