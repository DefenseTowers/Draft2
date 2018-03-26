package ntnu.codt.systems;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

import ntnu.codt.components.AttackComponent;
import ntnu.codt.components.CreepComponent;
import ntnu.codt.components.DefenseComponent;
import ntnu.codt.components.HealthComponent;
import ntnu.codt.components.ObserverComponent;
import ntnu.codt.components.PositionComponent;
import ntnu.codt.components.TextureComponent;
import ntnu.codt.components.TransformComponent;
import ntnu.codt.components.VelocityComponent;
import ntnu.codt.core.observer.Observer;

public class CreepSystem extends IteratingSystem{

  private ComponentMapper<VelocityComponent> sm;
  private ComponentMapper<PositionComponent> pm;
  private ComponentMapper<TextureComponent> tm;
  private ComponentMapper<TransformComponent> trm;
  private ComponentMapper<CreepComponent> cm;

  private TiledMapTileLayer layer;
  private Array<Entity> queue;
  private List<Entity> observers = new ArrayList<Entity>();

  public CreepSystem(TiledMapTileLayer layer) {
    super(Family.all(VelocityComponent.class, PositionComponent.class, TextureComponent.class, TransformComponent.class, CreepComponent.class).get());

    sm = ComponentMapper.getFor(VelocityComponent.class);
    pm = ComponentMapper.getFor(PositionComponent.class);
    tm = ComponentMapper.getFor(TextureComponent.class);
    trm = ComponentMapper.getFor(TransformComponent.class);
    cm = ComponentMapper.getFor(CreepComponent.class);

    observers = new ArrayList<Entity>();
    queue = new Array<Entity>();
    this.layer = layer;
  }

  public void addObserver(Entity entity){
    System.out.println("tower added to observerlist");
    observers.add(entity);
  }

  @Override
  public void update(float deltaTime){
    super.update(deltaTime);

    float tileHeight = layer.getTileHeight(), tileWidth = layer.getTileWidth();

    for (Entity entity : queue) {
      PositionComponent pc = pm.get(entity);
      VelocityComponent vc = sm.get(entity);
      //System.out.println(pc.pos.x + " " + pc.pos.y);
      //System.out.println(layer.getCell((int)Math.floor(pc.pos.x/tileWidth), (int)Math.floor(pc.pos.y/tileHeight)));


      if (pc.pos.y < 720) {
        TiledMapTile tile = layer.getCell((int) Math.floor(pc.pos.x / tileWidth), (int) Math.floor(pc.pos.y / tileHeight)).getTile();
        for(int i = 0; i < observers.size(); i++){

          AttackComponent component = observers.get(i).getComponent(AttackComponent.class);
          if(component.attackRadius.contains(pc.pos.x,pc.pos.y)){
            if (!component.creepsInRange.contains(entity,true)){
              component.creepsInRange.add(entity);
              System.out.println("entity in range");
              ;
            }
          }
          else if (!component.attackRadius.contains(pc.pos.x, pc.pos.y)) {
            component.creepsInRange.removeValue(entity, true);
          }
        }



        if (tile.getId() == 17) {
          vc.velocity.x = 40;
          vc.velocity.y = 0;
          //System.out.println("going forward");
        } else if (tile.getId() == 42) {
          vc.velocity.x = 0;
          vc.velocity.y = 40;
          //System.out.println("going upward");
        } else if (tile.getId() == 67) {
          vc.velocity.x = -40;
          vc.velocity.y = 0;
          //System.out.println("going backward");
        } else if (tile.getId() == 92) {
          vc.velocity.x = 0;
          vc.velocity.y = -40;

          //System.out.println("going downward");
        }

        pc.pos.x += vc.velocity.x * deltaTime;
        pc.pos.y += vc.velocity.y * deltaTime;

      }
      if (pc.pos.y >= 720) {
        for (int i = 0; i < observers.size(); i++) {
          AttackComponent component = observers.get(i).getComponent(AttackComponent.class);
          component.creepsInRange.removeValue(entity, true);
        }
        entity.removeAll();
      }
    }

    queue.clear();
  }


  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    queue.add(entity);
  }



}


