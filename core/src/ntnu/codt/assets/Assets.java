package ntnu.codt.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

  public final AssetManager assetManager;
  public final Fonts fonts;
  public Skin skin;


  public Assets(AssetManager assetManager) {
    this.assetManager = assetManager;
    assetManager.setErrorListener(this);
    this.skin = new Skin(Gdx.files.internal("default/skin/uiskin.json"));
    //TODO load texture assets

    assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
    assetManager.load("tiledmap.tmx", TiledMap.class);
    assetManager.load("playBtnUp.png", Texture.class);
    assetManager.load("playBtnDown.png", Texture.class);
    assetManager.load("settingsBtnUp.png", Texture.class);
    assetManager.load("settingsBtnDown.png", Texture.class);
    assetManager.load("1.png", Texture.class);
    assetManager.load("2.png", Texture.class);
    assetManager.finishLoading();
    fonts = new Fonts();
  }

  @Override
  public void error(AssetDescriptor asset, Throwable throwable) {

  }

  @Override
  public void dispose() {

  }

  public TiledMap getMap(){
    return assetManager.get("tiledmap.tmx");
  }

  public Texture getTexture(String texName){
    return assetManager.get(texName);
  }

}


