package ntnu.codt.mvc.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
import ntnu.codt.components.AttackComponent;
import ntnu.codt.components.PositionComponent;
import ntnu.codt.components.ProjectileComponent;
import ntnu.codt.components.TextureComponent;
import ntnu.codt.components.PlayerComponent;
import ntnu.codt.entities.Creeps;
import ntnu.codt.entities.Towers;
import ntnu.codt.core.observer.Subject;
import ntnu.codt.mvc.Controller;
import ntnu.codt.ui.TowerButton;

public class GameController extends Controller implements EntityListener {
  private final GameModel model;
  private Subject<Void> subjectTouch;
  private GameView view;

  public GameController(CoDT game, GameModel model, GameView gameView) {

    super(game);
    this.model = model;
    subjectTouch = new Subject<Void>();
    this.view = gameView;
    model.engine.addEntityListener(Family.all(PositionComponent.class, TextureComponent.class, AttackComponent.class)
        .exclude(ProjectileComponent.class)
        .get(),
        this);
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
        if (tiles.get(i).getId() == 395 | tiles.get(i).getId() == 1) {
          legal = true;
        } else {
          return false;
        }
      }
      return legal;
    }catch (NullPointerException e){
      System.out.println("out of bounds");
      return false;
    }

  }

  public void update(float deltaTime) {
    if (Gdx.input.isKeyJustPressed(20)){

//      model.entityFactory.createCreep();
      Creeps.SMALL_BOI.copy(model.engine, PlayerComponent.FACTION2);

    }
/*    if (Gdx.input.justTouched()) {
      model.touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      model.camera.unproject(model.touchPoint);
      subjectTouch.publish(null);
      System.out.println("just touched");
      if (legalTowerPlacement(new Rectangle(model.touchPoint.x - 15, model.touchPoint.y - 30, 30, 60))) {
        Towers.FIRE.copy(model.touchPoint, model.engine, PlayerComponent.FACTION1);
//        model.entityFactory.createTower(model.touchPoint.x, model.touchPoint.y, 30,60,300,300,20,1000);
        model.ecoBus.publish(-100);
      }

    }*/

  }

  @Override
  public boolean touchDown ( int x, int y, int pointer, int button){
/*    model.touchPoint.set(x, y, 0);
    model.camera.unproject(model.touchPoint);
    System.out.println("Just touched " + x + y);*/
    return true;
  }

  @Override
  public boolean touchDragged ( int screenX, int screenY, int pointer){
    return super.touchDragged(screenX, screenY, pointer);
  }


  public void setListeners() {


    final Array<TowerButton> towerBtnList = view.getTowerBtnList();

    for (TowerButton btn : towerBtnList) {
      final TowerButton towerButton = btn;

      towerButton.addListener(new DragListener() {

        float startX = towerButton.getX();
        float startY = towerButton.getY();

        boolean legalPlacement;

        public void touchDragged(InputEvent event, float x, float y, int pointer) {

          event.getButton();

          float height = towerButton.towerType.height;
          float width = towerButton.towerType.width;

          Rectangle boundingBox = new Rectangle(event.getStageX() - width/2, event.getStageY() - height/2, width, height);

          if(!legalTowerPlacement(boundingBox)){
            towerButton.getColor().a = 0.5f;
            legalPlacement = false;
          } else {
            towerButton.getColor().a = 1f;
            legalPlacement = true;
          }
          towerButton.moveBy(x - width / 2, y - height / 2);
        }

        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
          towerButton.setX(startX);
          towerButton.setY(startY);
          Vector3 pos = new Vector3(event.getStageX()-15, event.getStageY()-30, 0);
          if(legalPlacement) {

            towerButton.towerType.copy(pos, model.engine, 1);
//            towerButton.towerType.copy(new Towers.Pack(pos, model.engine));

          }
        }

        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          return true;
        }
      });
    }
  }

  @Override
  public void entityAdded(Entity entity) {

    int dmg = entity.getComponent(AttackComponent.class).attackDamage;
    float range = entity.getComponent(AttackComponent.class).attackRadius.radius;
    Vector3 pos = entity.getComponent(PositionComponent.class).pos;
    Texture texture = entity.getComponent(TextureComponent.class).region.getTexture();


    final Label description = new Label("Damage: " + dmg + "\n" +
        "Range: " + range + "\n", game.assets.skin);
    description.setPosition(pos.x+texture.getWidth(), pos.y+texture.getHeight(), 0);
    description.setVisible(false);

    Button btn = new Button(game.assets.skin);
    btn.setSize(texture.getWidth(), texture.getHeight());
    btn.setPosition(pos.x, pos.y);
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

    Stage stage = view.getUi();
    stage.addActor(description);
    stage.addActor(btn);

/*
    TextField description = new TextField("Damage: " + dmg + "\n" +
        "Range: " + range + "\n", textStyle);

*/

    // gameView.addActor??

    System.out.println("entity added");
  }

  @Override
  public void entityRemoved(Entity entity) {

  }
}
