package ntnu.codt.mvc.game;



import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import ntnu.codt.CoDT;
import ntnu.codt.EntityFactory;
import ntnu.codt.systems.AnimationSystem;
import ntnu.codt.systems.CreepSystem;
import ntnu.codt.systems.RenderSystem;
import ntnu.codt.systems.TowerSystem;

public class GameModel {
  private final EntityFactory entityFactory;
  public final OrthographicCamera camera;
  public final PooledEngine engine;
  public final Vector3 touchPoint;
  public TiledMap map;
  public TiledMapTileLayer layer;
  public OrthogonalTiledMapRenderer renderer;
  public Skin skin;



  public GameModel(CoDT game) {
    touchPoint = new Vector3();
    camera = new OrthographicCamera(1280, 720);
    camera.position.set(1280 / 2, 720 / 2, 0);
    map = game.assets.getMap();
    layer = (TiledMapTileLayer)map.getLayers().get(1);
    renderer = new OrthogonalTiledMapRenderer(map);

    this.skin = new Skin();

    skin.add("1", game.assets.getTexture("1.png"));
    skin.add("2", game.assets.getTexture("2.png"));

    engine = new PooledEngine();
    engine.addSystem(new RenderSystem(game.batch, game.shape, game.assets));
    engine.addSystem(new TowerSystem());
    engine.addSystem(new AnimationSystem());
    engine.addSystem(new CreepSystem(layer));

    entityFactory = new EntityFactory(engine);

    entityFactory.createCreep();

  }

  public void update(float deltaTime) {
    camera.update();
    engine.update(deltaTime);
    renderer.setView(camera);
  }

}
