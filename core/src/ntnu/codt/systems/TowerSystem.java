package ntnu.codt.systems;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import ntnu.codt.CoDT;
import ntnu.codt.components.*;

import static ntnu.codt.CoDT.soundON;

public class TowerSystem extends IteratingSystem {
  private ComponentMapper<AttackComponent> am;
  private ComponentMapper<PositionComponent> pm;
  private ComponentMapper<VelocityComponent> vm;
  private ComponentMapper<TowerComponent> tm;
  private PooledEngine engine;
  public Sound fire = Gdx.audio.newSound(Gdx.files.internal("sounds/flaunch.wav"));
  public Sound ice = Gdx.audio.newSound(Gdx.files.internal("sounds/iceball.wav"));
  public Sound electric = Gdx.audio.newSound(Gdx.files.internal("sounds/elDamage2.wav"));




  private void playSound(int soundNumber){
  Sound sound;
  switch (soundNumber) {
    case 1:  sound = fire;
      break;
    case 2:  sound = electric;
      break;
    case 3:  sound = ice;
      break;
    default: sound = fire;
      break;
  }
    sound.play(0.5f);
  }


  public TowerSystem(PooledEngine engine) {
    super(Family.all(TowerComponent.class, AttackComponent.class, PositionComponent.class, TransformComponent.class, BoundsComponent.class).get());

    pm = ComponentMapper.getFor(PositionComponent.class);
    am = ComponentMapper.getFor(AttackComponent.class);
    vm = ComponentMapper.getFor(VelocityComponent.class);
    tm = ComponentMapper.getFor(TowerComponent.class);

    this.engine = engine;

  }




  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    PositionComponent pc = pm.get(entity);
    AttackComponent ac = am.get(entity);
    TowerComponent tc = tm.get(entity);
    if (ac.lastShot - System.currentTimeMillis() < -ac.reloadTime && ac.creepsInRange.size() > 0) {
      if (ac.attackRadius.contains(ac.creepsInRange.get(0).getComponent(PositionComponent.class).pos.x, ac.creepsInRange.get(0).getComponent(PositionComponent.class).pos.y)) {
        ac.projectile.copy(
            engine,
            pc.pos,
            pm.get(ac.creepsInRange.get(0)).pos,
            vm.get(ac.creepsInRange.get(0)).velocity,
            ac.creepsInRange.get(0)
        );
        if(soundON){playSound(tc.sound);}
        ac.lastShot = System.currentTimeMillis();
      } else {
        ac.creepsInRange.remove(0);
      }
    }



  }


  public boolean isTowersOverlapping(Rectangle placedTower){

    ImmutableArray<Entity> towerList = getEntities();

    boolean isOverlapping = false;

    for (Entity tower : towerList){
      if(tower.getComponent(BoundsComponent.class).bounds.overlaps(placedTower))
        isOverlapping = true;
    }
    return isOverlapping;
  }
}
