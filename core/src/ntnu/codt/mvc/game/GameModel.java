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

import ntnu.codt.entities.Creeps;
import ntnu.codt.entities.EntityFactory;
import ntnu.codt.entities.Player;
import ntnu.codt.entities.Projectiles;
import ntnu.codt.entities.Towers;

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
  public TiledMapTileLayer towerTiles;
  public TiledMapTileLayer pathTiles;
  public TiledMapTileLayer loadingLayer;

  public OrthogonalTiledMapRenderer renderer;
  public Skin skin;
  public Entity player1;

  public Player currentPlayer;



  public GameModel(CoDT game) {
    this.game = game;
    touchPoint = new Vector3();
    camera = new OrthographicCamera(1280, 720);
    camera.position.set(1280 / 2, 720 / 2, 0);
    viewport = new StretchViewport(1280, 720, camera);
    viewport.apply();

    map = new TmxMapLoader().load("greytilemap.tmx");

    loadingLayer = (TiledMapTileLayer)map.getLayers().get(0);
    pathTiles = (TiledMapTileLayer)map.getLayers().get(1);
    towerTiles = (TiledMapTileLayer)map.getLayers().get(2);


    renderer = new OrthogonalTiledMapRenderer(map);

    this.skin = new Skin();

    skin.add("1", game.assets.ui.one, TextureRegion.class);
    skin.add("2", game.assets.ui.two, TextureRegion.class);


    engine = new PooledEngine(100, 1000, 100, 1000);
    engine.addSystem(new AnimationSystem());
    engine.addSystem(new RenderSystem(game.batch, game.shape, game.assets));
    engine.addSystem(new TowerSystem(engine));
    engine.addSystem(new AttackSystem(engine));
    engine.addSystem(new CreepSystem(pathTiles, engine, this));
    engine.addSystem(new EconomySystem());
    currentPlayer = game.client.getPlayer();
    entityFactory = new EntityFactory(engine);
    player1 = entityFactory.createPlayer(currentPlayer, "MYNAME", 100, 250);

    CoDT.EVENT_BUS.register(engine.getSystem(EconomySystem.class));

    loadTowers();
    loadCreeps();
    loadProjectiles();


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
