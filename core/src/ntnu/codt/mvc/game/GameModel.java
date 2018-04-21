package ntnu.codt.mvc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;

import com.badlogic.gdx.utils.viewport.StretchViewport;
import ntnu.codt.CoDT;
import ntnu.codt.entities.*;
import ntnu.codt.components.PlayerComponent;
import ntnu.codt.core.observer.Observer;
import ntnu.codt.core.observer.Subject;
import ntnu.codt.systems.*;

public class GameModel {
  private final CoDT game;
  public final EntityFactory entityFactory;
  public final OrthographicCamera camera;
  public final StretchViewport viewport;
  public final PooledEngine engine;
  public final Vector3 touchPoint;
  public TiledMap map;
  public TiledMapTileLayer layer;
  public TiledMapTileLayer layer2;
  public OrthogonalTiledMapRenderer renderer;
  public Skin skin;
  public Entity player1;
  public Player currentPlayer;
  public Subject<Integer> ecoBus = new Subject<Integer>();


  public GameModel(CoDT game) {
    this.game = game;
    touchPoint = new Vector3();
    camera = new OrthographicCamera(1280, 720);
    camera.position.set(1280 / 2, 720 / 2, 0);
    viewport = new StretchViewport(1280, 720, camera);
    viewport.apply();

    map = new TmxMapLoader().load("greytilemap.tmx");
    layer = (TiledMapTileLayer)map.getLayers().get(0);
    layer2 = (TiledMapTileLayer)map.getLayers().get(1);

    renderer = new OrthogonalTiledMapRenderer(map);

    this.skin = new Skin();

    skin.add("1", game.assets.ui.one, TextureRegion.class);
    skin.add("2", game.assets.ui.two, TextureRegion.class);


    engine = new PooledEngine(100, 1000, 100, 1000);
    engine.addSystem(new AnimationSystem());
    engine.addSystem(new RenderSystem(game.batch, game.shape, game.assets));
    engine.addSystem(new TowerSystem(engine));
    engine.addSystem(new AttackSystem(engine));
    engine.addSystem(new CreepSystem(layer, engine, this));
    engine.addSystem(new EconomySystem());

    CoDT.EVENT_BUS.register(engine.getSystem(EconomySystem.class));

    loadTowers();
    loadCreeps();
    loadProjectiles();

    entityFactory = new EntityFactory(engine,layer);

    currentPlayer = game.client.getPlayer();

    player1 = entityFactory.createPlayer("bjarne", 1);
    ecoBus.subscribe(new Observer<Integer>() {
      @Override
      public void call(Integer input) {
        player1.getComponent(PlayerComponent.class).funds = input;
      }
    });

    //entityFactory.createTestEntity();

    System.out.println("number of entities in engine: " +  engine.getEntities().size());

  }

  public void update(float deltaTime) {
    camera.update();
    engine.update(deltaTime);
    renderer.setView(camera);
  }

  private void loadTowers() {
    Towers.FIRE.setTextureRegion(game.assets.towers.fire);
    Towers.ICE.setTextureRegion(game.assets.towers.ice);
    Towers.LIGHTNING.setTextureRegion(game.assets.towers.lightning);
  }

  private void loadCreeps() {
    Creeps.SMALL_BOI.setTextureRegions(game.assets.creeps.little);
    Creeps.BIG_BOI.setTextureRegions(game.assets.creeps.bigboy);
  }

  private void loadProjectiles() {
    Projectiles.FIRE.setAnimation(game.assets.projectiles.fire);
    Projectiles.ICE.setAnimation(game.assets.projectiles.ice);
    Projectiles.LIGHTNING.setAnimations(game.assets.projectiles.lightning);
  }

}
