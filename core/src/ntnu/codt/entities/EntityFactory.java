package ntnu.codt.entities;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import ntnu.codt.components.AllegianceComponent;
import ntnu.codt.components.AttackComponent;
import ntnu.codt.components.BoundsComponent;
import ntnu.codt.components.CreepComponent;
import ntnu.codt.components.HealthComponent;
import ntnu.codt.components.IdentificationComponent;
import ntnu.codt.components.PlayerComponent;
import ntnu.codt.components.PositionComponent;
import ntnu.codt.components.VelocityComponent;
import ntnu.codt.components.TextureComponent;
import ntnu.codt.components.TransformComponent;
import ntnu.codt.systems.CreepSystem;

import java.util.ArrayDeque;
import java.util.Queue;

public class EntityFactory {

  private final PooledEngine engine;


  public EntityFactory(PooledEngine engine) {
    this.engine = engine;
  }


  public Entity createPlayer(Player p, String playerName, int funds, int health) {

    Entity player = engine.createEntity();
    PlayerComponent pc = engine.createComponent(PlayerComponent.class);
    AllegianceComponent ac = engine.createComponent(AllegianceComponent.class);
    pc.name = playerName;
    pc.funds = funds;
    pc.health = health;
    pc.score = 0;
    ac.loyalty = p;
    player.add(pc);
    player.add(ac);
    engine.addEntity(player);

    return player;
  }

  public Entity createCreep(int speed){
    Entity creep = Creeps.BIG_BOI.copy(engine, Player.P2);
    creep.getComponent(VelocityComponent.class).maxVel = speed;
    creep.getComponent(PositionComponent.class).pos.x = 10*20;
    creep.getComponent(PositionComponent.class).pos.y = 21*20;
    //engine.addEntity(creep);
    return creep;


  }

}