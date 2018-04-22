package ntnu.codt.mvc.menu;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import ntnu.codt.CoDT;
import ntnu.codt.components.CreepComponent;
import ntnu.codt.components.PositionComponent;
import ntnu.codt.components.VelocityComponent;
import ntnu.codt.entities.Creeps;
import ntnu.codt.entities.EntityFactory;
import ntnu.codt.mvc.BaseScreen;
import ntnu.codt.mvc.game.GameModel;
import ntnu.codt.systems.AnimationSystem;
import ntnu.codt.systems.CreepSystem;

/**
 * Created by oddmrog on 06.04.18.
 */

public class LoadingScreen extends BaseScreen {

  private LoadingView loadingView;
  private GameModel model;
  private Entity loadingCreep;

  public LoadingScreen(CoDT game, GameModel model) {
    super(game);
    this.model = model;

    loadingView = new LoadingView(game);
    loadingView.loadStage();

    model.engine.getSystem(CreepSystem.class).setLayer(model.loadingLayer);
    loadingCreep = model.entityFactory.createCreep(100);

  }

  @Override
  public void dispose() {
    super.dispose();
    loadingView.getStage().dispose();

  }


  @Override
  public void render(float deltaTime) {
    loadingView.render(deltaTime);
    model.engine.update(deltaTime);
  }


  @Override
  public void show() {
  }

  @Override
  public void hide(){

  model.engine.removeEntity(loadingCreep);


  }
}
