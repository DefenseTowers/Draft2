package ntnu.codt.mvc.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

import ntnu.codt.CoDT;
import ntnu.codt.core.observer.Subject;
import ntnu.codt.mvc.Controller;

public class GameController extends Controller {
  private final GameModel model;
  private Subject<Void> subjectTouch;

  public GameController(CoDT game, GameModel model) {
    super(game);
    this.model = model;

    subjectTouch = new Subject<Void>();
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
      model.entityFactory.createCreep();
    }
    if (Gdx.input.justTouched()) {
      model.touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      model.camera.unproject(model.touchPoint);
      subjectTouch.publish(null);
      System.out.println("just touched");
      if (legalTowerPlacement(new Rectangle(model.touchPoint.x - 15, model.touchPoint.y - 30, 30, 60))) {
        model.entityFactory.createTower(model.touchPoint.x, model.touchPoint.y, 30,60,300,300,20,1000);
      }
    }
  }

  @Override
  public boolean touchDown(int x, int y, int pointer, int button) {
    model.touchPoint.set(x, y, 0);
    model.camera.unproject(model.touchPoint);
    return true;
  }

}
