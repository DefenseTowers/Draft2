package ntnu.codt.systems;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import ntnu.codt.components.AnimationComponent;
import ntnu.codt.components.TextureComponent;

public class AnimationSystem extends IteratingSystem {
  private ComponentMapper<TextureComponent> tm;
  private ComponentMapper<AnimationComponent> am;

  private Array<Entity> queue;
  private OrthographicCamera camera;

  public AnimationSystem() {
    super(Family.all(TextureComponent.class, AnimationComponent.class).get());

    tm = ComponentMapper.getFor(TextureComponent.class);
    am = ComponentMapper.getFor(AnimationComponent.class);
  }

  @Override
  public void update(float deltaTime) {

  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    queue.add(entity);
  }

}
