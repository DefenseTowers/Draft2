package ntnu.codt.systems;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.utils.Array;

import ntnu.codt.components.DefenseComponent;
import ntnu.codt.components.HealthComponent;
import ntnu.codt.components.PositionComponent;
import ntnu.codt.components.TextureComponent;
import ntnu.codt.components.TransformComponent;
import ntnu.codt.components.VelocityComponent;

public class CreepSystem extends IteratingSystem {

  private ComponentMapper<VelocityComponent> sm;
  private ComponentMapper<PositionComponent> pm;
  private ComponentMapper<TextureComponent> tm;
  private ComponentMapper<TransformComponent> trm;

  private TiledMapTileLayer layer;
  private Array<Entity> queue;

  public CreepSystem(TiledMapTileLayer layer) {
    super(Family.all(VelocityComponent.class, PositionComponent.class, TextureComponent.class, TransformComponent.class).get());

    sm = ComponentMapper.getFor(VelocityComponent.class);
    pm = ComponentMapper.getFor(PositionComponent.class);
    tm = ComponentMapper.getFor(TextureComponent.class);
    trm = ComponentMapper.getFor(TransformComponent.class);

    queue = new Array<Entity>();
    this.layer = layer;
  }


  @Override
  public void update(float deltaTime){
    super.update(deltaTime);

    float tileHeight = layer.getTileHeight(), tileWidth = layer.getTileWidth();

    for (Entity entity : queue){
      PositionComponent pc = pm.get(entity);
      VelocityComponent vc = sm.get(entity);

      TiledMapTile tile = layer.getCell((int)Math.floor(pc.pos.x/tileWidth), (int)Math.floor(pc.pos.y/tileHeight)).getTile();
/*
      if (tile.getId()==17) {
        vc.velocity.x = 10;
        vc.velocity.y = 0;
        System.out.println("going forward");
      } else if (tile.getId()==42) {
        vc.velocity.x = 0;
        vc.velocity.y = 10;
        System.out.println("going upward");
      } else if (tile.getId()==67){
        vc.velocity.x = -10;
        vc.velocity.y = 0;
        System.out.println("going backward");
      } else if (tile.getId()==92) {
        vc.velocity.x = 0;
        vc.velocity.y = -10;
        System.out.println("going downward");
      }*/


        pc.pos.x += vc.velocity.x * deltaTime;
        pc.pos.y += vc.velocity.y * deltaTime;
    }

    queue.clear();
  }


  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    queue.add(entity);
  }


  public void clockwiseTurn(Entity entity, TiledMapTile tile){
  }

  public void counterClockwiseTurn(){

  }
}
