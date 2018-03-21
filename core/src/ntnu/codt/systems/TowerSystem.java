package ntnu.codt.systems;


import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.utils.Array;

import ntnu.codt.components.AttackComponent;
import ntnu.codt.components.ObserverComponent;
import ntnu.codt.components.PositionComponent;
import ntnu.codt.components.TextureComponent;
import ntnu.codt.components.TransformComponent;
import ntnu.codt.components.VelocityComponent;

public class TowerSystem extends IteratingSystem {
  private ComponentMapper<AttackComponent> ac;
  private ComponentMapper<PositionComponent> pm;
  private ComponentMapper<TransformComponent> trm;
  private ComponentMapper<TextureComponent> tem;
  private ComponentMapper<ObserverComponent> oc;
  private Array<Entity> queue;

  public TowerSystem() {
    super(Family.all(AttackComponent.class, PositionComponent.class, TransformComponent.class, ObserverComponent.class).get());

    trm = ComponentMapper.getFor(TransformComponent.class);
    pm = ComponentMapper.getFor(PositionComponent.class);
    ac = ComponentMapper.getFor(AttackComponent.class);
    oc = ComponentMapper.getFor(ObserverComponent.class);
    queue = new Array<Entity>();

  }
/*TEST*/



  public void update(float deltaTime){
    super.update(deltaTime);


    for (Entity entity : queue) {
      PositionComponent pc = pm.get(entity);
      VelocityComponent vc = entity.getComponent(VelocityComponent.class);
      PositionComponent pm = entity.getComponent(PositionComponent.class);

      //System.out.println(pc.pos.x + " " + pc.pos.y);
      //System.out.println(layer.getCell((int)Math.floor(pc.pos.x/tileWidth), (int)Math.floor(pc.pos.y/tileHeight)));






      }


    queue.clear();
  }
  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    queue.add(entity);
  }
}
