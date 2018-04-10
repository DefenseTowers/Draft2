package ntnu.codt.entities;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import ntnu.codt.components.*;
import ntnu.codt.core.prototype.Prototype;
import ntnu.codt.systems.CreepSystem;

public enum Towers implements Prototype<Entity, Towers.Pack> {

  FIRE("tower.png", 30, 60, 500, 300, 1, 10),
  WATER("water.png", 30, 60, 150, 300, 5, 100),
  ICE("ice.png", 30, 60, 300 , 300, 5, 200);

  private final String texture;
  private final float width;
  private final float height;
  private final float radius;
  private final float av;
  private final int damage;
  private final long reload;


  Towers(String texture, float width, float height, float radius, float av, int damage, long reload) {
    this.texture = texture;
    this.width = width;
    this.height = height;
    this.radius = radius;
    this.av = av;
    this.damage = damage;
    this.reload = reload;
  }

  @Override
  public Entity copy(Pack pack) {
    Entity entity = pack.engine.createEntity();


    TransformComponent trm = pack.engine.createComponent(TransformComponent.class);
    TextureComponent tem = pack.engine.createComponent(TextureComponent.class);
    PositionComponent pm = pack.engine.createComponent(PositionComponent.class);
    AttackComponent at = pack.engine.createComponent(AttackComponent.class);
    BoundsComponent bc = pack.engine.createComponent(BoundsComponent.class);

    pm.pos = pack.pos.cpy();
    bc.bounds = new Rectangle(pm.pos.x-(this.width /2),pm.pos.y-(this.height /2),this.width,this.height);
    tem.region = new TextureRegion(new Texture(Gdx.files.internal("towers/" + this.texture)));
    at.attackRadius = new Circle(pm.pos.x,pm.pos.y,this.radius);
    at.attackDamage = this.damage;
    at.lastShot =  System.currentTimeMillis();
    at.attackVelocity = this.av;
    at.reloadTime = this.reload;


    entity.add(trm);
    entity.add(tem);
    entity.add(pm);
    entity.add(at);
    entity.add(bc);

    pack.engine.getSystem(CreepSystem.class).addObserver(entity);
    pack.engine.addEntity(entity);

    return entity;
  }

  public static class Pack {
    public final Vector3 pos;
    public final PooledEngine engine;

    public Pack(Vector3 pos, PooledEngine engine) {
      this.pos = pos;
      this.engine = engine;
    }

  }

}
