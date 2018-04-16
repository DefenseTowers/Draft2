package ntnu.codt.entities;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ntnu.codt.components.*;
import ntnu.codt.core.prototype.Prototype3;
import ntnu.codt.systems.CreepSystem;

public enum Towers implements Prototype3<Entity, Vector3, PooledEngine, Integer> {

 FIRE(30, 60, 500, 200, 1, 250, Projectiles.FIRE),
  WATER(30, 60, 150, 300, 5, 100, Projectiles.FIRE),
  ICE(30, 60, 300 , 300, 5, 200, Projectiles.ICE);

  public TextureRegion textureRegion;
  public final float width;
  public final float height;
  public final float radius;
  public final float av;
  public final int damage;
  public final long reload;
  public final Projectiles projectile;

  Towers(float width, float height, float radius, float av, int damage, long reload, Projectiles projectile) {
    this.width = width;
    this.height = height;
    this.radius = radius;
    this.av = av;
    this.damage = damage;
    this.reload = reload;
    this.projectile = projectile;
  }

  public void setTextureRegion(TextureRegion region) {
    this.textureRegion = region;
  }

  @Override
  public Entity copy(Vector3 pos, PooledEngine engine, Integer faction) {
    Entity entity = engine.createEntity();

    TransformComponent trm = engine.createComponent(TransformComponent.class);
    TextureComponent tem = engine.createComponent(TextureComponent.class);
    PositionComponent pm = engine.createComponent(PositionComponent.class);
    AttackComponent at = engine.createComponent(AttackComponent.class);
    BoundsComponent bc = engine.createComponent(BoundsComponent.class);
    TowerComponent tc = engine.createComponent(TowerComponent.class);

    tc.faction = faction;
    pm.pos = pos.cpy();
    bc.bounds = new Rectangle(pm.pos.x-(this.width /2),pm.pos.y-(this.height /2),this.width,this.height);
    tem.region = this.textureRegion;
    at.attackRadius = new Circle(pm.pos.x,pm.pos.y,this.radius);
    at.attackDamage = this.damage;
    at.lastShot =  System.currentTimeMillis();
    at.attackVelocity = this.av;
    at.reloadTime = this.reload;
    at.projectile = this.projectile;

    trm.rotation = 0.0f;
    trm.scale = new Vector2(1, 1);

    entity.add(trm);
    entity.add(tem);
    entity.add(pm);
    entity.add(at);
    entity.add(bc);
    entity.add(tc);

    engine.getSystem(CreepSystem.class).addObserver(entity);
    engine.addEntity(entity);

    return entity;
  }

}
