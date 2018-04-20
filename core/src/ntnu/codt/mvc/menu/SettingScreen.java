package ntnu.codt.mvc.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import ntnu.codt.CoDT;
import ntnu.codt.mvc.BaseScreen;

/**
 * Created by oddmrog on 06.04.18.
 */

public class SettingScreen extends BaseScreen {

  private SettingView settingView;
  private InputMultiplexer mp;


  @Override
  public void dispose() {
    super.dispose();
    settingView.getStage().dispose();
  }

  public SettingScreen(CoDT game){
    super(game);

    settingView = new SettingView(game);
    settingView.loadStage();

    mp = new InputMultiplexer();
    mp.addProcessor(settingView.getStage());
  }

  @Override
  public void render(float deltaTime) {
    settingView.render(deltaTime);
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(mp);
  }


}
