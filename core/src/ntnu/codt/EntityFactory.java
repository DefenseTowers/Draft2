package ntnu.codt;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ntnu.codt.components.PositionComponent;
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

    tem.region = new TextureRegion(new Texture(Gdx.files.internal("badlogic.jpg")));

    entity.add(tem);
    entity.add(trm);
    entity.add(pm);

    engine.addEntity(entity);

    return entity;
  }

  public Entity createTower() {
    return engine.createEntity();
  }

  public Entity createCreep() {
    return engine.createEntity();
  }

}
