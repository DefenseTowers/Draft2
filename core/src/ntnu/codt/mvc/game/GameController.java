package ntnu.codt.mvc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;


import ntnu.codt.CoDT;
import ntnu.codt.entities.Creeps;
import ntnu.codt.entities.Towers;
import ntnu.codt.core.observer.Subject;
import ntnu.codt.mvc.Controller;

public class GameController extends Controller {
  private final GameModel model;
  private Subject<Void> subjectTouch;
  private GameView view;
  private Stage ui;

  public GameController(CoDT game, GameModel model, GameView gameView) {

    super(game);
    this.model = model;
    subjectTouch = new Subject<Void>();
    this.ui = gameView.getUi();
    this.view = gameView;
    setListeners();

  }

  private boolean legalTowerPlacement(Rectangle bounds){
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

  }

  public void update(float deltaTime) {
    if (Gdx.input.isKeyJustPressed(20)){
//      model.entityFactory.createCreep();
      Creeps.SMALL_BOI.copy(new Creeps.Pack(model.touchPoint, model.engine));

    }
    if (Gdx.input.justTouched()) {
      model.touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      model.camera.unproject(model.touchPoint);
      subjectTouch.publish(null);
      System.out.println("just touched");
      if (legalTowerPlacement(new Rectangle(model.touchPoint.x - 15, model.touchPoint.y - 30, 30, 60))) {
        Towers.FIRE.copy(new Towers.Pack(model.touchPoint, model.engine));
//        model.entityFactory.createTower(model.touchPoint.x, model.touchPoint.y, 30,60,300,300,20,1000);
        model.ecoBus.publish(-100);
      }

    }

  }

  @Override
  public boolean touchDown ( int x, int y, int pointer, int button){
    model.touchPoint.set(x, y, 0);
    model.camera.unproject(model.touchPoint);
    System.out.println("Just touched " + x + y);
    return true;
  }

  @Override
  public boolean touchDragged ( int screenX, int screenY, int pointer){
    return super.touchDragged(screenX, screenY, pointer);
  }


  public void setListeners() {

    final int screenHeight = Gdx.graphics.getHeight();
    final int screenWidth = Gdx.graphics.getWidth();
    final Actor dragImage = ui.getActors().get(0);
    final Actor playBtn = ui.getActors().get(1);
    Array<ImageButton> towerBtnList = view.getTowerBtnList();
    //System.out.println(ui.getActors().toString());

    for (ImageButton btn : towerBtnList) {
      final ImageButton imgbtn = btn;
      imgbtn.addListener(new DragListener() {

        float startx = imgbtn.getX();
        float starty = imgbtn.getY();

        public void touchDragged(InputEvent event, float x, float y, int pointer) {
          float xx = imgbtn.getX();
          float yy = imgbtn.getY();
          imgbtn.moveBy(x - imgbtn.getWidth() / 2, y - imgbtn.getHeight() / 2);
          //System.out.println("dragged image coords:" + xx + "," + yy);

          if ((screenHeight - xx - yy) > 0) {
            System.out.println("under lgiine" + screenHeight + "x: " + xx + "y: " + yy);
            //dragImage.setDrawable(skin.getDrawable("2"));


          } else {
            System.out.println("above line");
            //dragImage.setDrawable(skin.getDrawable("1"));
          }
        }

        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
          imgbtn.setX(startx);
          imgbtn.setY(starty);

          Vector3 pos = new Vector3(event.getStageX()-15, event.getStageY()-30, 0);
          Towers.FIRE.copy(new Towers.Pack(pos, model.engine));

        }

        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
          return true;
        }
      });
    }
  }
}
