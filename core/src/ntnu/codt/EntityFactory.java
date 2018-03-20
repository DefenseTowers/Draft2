package ntnu.codt;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import ntnu.codt.components.AttackComponent;
import ntnu.codt.components.BoundsComponent;
import ntnu.codt.components.HealthComponent;
import ntnu.codt.components.IdentificationComponent;
import ntnu.codt.components.PlayerComponent;
import ntnu.codt.components.PositionComponent;
import ntnu.codt.components.VelocityComponent;
import ntnu.codt.components.TextureComponent;
import ntnu.codt.components.TransformComponent;

public class EntityFactory {

  private final PooledEngine engine;
  private TiledMapTileLayer layer;

  public EntityFactory(PooledEngine engine,TiledMapTileLayer layer) {
    this.engine = engine;
    this.layer = layer;
  }

  public Entity createTestEntity() {
    Entity entity = engine.createEntity();

    TextureComponent tem = engine.createComponent(TextureComponent.class);
    TransformComponent trm = engine.createComponent(TransformComponent.class);
    PositionComponent pm = engine.createComponent(PositionComponent.class);
    VelocityComponent vp = engine.createComponent(VelocityComponent.class);

    tem.region = new TextureRegion(new Texture(Gdx.files.internal("badlogic.jpg")));
    pm.pos = new Vector3(50,50,50);
    entity.add(tem);
    entity.add(trm);
    entity.add(pm);

    engine.addEntity(entity);

    return entity;
  }

  public Entity createCreep() {
    Entity entity = engine.createEntity();

    TransformComponent trm = engine.createComponent(TransformComponent.class);
    PositionComponent pm = engine.createComponent(PositionComponent.class);
    TextureComponent tem = engine.createComponent(TextureComponent.class);
    VelocityComponent vm = engine.createComponent(VelocityComponent.class);

    tem.region = new TextureRegion(new Texture(Gdx.files.internal("badlogic.jpg")));
    pm.pos = new Vector3(20*30, 0, 0);
    vm.velocity = new Vector3(0, 10, 0);

    System.out.println("created creep at pos: " + pm.pos.x + " " + pm.pos.y);


    entity.add(pm);
    entity.add(tem);
    entity.add(vm);
    entity.add(trm);

    engine.addEntity(entity);

    return entity;
  }

  public Entity createCharacterEntity(int id, int health, int funds){
    Entity entity = engine.createEntity();

    PlayerComponent pc = engine.createComponent(PlayerComponent.class);
    HealthComponent hc = engine.createComponent(HealthComponent.class);
    IdentificationComponent ic = engine.createComponent(IdentificationComponent.class);

    hc.health = health;
    pc.funds = funds;
    ic.UNIQUE_ID = id;

    entity.add(hc);
    entity.add(pc);
    entity.add(ic);
    engine.addEntity(entity);

    return entity;
  }


  public Entity createTower(float x, float y) {


    Entity entity = engine.createEntity();

    TransformComponent trm = engine.createComponent(TransformComponent.class);
    TextureComponent tem = engine.createComponent(TextureComponent.class);
    PositionComponent pm = engine.createComponent(PositionComponent.class);
    AttackComponent at = engine.createComponent(AttackComponent.class);
    BoundsComponent bc = engine.createComponent(BoundsComponent.class);

    pm.pos = new Vector3(x, y, 0);
    bc.bounds = new Rectangle(x-15,y-30,30,60);
    tem.region = new TextureRegion(new Texture(Gdx.files.internal("tower.png")));


    entity.add(trm);
    entity.add(tem);
    entity.add(pm);
    entity.add(at);
    entity.add(bc);

    engine.addEntity(entity);
    return entity;

  }
}
