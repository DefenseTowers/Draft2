package ntnu.codt.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {
  private final String ATLAS_PATH = "defensetowers.atlas";

  public final AssetManager assetManager;
  public final Fonts fonts;
  public final AssetTowers towers;
  public final AssetCreeps creeps;
  public final AssetProjectiles projectiles;

  public Assets(AssetManager assetManager) {
    this.assetManager = assetManager;
    assetManager.setErrorListener(this);

    //TODO load texture assets

    //assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
    //assetManager.load("tiledmap.tmx", TiledMap.class);
    assetManager.load(ATLAS_PATH, TextureAtlas.class);
    assetManager.load("playBtnUp.png", Texture.class);
    assetManager.load("playBtnDown.png", Texture.class);
    assetManager.load("settingsBtnUp.png", Texture.class);
    assetManager.load("settingsBtnDown.png", Texture.class);
    assetManager.load("1.png", Texture.class);
    assetManager.load("2.png", Texture.class);

    assetManager.finishLoading();

    for (String a : assetManager.getAssetNames()) {
      Gdx.app.debug("ASSETS", a);
    }

    TextureAtlas atlas = assetManager.get(ATLAS_PATH);
    for (Texture t : atlas.getTextures()) {
      t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    towers = new AssetTowers(atlas);
    creeps = new AssetCreeps(atlas);
    projectiles = new AssetProjectiles(atlas);

    fonts = new Fonts();
  }

  @Override
  public void error(AssetDescriptor asset, Throwable throwable) {
    throwable.printStackTrace();
    Gdx.app.debug("test", asset.fileName);

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

  public class AssetTowers {
    public final AtlasRegion fire;
    public final AtlasRegion ice;

    public AssetTowers(TextureAtlas atlas) {
      fire = atlas.findRegion("tower_fire");
      ice = atlas.findRegion("tower_ice");
    }
  }

  public class AssetCreeps {
    public final TextureRegion[] little;
    public final AtlasRegion bigboy;

    public AssetCreeps(TextureAtlas atlas) {
      TextureRegion[][] temp = atlas.findRegion("creep_little_haunt_spritesheet").split(38, 40);
      little = temp[0];
      bigboy = atlas.findRegion("little_haunt");
    }
  }

  public class AssetProjectiles {
    public final Animation<TextureRegion> fire;

    public AssetProjectiles(TextureAtlas atlas) {
      TextureRegion tempRegion = atlas.findRegion("fire_spritesheet");
      TextureRegion[][] fsheet = tempRegion.split(104, 40);

      for (TextureRegion[] r : fsheet) {
        for (TextureRegion t : r) {
          t.flip(true, false);
        }
      }

      fire = new Animation<TextureRegion>(
          0.1f,
          fsheet[0][0], fsheet[0][1], fsheet[0][2],
          fsheet[1][0], fsheet[1][1], fsheet[1][2]
      );
      fire.setPlayMode(Animation.PlayMode.LOOP);
    }
  }

}


