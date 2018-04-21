package ntnu.codt.mvc.menu;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.scenes.scene2d.Stage;

import ntnu.codt.CoDT;
import ntnu.codt.entities.EntityFactory;
import ntnu.codt.mvc.View;
import ntnu.codt.systems.CreepSystem;

/**
 * Created by oddmrog on 21.04.18.
 */

public class LoadingView implements View {

  private Stage stage;
  private CoDT game;


  public LoadingView(CoDT game) {

    this.stage = new Stage();
    this.game = game;
    loadStage();

    PooledEngine engine = new PooledEngine(100, 1000, 100, 1000);
    engine.addSystem(new CreepSystem());
  }


  @Override
  public void render(float deltaTime) {

  }

  public void loadStage(){





  }

  public Stage getStage() {
    return stage;
  }
}
