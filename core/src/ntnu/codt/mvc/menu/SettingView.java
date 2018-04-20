package ntnu.codt.mvc.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ntnu.codt.CoDT;
import ntnu.codt.mvc.View;

/**
 * Created by oddmrog on 17.04.18.
 */

public class SettingView implements View{

  private Stage stage;
  private CoDT game;

  public SettingView(CoDT game) {
    this.stage = new Stage();
    this.game = game;
    loadStage();

  }


  @Override
  public void render(float deltaTime) {
    stage.draw();
  }

  public void loadStage(){

    int screenHeight = Gdx.graphics.getHeight();
    int screenWidth = Gdx.graphics.getWidth();

    CheckBox soundCheckBtn = new CheckBox("Sound", game.assets.skin);
    soundCheckBtn.setSize(screenWidth/10, screenHeight/10);
    soundCheckBtn.setPosition(screenWidth/2 - soundCheckBtn.getWidth()/2, screenHeight*60/100 - soundCheckBtn.getHeight()/2);
    soundCheckBtn.addListener(new ClickListener(){
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        //toggle music here
        System.out.println("Soundbutton clicked");
      }
    });


    Skin skin = game.assets.skin;
    skin.add("playBtnUp", game.assets.ui.playButtonUp, TextureRegion.class);
    skin.add("playBtnDown", game.assets.ui.playButtonDown, TextureRegion.class);

    Button backBtn = new Button(skin.getDrawable("playBtnUp"), skin.getDrawable("playBtnDown"));
    //backBtn.setSize(width/10, height/10);
    backBtn.setPosition(screenWidth/2 - backBtn.getWidth()/2, screenHeight*70/100 - backBtn.getHeight()/2);
    backBtn.addListener(new ClickListener(){
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        System.out.println("Back button clicked");
        returnToMenu();
      }
    });

    stage.addActor(backBtn);
    stage.addActor(soundCheckBtn);

  }

  public Stage getStage(){
    return stage;
  }

  public void returnToMenu(){
    game.setScreen(new MenuScreen(game));
  }


}
