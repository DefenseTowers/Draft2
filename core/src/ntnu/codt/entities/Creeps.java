package ntnu.codt.entities;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ntnu.codt.components.*;
import ntnu.codt.core.prototype.Prototype2;

public enum Creeps implements Prototype2<Entity, PooledEngine, Player> {

  SMALL_BOI(20, 20, 250, 40),
  BIG_BOI(40, 40, 600, 30);

  private TextureRegion[] textureRegions;
  private final float width;
  private final float height;
  private final int hp;
  private final int maxVel;

  Creeps(float width, float height, int hp, int maxVel){
    this.width = width;
    this.height = height;
    this.hp = hp;
    this.maxVel = maxVel;
  }

  public void setTextureRegions(TextureRegion[] textureRegions) {
    this.textureRegions = textureRegions;
  }

  @Override
  public Entity copy(PooledEngine engine, Player player) {
    Entity entity = engine.createEntity();

    TransformComponent trm = engine.createComponent(TransformComponent.class);
    TextureComponent tem = engine.createComponent(TextureComponent.class);
    PositionComponent pm = engine.createComponent(PositionComponent.class);
    VelocityComponent vs = engine.createComponent(VelocityComponent.class);
    HealthComponent hc = engine.createComponent(HealthComponent.class);
    StateComponent sc = engine.createComponent(StateComponent.class);
    CreepComponent cc = engine.createComponent(CreepComponent.class);
    AllegianceComponent ac = engine.createComponent(AllegianceComponent.class);

    ac.loyalty = player;
    sc.set(State.NORTH);
    cc.regions = textureRegions;
    pm.pos = player.getStartPos();
    tem.region = this.textureRegions[sc.get()];
    hc.health = this.hp;
    vs.velocity = new Vector3(0, 0, 0);
    vs.maxVel = maxVel;

    trm.rotation = 0.0f;
    trm.scale = new Vector2(1, 1);

    entity.add(trm);
    entity.add(tem);
    entity.add(pm);
    entity.add(vs);
    entity.add(hc);
    entity.add(sc);
    entity.add(cc);
    entity.add(ac);

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