package ntnu.codt.mvc.game;



import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;


import ntnu.codt.CoDT;

import ntnu.codt.components.AllegianceComponent;
import ntnu.codt.core.eventhandler.Subscribe;
import ntnu.codt.core.network.ReceiveEndpoint;
import ntnu.codt.entities.Creeps;


import ntnu.codt.entities.Player;
import ntnu.codt.entities.Towers;

import ntnu.codt.events.TowerPlaced;
import ntnu.codt.mvc.Controller;
import ntnu.codt.systems.EconomySystem;
import ntnu.codt.systems.TowerSystem;
import ntnu.codt.ui.CreepButton;
import ntnu.codt.ui.TowerButton;

public class GameController extends Controller implements ReceiveEndpoint {
  private final GameModel model;
  private GameView view;
  private Sound placeSFX;


  private EconomySystem economySystem;

  private final List<UpdateAction> updateQueue;


  public GameController(CoDT game, GameModel model, GameView gameView) {

    super(game);
    this.model = model;
    this.view = gameView;
    this.economySystem = model.engine.getSystem(EconomySystem.class);
    CoDT.EVENT_BUS.register(this);
    setListeners();

    placeSFX = Gdx.audio.newSound(Gdx.files.internal("sounds/Click_Heavy_00.wav"));

    updateQueue = new ArrayList<UpdateAction>();
  }

  private boolean legalTowerPlacement(Rectangle bounds, Player player){

    try {

      float tileHeight = model.towerTiles.getTileHeight(), tileWidth = model.towerTiles.getTileWidth();
      TiledMapTile bottomLeft = model.towerTiles.getCell((int) Math.floor(bounds.getX() / tileWidth), (int) Math.floor(bounds.getY() / tileHeight)).getTile();
      TiledMapTile topLeft = model.towerTiles.getCell((int) Math.floor(bounds.getX() / tileWidth), (int) Math.floor((bounds.getY()+bounds.getHeight()) / tileHeight)).getTile();
      TiledMapTile topRight = model.towerTiles.getCell((int) Math.floor((bounds.getX()+bounds.getWidth()) / tileWidth), (int) Math.floor((bounds.getY()+bounds.getHeight()) / tileHeight)).getTile();;
      TiledMapTile bottomRight = model.towerTiles.getCell((int) Math.floor((bounds.getX()+bounds.getWidth()) / tileWidth), (int) Math.floor((bounds.getY()+bounds.getHeight()) / tileHeight)).getTile();
      boolean legal = true;

      List<TiledMapTile> tiles = new ArrayList<TiledMapTile>();
      tiles.add(bottomLeft);
      tiles.add(topLeft);
      tiles.add(topRight);
      tiles.add(bottomRight);

      //370,369,345,346
      for (int i = 0; i < tiles.size(); i++) {
        System.out.println("tile ID: " +tiles.get(i).getId());
        if (tiles.get(i).getId() == player.towerTile) {
          legal = true;
        } else {
          legal = false;
          break;
        }
      }

      if(model.engine.getSystem(TowerSystem.class).isTowersOverlapping(bounds))
        legal = false;
      return legal;
    }catch (NullPointerException e){
      System.out.println("out of bounds");

      return false;
    }

  }

  public void update(float deltaTime) {

    if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
      game.goToMenuScreen();
    }

    if (Gdx.input.isKeyJustPressed(20)) {

      Creeps.SMALL_BOI.copy(model.engine, model.currentPlayer);
      game.client.creepSent(Creeps.SMALL_BOI, model.currentPlayer);
    }
    for (UpdateAction action : updateQueue) {
      action.call();
    }
    updateQueue.clear();

  }

  @Override
  public boolean touchDown ( int x, int y, int pointer, int button){
    return true;
  }

  @Override
  public boolean touchDragged ( int screenX, int screenY, int pointer){
    return super.touchDragged(screenX, screenY, pointer);
  }


  public void setListeners() {
    final ImageButton upgradeTowerBtn = view.getUi().getRoot().findActor("upgradeTowerBtn");
    final Array<TowerButton> towerBtnList = view.getTowerBtnList();
    final Array<CreepButton> creepBtnList = view.getCreepBtnList();
    final EconomySystem economySystem = model.engine.getSystem(EconomySystem.class);

    for (TowerButton btn : towerBtnList) {
      final TowerButton towerButton = btn;
      towerButton.addListener(new DragListener() {

        float startX = towerButton.getX();
        float startY = towerButton.getY();
        boolean legalPlacement, sufficientFunds;
        int price = towerButton.towerType.price;
        Entity player = model.player1;

        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          if(economySystem.sufficientFunds(player, price)) {
            sufficientFunds = true;
          } else
            sufficientFunds = false;
          return true;
        }

        public void touchDragged(InputEvent event, float x, float y, int pointer) {
          model.touchPoint.set(event.getStageX(), event.getStageY(), 0);
          //model.viewport.project(model.touchPoint);

          event.getButton();

          float towerHeight = towerButton.towerType.height;
          float towerWidth = towerButton.towerType.width;


          Vector3 v = model.touchPoint.cpy();



          Rectangle boundingBox = new Rectangle(v.x - towerWidth/2, v.y - towerHeight/2, towerWidth, towerHeight);


          if(legalTowerPlacement(boundingBox, model.currentPlayer) && sufficientFunds){
            towerButton.getColor().a = 1f;
            legalPlacement = true;
          } else {
            towerButton.getColor().a = 0.5f;
            legalPlacement = false;
          }
          towerButton.setPosition(model.touchPoint.x - towerButton.getWidth() / 2, model.touchPoint.y - towerButton.getHeight() / 2);
          towerButton.rangeImage.setPosition(model.touchPoint.x - towerButton.towerType.radius, model.touchPoint.y - towerButton.towerType.radius);
          towerButton.rangeImage.setVisible(true);
        }

        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

          model.touchPoint.set(event.getStageX(), event.getStageY(), 0);
          //model.camera.unproject(model.touchPoint);

          towerButton.setX(startX);
          towerButton.setY(startY);
          Vector3 pos = new Vector3(event.getStageX(), event.getStageY(), 0);



          if(legalPlacement && sufficientFunds) {
            towerButton.towerType.copy(model.touchPoint, model.engine, model.currentPlayer);
            game.client.towerPlaced(model.touchPoint, towerButton.towerType, model.currentPlayer);


            CoDT.EVENT_BUS.post(new TowerPlaced(towerButton.towerType, pos));
            economySystem.doTransaction(player, price);
          }
          towerButton.rangeImage.setVisible(false);
        }

      });
    }

    for (CreepButton cb : creepBtnList){
      final CreepButton creepBtn = cb;
      creepBtn.addListener(new ChangeListener() {
        public void changed (ChangeEvent event, Actor actor) {
          if(economySystem.sufficientFunds(model.player1, creepBtn.creep.bounty)) {
            economySystem.doTransaction(model.player1, creepBtn.creep.bounty);
            creepBtn.creep.copy(model.engine, model.currentPlayer);
            game.client.creepSent(creepBtn.creep, model.currentPlayer);
          }
        }
      });
    }

  }

  @Subscribe
  public void towerAdded(TowerPlaced event) {

    int dmg = event.tower.damage;
    float radius = event.tower.radius;
    Vector3 pos = event.pos;
    float width = event.tower.width;
    float height = event.tower.height;
    Stage stage = view.getUi();

    //TowerButton upgradeTowerBtn = new TowerButton();

    final Label description = new Label(
        "Type: " + event.tower + "\n" +
             "Rate of fire: " + event.tower.reload + "\n" +
             "Damage: " + dmg + "\n" +
             "Range: " + (int)radius + "\n", game.assets.skin
    );

    description.setPosition(pos.x+width, pos.y+height, 0);
    description.setVisible(false);

    Button btn = new Button(game.assets.skin);
    btn.setSize(width, height);
    btn.setPosition(pos.x - width/2, pos.y - height/2);
    btn.getColor().a = 0f;
    btn.setVisible(true);
    btn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        if(description.isVisible()){
          description.setVisible(false);
        } else {
          description.setVisible(true);
        }
      }
    });
    stage.addActor(description);
    stage.addActor(btn);
    System.out.println("entity added");
  }


  @Override
  public void receiveTowerPlaced(final Vector3 pos, final Towers tower, final Player player) throws InterruptedException {
    updateQueue.add(new UpdateAction() {
      @Override
      public void call() {
        tower.copy(pos, model.engine, player);
      }
    });
  }

  @Override
  synchronized public void receiveCreepSpawned(final Creeps creep, final Player player) throws InterruptedException {
    Gdx.app.error("CREEP", player.name() + ":" + creep.name());
    updateQueue.add(new UpdateAction() {
      @Override
      public void call() {
        creep.copy(model.engine, player);
      }
    });

  }

  private interface UpdateAction {
    void call();
  }


}
