package ntnu.codt.mvc.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

import ntnu.codt.CoDT;
import ntnu.codt.components.PlayerComponent;
import ntnu.codt.core.eventhandler.Subscribe;
import ntnu.codt.entities.Creeps;
import ntnu.codt.core.observer.Subject;
import ntnu.codt.events.FundsChanged;
import ntnu.codt.events.TowerPlaced;
import ntnu.codt.mvc.Controller;
import ntnu.codt.systems.TowerSystem;
import ntnu.codt.ui.TowerButton;

public class GameController extends Controller{
  private final GameModel model;
  private Subject<Void> subjectTouch;
  private GameView view;

  public GameController(CoDT game, GameModel model, GameView gameView) {

    super(game);
    this.model = model;
    subjectTouch = new Subject<Void>();
    this.view = gameView;
    CoDT.EVENT_BUS.register(this);
    setListeners();

  }



  private boolean legalTowerPlacement(Rectangle bounds){

    try {

      float tileHeight = model.layer2.getTileHeight(), tileWidth = model.layer.getTileWidth();
      TiledMapTile bottomLeft = model.layer2.getCell((int) Math.floor(bounds.getX() / tileWidth), (int) Math.floor(bounds.getY() / tileHeight)).getTile();
      TiledMapTile topLeft = model.layer2.getCell((int) Math.floor(bounds.getX() / tileWidth), (int) Math.floor((bounds.getY()+bounds.getHeight()) / tileHeight)).getTile();
      TiledMapTile topRight = model.layer2.getCell((int) Math.floor((bounds.getX()+bounds.getWidth()) / tileWidth), (int) Math.floor((bounds.getY()+bounds.getHeight()) / tileHeight)).getTile();;
      TiledMapTile bottomRight = model.layer2.getCell((int) Math.floor((bounds.getX()+bounds.getWidth()) / tileWidth), (int) Math.floor((bounds.getY()+bounds.getHeight()) / tileHeight)).getTile();
      boolean legal = true;

      List<TiledMapTile> tiles = new ArrayList<TiledMapTile>();
      tiles.add(bottomLeft);
      tiles.add(topLeft);
      tiles.add(topRight);
      tiles.add(bottomRight);

      //370,369,345,346
      for (int i = 0; i < tiles.size(); i++) {
        if (tiles.get(i).getId() == 395) {
          legal = true;
        } else {
          legal = false;
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
    if (Gdx.input.isKeyJustPressed(20)) {
      Creeps.BIG_BOI.copy(model.engine, PlayerComponent.FACTION2);
      Creeps.SMALL_BOI.copy(model.engine, PlayerComponent.FACTION1);
    }
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

    final Array<TowerButton> towerBtnList = view.getTowerBtnList();
    final int funds = model.player1.getComponent(PlayerComponent.class).funds;


    for (TowerButton btn : towerBtnList) {
      final TowerButton towerButton = btn;

      towerButton.addListener(new DragListener() {

        float startX = towerButton.getX();
        float startY = towerButton.getY();
        boolean legalPlacement, sufficientFunds;

        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

          if(funds < towerButton.towerType.price)
            sufficientFunds = false;
          else
            sufficientFunds = true;
          return true;
        }

        public void touchDragged(InputEvent event, float x, float y, int pointer) {

          event.getButton();
          float height = towerButton.towerType.height;
          float width = towerButton.towerType.width;

          Rectangle boundingBox = new Rectangle(event.getStageX() - width/2, event.getStageY() - height/2, width, height);

          if(legalTowerPlacement(boundingBox) && sufficientFunds){
            towerButton.getColor().a = 1f;
            legalPlacement = true;
          } else {
            towerButton.getColor().a = 0.5f;
            legalPlacement = false;
          }
          towerButton.setPosition(event.getStageX() - towerButton.getWidth() / 2, event.getStageY() - towerButton.getHeight() / 2);
          towerButton.rangeImage.setPosition(event.getStageX() - towerButton.towerType.radius, event.getStageY() - towerButton.towerType.radius);
          towerButton.rangeImage.setVisible(true);
        }

        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

          towerButton.setX(startX);
          towerButton.setY(startY);
          Vector3 pos = new Vector3(event.getStageX(), event.getStageY(), 0);

          if(legalPlacement) {
            towerButton.towerType.copy(pos, model.engine, 1);
            CoDT.EVENT_BUS.post(new TowerPlaced(towerButton.towerType, pos));
            CoDT.EVENT_BUS.post(new FundsChanged(funds - towerButton.towerType.price));
            System.out.println("Current funds =" + funds);
          }
          towerButton.rangeImage.setVisible(false);
        }

      });
    }
  }

  @Subscribe
  public void entityAdded(TowerPlaced event) {
    //Flytte denne metoden til View?
    int dmg = event.tower.damage;
    float radius = event.tower.radius;
    Vector3 pos = event.pos;
    float width = event.tower.width;
    float height = event.tower.height;
    Stage stage = view.getUi();


    final Label description = new Label(
        "Type: " + event.tower + "\n" +
             "Rate of fire: " + event.tower.reload + "\n" +
             "Damage: " + dmg + "\n" +
             "Range: " + (int)radius + "\n", game.assets.skin
    );

    int x = Gdx.graphics.getWidth();
    int y = Gdx.graphics.getHeight();

/*    com.badlogic.gdx.graphics.Pixmap p = new Pixmap(diameter, diameter, Pixmap.Format.RGBA8888);
    p.setColor(255, 255, 255, 0.2f);
    p.fillCircle((int)radius,(int)radius, diameter/2);

    p.setBlending(com.badlogic.gdx.graphics.Pixmap.Blending.None);
    Texture t = new Texture(p);
    final Image attackRange = new Image(t);
    attackRange.setPosition(pos.x - radius, pos.y - radius);
    attackRange.setVisible(false);*/

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
          //attackRange.setVisible(true);
        }
      }
    });
    //stage.addActor(attackRange);
    stage.addActor(description);
    stage.addActor(btn);
    System.out.println("entity added");
  }




}
