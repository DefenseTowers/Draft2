package ntnu.codt.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Fonts {

  public final BitmapFont fontSmall;
  public final BitmapFont fontMedium;
  public final BitmapFont fontLarge;
  public final ShaderProgram shader;

  public Fonts() {
    Texture texture = new Texture(Gdx.files.internal("fonts/font.png"));
    texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

    FileHandle fontFile = Gdx.files.internal("fonts/font.fnt");
    FileHandle shaderVert = Gdx.files.internal("fonts/font.vert");
    FileHandle shaderFrag = Gdx.files.internal("fonts/font.frag");
    TextureRegion textureRegion = new TextureRegion(texture);

    fontSmall = new BitmapFont(fontFile, textureRegion, false);
    fontMedium = new BitmapFont(fontFile, textureRegion, false);
    fontLarge = new BitmapFont(fontFile, textureRegion, false);
    fontMedium.getData().setScale(2.0f);
    fontLarge.getData().setScale(4.0f);

    shader = new ShaderProgram(shaderVert, shaderFrag);
    if (!shader.isCompiled()) {
      Gdx.app.error("FontShader", "compilation failed:\n" + shader.getLog());
    }

  }

}
