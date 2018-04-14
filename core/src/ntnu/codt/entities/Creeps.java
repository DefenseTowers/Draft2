package ntnu.codt.entities;

/**
 * Created by Gemcluster on 10/04/2018.
 */

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ntnu.codt.components.*;
import ntnu.codt.core.prototype.Prototype1;

public enum Creeps implements Prototype1<Entity, PooledEngine> {

  SMALL_BOI(20, 20, 250, 20*30, 0),
  BIG_BOI(40, 40, 600, 20*30, 0);

  private TextureRegion textureRegion;
  private final float width;
  private final float height;
  private final int hp;
  private final int startx;
  private final int starty;

  Creeps(float width, float height, int hp, int startx, int starty){
    this.width = width;
    this.height = height;
    this.hp = hp;
    this.startx = startx;
    this.starty = starty;
  }

  public void setTextureRegion(TextureRegion textureRegion) {
    this.textureRegion = textureRegion;
  }

  @Override
  public Entity copy(PooledEngine engine) {
    Entity entity = engine.createEntity();

    TransformComponent trm = engine.createComponent(TransformComponent.class);
    TextureComponent tem = engine.createComponent(TextureComponent.class);
    PositionComponent pm = engine.createComponent(PositionComponent.class);
    VelocityComponent vs = engine.createComponent(VelocityComponent.class);
    HealthComponent hc = engine.createComponent(HealthComponent.class);

    pm.pos = new Vector3(startx, starty, 0);
    tem.region = this.textureRegion;
    hc.health = this.hp;
    vs.velocity = new Vector3(0, 10, 0);

    trm.rotation = 0.0f;
    trm.scale = new Vector2(1, 1);

    entity.add(trm);
    entity.add(tem);
    entity.add(pm);
    entity.add(vs);
    entity.add(hc);

    engine.addEntity(entity);

    return entity;
  }

}