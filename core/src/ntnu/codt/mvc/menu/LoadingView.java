package ntnu.codt.mvc.menu;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import ntnu.codt.CoDT;
import ntnu.codt.mvc.View;
/**
 * Created by oddmrog on 21.04.18.
 */

public class LoadingView implements View {

  private Stage stage;
  private CoDT game;


  public LoadingView(CoDT game) {

    this.game = game;
    loadStage();

  }


  @Override
  public void render(float deltaTime) {


    stage.draw();
  }

  public void loadStage(){
    this.stage = new Stage();
    com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle textStyle = new com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle();
    textStyle.font = new BitmapFont();
    textStyle.fontColor = Color.WHITE;

    TextField textfield = new TextField("Loading...", textStyle);
    textfield.setPosition(32*20, 19*20);
    stage.addActor(textfield);

  }

  public Stage getStage() {
    return stage;
  }


}
