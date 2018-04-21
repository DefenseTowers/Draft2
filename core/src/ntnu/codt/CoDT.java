package ntnu.codt;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ntnu.codt.assets.Assets;
import ntnu.codt.core.eventhandler.EventBus;
import ntnu.codt.core.network.IServiceClient;
import ntnu.codt.mvc.game.GameModel;
import ntnu.codt.mvc.game.GameScreen;
import ntnu.codt.mvc.menu.LoadingScreen;
import ntnu.codt.mvc.menu.MenuScreen;
import ntnu.codt.mvc.menu.SettingScreen;

public class CoDT extends Game{

  public static EventBus EVENT_BUS;
	public SpriteBatch batch;
	public ShapeRenderer shape;
	public Assets assets;
	public IServiceClient client;
	private GameModel gameModel;

	public CoDT(IServiceClient client) {
	  this.client = client;
  }
	
	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);


		EVENT_BUS = new EventBus();
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		assets = new Assets(new AssetManager());
		gameModel = new GameModel(this);

		setScreen(new MenuScreen(this));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.8f, 0.5f, 0.5f, 0.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}

	public void goToGameScreen(){
		setScreen(new GameScreen(this, gameModel));
	}

	public void goToSettingScreen(){
		setScreen(new SettingScreen(this));
	}
	public void goToLoadingScreen(){
		setScreen(new LoadingScreen(this, gameModel));
	}
}
