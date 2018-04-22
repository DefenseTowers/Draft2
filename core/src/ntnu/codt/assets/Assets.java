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

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;

public class Assets implements Disposable, AssetErrorListener {
  private final String ATLAS_PATH = "dtatlas.atlas";

  public final AssetManager assetManager;
  public final Fonts fonts;

  public Skin skin;

  public final AssetTowers towers;
  public final AssetCreeps creeps;
  public final AssetProjectiles projectiles;
  public final AssetUI ui;
  public final Texture menuScreen;


  public Assets(AssetManager assetManager) {
    this.assetManager = assetManager;
    assetManager.setErrorListener(this);
    this.skin = new Skin(Gdx.files.internal("default/skin/uiskin.json"));
    //TODO load texture assets
    menuScreen = new Texture("menuScreen.png");
    assetManager.load(ATLAS_PATH, TextureAtlas.class);
    assetManager.finishLoading();


    for (String a : assetManager.getAssetNames()) {
      Gdx.app.debug("ASSETS", a);
    }

    TextureAtlas atlas = assetManager.get(ATLAS_PATH);
    for (Texture t : atlas.getTextures()) {
      t.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    towers = new AssetTowers(atlas);
    creeps = new AssetCreeps(atlas);
    projectiles = new AssetProjectiles(atlas);
    ui = new AssetUI(atlas);


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

  public class AssetUI {
    public final AtlasRegion playButtonUp;
    public final AtlasRegion playButtonDown;
    public final AtlasRegion settingsButtonUp;
    public final AtlasRegion settingsButtonDown;
    public final AtlasRegion one;
    public final AtlasRegion two;
    public final AtlasRegion goldDisplay;

    public AssetUI(TextureAtlas atlas) {
      playButtonUp = atlas.findRegion("playBtnUp");
      playButtonDown = atlas.findRegion("playBtnDown");
      settingsButtonUp = atlas.findRegion("settingsBtnUp");
      settingsButtonDown = atlas.findRegion("settingsBtnDown");
      one = atlas.findRegion("fire2");
      two = atlas.findRegion("fire2");
      goldDisplay = atlas.findRegion("goldDisplay");
    }
  }

  public class AssetTowers {
    public final AtlasRegion fire;
    public final AtlasRegion ice;
    public final AtlasRegion lightning;

    public AssetTowers(TextureAtlas atlas) {
      fire = atlas.findRegion("fire");
      ice = atlas.findRegion("ice");
      lightning = atlas.findRegion("lightning");
    }
  }

  public class AssetCreeps {
    public final TextureRegion[] little;
    public final TextureRegion[] bigboy;

    public AssetCreeps(TextureAtlas atlas) {
      TextureRegion[][] temp = atlas.findRegion("little_haunt").split(38, 40);
      little = temp[0];
      bigboy = atlas.findRegion("big_boi").split(80,80)[0];
    }
  }

  public class AssetProjectiles {
    public final Animation<TextureRegion> fire;
    public final Animation<TextureRegion> ice;
    public final IntMap<Animation<TextureRegion>> lightning;

    public AssetProjectiles(TextureAtlas atlas) {
      TextureRegion tempRegion = atlas.findRegion("fire_projectile");
      TextureRegion[][] fsheet = tempRegion.split(104, 40);
      fire = new Animation<TextureRegion>(
          0.1f,
          fsheet[0][0], fsheet[0][1], fsheet[0][2],
          fsheet[1][0], fsheet[1][1], fsheet[1][2]
      );
      fire.setPlayMode(Animation.PlayMode.LOOP);


      tempRegion = atlas.findRegion("ice_projectile");
      TextureRegion[][] isheet = tempRegion.split(104, 40);
      ice = new Animation<TextureRegion>(
          0.1f,
          isheet[0][0], isheet[0][1], isheet[0][2],
          isheet[1][0], isheet[1][1], isheet[1][2]
      );
      ice.setPlayMode(Animation.PlayMode.LOOP);

      tempRegion = atlas.findRegion("lightning_projectile");
      TextureRegion[] lsheet = tempRegion.split(100, 40)[0];
      lightning = new IntMap<Animation<TextureRegion>>();
      for (int i = 0; i < lsheet.length; i += 4) {
        Animation<TextureRegion> a = new Animation<TextureRegion>(
            0.2f,
            lsheet[i], lsheet[i+1], lsheet[i+2], lsheet[i+3]
        );
        a.setPlayMode(Animation.PlayMode.LOOP);
        lightning.put(i / 4, a);
      }
    }
  }

}


