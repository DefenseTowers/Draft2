package ntnu.codt;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ntnu.codt.assets.Assets;
import ntnu.codt.screens.GameScreen;
import ntnu.codt.screens.MenuScreen;

public class CoDT extends Game {
	public SpriteBatch batch;
	public ShapeRenderer shape;
	public Assets assets;
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		Gdx.input.setCatchBackKey(true);

		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		assets = new Assets(new AssetManager());

		setScreen(new GameScreen(this));
	}

	@Override
  public void render() {
	  Gdx.gl.glClearColor(1.0f, 0.0f, 1.0f, 0.0f);
	  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	  super.render();
  }

}
