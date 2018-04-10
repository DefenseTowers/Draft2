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
import com.badlogic.gdx.scenes.scene2d.ui.Cell;

import ntnu.codt.CoDT;
import ntnu.codt.EntityFactory;
import ntnu.codt.systems.AnimationSystem;
import ntnu.codt.systems.AttackSystem;
import ntnu.codt.systems.CreepSystem;
import ntnu.codt.systems.RenderSystem;
import ntnu.codt.systems.TowerSystem;

public class GameModel {
  public final EntityFactory entityFactory;
  public final OrthographicCamera camera;
  public final PooledEngine engine;
  public final Vector3 touchPoint;
  public TiledMap map;
  public TiledMapTileLayer layer;
  public TiledMapTileLayer layer2;
  public OrthogonalTiledMapRenderer renderer;


  public GameModel(CoDT game) {
    touchPoint = new Vector3();
    camera = new OrthographicCamera(1280, 720);
    camera.position.set(1280 / 2, 720 / 2, 0);

    map = new TmxMapLoader().load("tiledmap2.tmx");
    layer = (TiledMapTileLayer)map.getLayers().get(1);
    layer2 = (TiledMapTileLayer)map.getLayers().get(0);
    renderer = new OrthogonalTiledMapRenderer(map);


    engine = new PooledEngine();

    engine.addSystem(new RenderSystem(game.batch, game.shape, game.assets));
    engine.addSystem(new TowerSystem(engine));
    engine.addSystem(new AnimationSystem());
    engine.addSystem(new AttackSystem());
    engine.addSystem(new CreepSystem(layer));

    entityFactory = new EntityFactory(engine,layer);

    //entityFactory.createTestEntity();

    System.out.println("number of entities in engine: " +  engine.getEntities().size());


  }

  public void update(float deltaTime) {
    camera.update();
    engine.update(deltaTime);
    renderer.setView(camera);


  }

  public EntityFactory getEntityFactory(){
    return entityFactory;
  }

}
