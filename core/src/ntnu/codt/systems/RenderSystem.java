package ntnu.codt.systems;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import ntnu.codt.assets.Assets;
import ntnu.codt.components.PositionComponent;
import ntnu.codt.components.TextureComponent;
import ntnu.codt.components.TransformComponent;

public class RenderSystem extends IteratingSystem {
  private static final float WIDTH = 1280;
  private static final float HEIGHT = 720;

  private ComponentMapper<TextureComponent> tem;
  private ComponentMapper<TransformComponent> trm;
  private ComponentMapper<PositionComponent> pm;

  private final SpriteBatch batch;
  private final ShapeRenderer shape;
  private final Assets assets;
  private final OrthographicCamera camera;
  private final Array<Entity> queue;

  public RenderSystem(SpriteBatch batch, ShapeRenderer shape, Assets assets) {
    super(Family.all(TextureComponent.class, TransformComponent.class, PositionComponent.class).get());

    tem = ComponentMapper.getFor(TextureComponent.class);
    trm = ComponentMapper.getFor(TransformComponent.class);
    pm = ComponentMapper.getFor(PositionComponent.class);

    this.batch = batch;
    this.shape = shape;
    this.assets = assets;

    queue = new Array<Entity>();

    camera = new OrthographicCamera(WIDTH, HEIGHT);
    camera.position.set(WIDTH / 2, HEIGHT / 2, 0);
  }

  @Override
  public void update(float deltaTime) {
    super.update(deltaTime);

    camera.update();
    batch.setProjectionMatrix(camera.combined);
    batch.begin();

    for (Entity entity : queue) {

      TextureRegion tex = tem.get(entity).region;
      TransformComponent t = trm.get(entity);
      Vector3 pos = pm.get(entity).pos;

      final float width = tex.getRegionWidth();
      final float height = tex.getRegionHeight();
      final float originX = width * 0.5f;
      final float originY = height * 0.5f;

      batch.draw(
          tex,
          pos.x - originX, pos.y - originY,
          originX, originY,
          width, height,
          t.scale.x, t.scale.y,
          MathUtils.radiansToDegrees * t.rotation
      );

    }

    batch.end();
    queue.clear();

  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    queue.add(entity);
  }

}
