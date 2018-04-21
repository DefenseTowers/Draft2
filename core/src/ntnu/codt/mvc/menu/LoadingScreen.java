package ntnu.codt.mvc.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import ntnu.codt.CoDT;
import ntnu.codt.mvc.BaseScreen;

/**
 * Created by oddmrog on 06.04.18.
 */

public class LoadingScreen extends BaseScreen {

  private LoadingView loadingView;
  private float progress;


  public LoadingScreen(CoDT game){
    super(game);

    loadingView = new LoadingView(game);
    loadingView.loadStage();
  }



  @Override
  public void dispose() {
    super.dispose();
    loadingView.getStage().dispose();
  }


  @Override
  public void render(float deltaTime) {

    loadingView.render(deltaTime);
  }


  @Override
  public void show() {

  }


}
