package ntnu.codt.entities;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ntnu.codt.components.*;
import ntnu.codt.core.prototype.Prototype2;

public enum Creeps implements Prototype2<Entity, PooledEngine, Integer> {

  SMALL_BOI(20, 20, 250, 20*30, 0, 10),
  BIG_BOI(40, 40, 600, 20*30, 0, 50);

  private TextureRegion[] textureRegions;
  private final float width;
  private final float height;
  private final int hp;
  private final int startX;
  private final int startY;
  private final int bounty;

  Creeps(float width, float height, int hp, int startX, int startY, int bounty){
    this.width = width;
    this.height = height;
    this.hp = hp;
    this.startX = startX;
    this.startY = startY;
    this.bounty = bounty;

  }

  public void setTextureRegions(TextureRegion[] textureRegions) {
    this.textureRegions = textureRegions;
  }

  @Override
  public Entity copy(PooledEngine engine, Integer faction) {
    Entity entity = engine.createEntity();

    TransformComponent trm = engine.createComponent(TransformComponent.class);
    TextureComponent tem = engine.createComponent(TextureComponent.class);
    PositionComponent pm = engine.createComponent(PositionComponent.class);
    VelocityComponent vs = engine.createComponent(VelocityComponent.class);
    HealthComponent hc = engine.createComponent(HealthComponent.class);
    StateComponent sc = engine.createComponent(StateComponent.class);
    CreepComponent cc = engine.createComponent(CreepComponent.class);


    //int mirroredX = Gdx.graphics.getWidth()/2 + (Gdx.graphics.getWidth()/2 - startX);
    cc.regions = textureRegions;
    cc.faction = faction;
    cc.bounty = bounty;
    tem.region = this.textureRegions[sc.get()];
    hc.health = this.hp;
    vs.velocity = new Vector3(0, 10, 0);

    trm.rotation = 0.0f;
    trm.scale = new Vector2(1, 1);


    if(cc.faction  == 1) {
      pm.pos = new Vector3(startX, startY, 0);
      sc.set(State.NORTH);
    } else {
      pm.pos = new Vector3(20*20, Gdx.graphics.getHeight()-20,0);
      sc.set(State.SOUTH);
    }


    entity.add(trm);
    entity.add(tem);
    entity.add(pm);
    entity.add(vs);
    entity.add(hc);
    entity.add(sc);
    entity.add(cc);

    engine.addEntity(entity);

    return entity;
  }

  public static class State {
    public static final int NORTH = 2;
    public static final int SOUTH = 3;
    public static final int EAST = 0;
    public static final int WEST = 1;
  }

}