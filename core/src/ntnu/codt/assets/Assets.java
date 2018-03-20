package ntnu.codt.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

  private final AssetManager assetManager;
  public final Fonts fonts;


  public Assets(AssetManager assetManager) {
    this.assetManager = assetManager;
    assetManager.setErrorListener(this);

    //TODO load texture assets

    assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
    assetManager.load("tiledmap.tmx", TiledMap.class);
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

}
