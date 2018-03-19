package ntnu.codt;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import ntnu.codt.components.HealthComponent;
import ntnu.codt.components.IdentificationComponent;
import ntnu.codt.components.PlayerComponent;
import ntnu.codt.components.PositionComponent;
import ntnu.codt.components.VelocityComponent;
import ntnu.codt.components.TextureComponent;
import ntnu.codt.components.TransformComponent;

public class EntityFactory {

  private final PooledEngine engine;

  public EntityFactory(PooledEngine engine) {
    this.engine = engine;
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
    pm.pos = new Vector3(20*29, 20*35, 0);
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

  public Entity createTower() {
    return engine.createEntity();
  }
}
