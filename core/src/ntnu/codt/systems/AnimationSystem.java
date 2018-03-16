package ntnu.codt.systems;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ntnu.codt.components.AnimationComponent;
import ntnu.codt.components.StateComponent;
import ntnu.codt.components.TextureComponent;

public class AnimationSystem extends IteratingSystem {
  private ComponentMapper<TextureComponent> tm;
  private ComponentMapper<AnimationComponent> am;
  private ComponentMapper<StateComponent> sm;

  public AnimationSystem() {
    super(Family.all(TextureComponent.class, AnimationComponent.class, StateComponent.class).get());

    tm = ComponentMapper.getFor(TextureComponent.class);
    am = ComponentMapper.getFor(AnimationComponent.class);
    sm = ComponentMapper.getFor(StateComponent.class);
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    TextureComponent tc = tm.get(entity);
    AnimationComponent ac = am.get(entity);
    StateComponent sc = sm.get(entity);

    Animation<TextureRegion> animation = ac.animations.get(sc.get());

    if (animation != null) {
      tc.region = animation.getKeyFrame(sc.time);
    }

    sc.time += deltaTime;
  }

}
