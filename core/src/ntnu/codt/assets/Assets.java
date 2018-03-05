package ntnu.codt.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

  private final AssetManager assetManager;
  public final Fonts fonts;

  public Assets(AssetManager assetManager) {
    this.assetManager = assetManager;
    assetManager.setErrorListener(this);

    //TODO load texture assets

    fonts = new Fonts();
  }

  @Override
  public void error(AssetDescriptor asset, Throwable throwable) {

  }

  @Override
  public void dispose() {

  }

}
