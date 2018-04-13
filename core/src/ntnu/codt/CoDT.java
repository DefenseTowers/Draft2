package ntnu.codt;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ObjectMap;

import ntnu.codt.assets.Assets;
import ntnu.codt.mvc.Controller;
import ntnu.codt.mvc.View;
import ntnu.codt.mvc.game.GameScreen;
import ntnu.codt.mvc.menu.MenuController;
import ntnu.codt.mvc.menu.MenuScreen;

public class CoDT extends Game{


	public SpriteBatch batch;
	public ShapeRenderer shape;
	public Assets assets;
	
	@Override
	public void create () {

		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		assets = new Assets(new AssetManager());



		setScreen(new MenuScreen(this));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.8f, 0.5f, 0.5f, 0.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}

	public void goToGameScreen(){
		setScreen(new GameScreen(this));
	}
	// Controls switching between screens


}
