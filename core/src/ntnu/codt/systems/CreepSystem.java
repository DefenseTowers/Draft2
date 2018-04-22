package ntnu.codt.systems;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;
import java.util.List;

import ntnu.codt.CoDT;
import ntnu.codt.components.*;
import ntnu.codt.entities.Creeps;
import ntnu.codt.entities.Player;
import ntnu.codt.events.CreepDied;
import ntnu.codt.events.FundsChanged;
import ntnu.codt.mvc.game.GameModel;

public class CreepSystem extends IteratingSystem{

  private ComponentMapper<VelocityComponent> vm;
  private ComponentMapper<PositionComponent> pm;
  private ComponentMapper<TextureComponent> tm;
  private ComponentMapper<TransformComponent> trm;
  private ComponentMapper<HealthComponent> hm;
  private ComponentMapper<StateComponent> sm;
  private ComponentMapper<CreepComponent> cm;
  private ComponentMapper<AllegianceComponent> am;
  private ComponentMapper<AttackComponent> atm;

  private TiledMapTileLayer layer;
  private List<Entity> observers = new ArrayList<Entity>();
  private GameModel model;
  private PooledEngine engine;

  private final float tileHeight;
  private final float tileWidth;

  public CreepSystem(TiledMapTileLayer layer, PooledEngine engine, GameModel model) {
    super(Family.all(
        VelocityComponent.class,
        PositionComponent.class,
        TextureComponent.class,
        TransformComponent.class,
        HealthComponent.class,
        StateComponent.class,
        CreepComponent.class,
        AllegianceComponent.class
    ).get());

    vm = ComponentMapper.getFor(VelocityComponent.class);
    pm = ComponentMapper.getFor(PositionComponent.class);
    tm = ComponentMapper.getFor(TextureComponent.class);
    trm = ComponentMapper.getFor(TransformComponent.class);
    hm = ComponentMapper.getFor(HealthComponent.class);
    sm = ComponentMapper.getFor(StateComponent.class);
    cm = ComponentMapper.getFor(CreepComponent.class);
    am = ComponentMapper.getFor(AllegianceComponent.class);
    atm = ComponentMapper.getFor(AttackComponent.class);

    observers = new ArrayList<Entity>();
    this.layer = layer;
    this.model = model;
    this.engine = engine;

    tileHeight = layer.getTileHeight();
    tileWidth = layer.getTileWidth();
  }

  public void addObserver(Entity entity){
    observers.add(entity);
  }

  @Override
  public void update(float deltaTime){
    super.update(deltaTime);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {

    PositionComponent pc = pm.get(entity);
    VelocityComponent vc = vm.get(entity);
    HealthComponent hc = hm.get(entity);
    StateComponent sc = sm.get(entity);
    TextureComponent tc = tm.get(entity);
    CreepComponent cc = cm.get(entity);
    AllegianceComponent ac = am.get(entity);

    if (pc.pos.y < 720 && pc.pos.y >= 0) {
      TiledMapTile tile = layer.getCell(
          (int) Math.floor(pc.pos.x / tileWidth),
          (int) Math.floor(pc.pos.y / tileHeight)
      ).getTile();

      for(Entity tower : observers) {

        AttackComponent attc = atm.get(tower);
        AllegianceComponent atc = am.get(tower);
        if (atc.loyalty != ac.loyalty) {
          if(attc.attackRadius.contains(pc.pos.x,pc.pos.y)){
            if (!attc.creepsInRange.contains(entity)){
              attc.creepsInRange.add(entity);
            }
          }
          else if (!attc.attackRadius.contains(pc.pos.x, pc.pos.y) & attc.creepsInRange.contains(entity)) {
            attc.creepsInRange.remove(entity);
          }
        }
      }

      if (tile.getId() == 2343 || tile.getId() == 18) {
        vc.velocity.set(vc.maxVel, 0, 0);
        sc.set(Creeps.State.EAST);
      } else if (tile.getId() == 2342) {
        vc.velocity.set(0, vc.maxVel, 0);
        sc.set(Creeps.State.NORTH);
      } else if (tile.getId() == 2345) {
        vc.velocity.set(-vc.maxVel, 0, 0);
        sc.set(Creeps.State.WEST);
      } else if (tile.getId() == 2344) {
        vc.velocity.set(0, -vc.maxVel, 0);
        sc.set(Creeps.State.SOUTH);
      }

      pc.pos.add(vc.velocity.x * deltaTime, vc.velocity.y * deltaTime, 0);
    }

    if (pc.pos.y >= 720 || pc.pos.y < 0 || hc.health <= 0) {
      for (Entity tower : observers) {
        AttackComponent act = tower.getComponent(AttackComponent.class);
        act.creepsInRange.remove(entity);
      }
      engine.removeEntity(entity);

      CoDT.EVENT_BUS.post(new CreepDied(cc.bounty, ac.loyalty == Player.P1 ? PlayerComponent.FACTION2 : PlayerComponent.FACTION1));

    }
    tc.region = cc.regions[sc.get()];
  }

}
