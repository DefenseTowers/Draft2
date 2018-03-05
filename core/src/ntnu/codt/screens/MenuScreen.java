package ntnu.codt.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import ntnu.codt.CoDT;

public class MenuScreen extends ScreenAdapter {
  private final int VIEWPORT_WIDTH = 1280;
  private final int VIEWPORT_HEIGHT = 720;
  private final CoDT game;
  OrthographicCamera guiCamera;
  Vector3 touchPoint;

  public MenuScreen(CoDT game) {
    this.game = game;

    guiCamera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    guiCamera.position.set(VIEWPORT_WIDTH / 2, VIEWPORT_HEIGHT / 2, 0);

    touchPoint = new Vector3();
  }

  public void update() {

    if (Gdx.input.justTouched()) {
      touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0);
      guiCamera.unproject(touchPoint);
    }

  }

  public void draw() {
    Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
  }

  @Override
  public void render(final float deltaTime) {
    update();
    draw();
  }

}
