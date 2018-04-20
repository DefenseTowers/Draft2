package ntnu.codt.graphics;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Pixmap extends com.badlogic.gdx.graphics.Pixmap {

  public Pixmap(final int width, final int height, Format format) {
    super(width, height, format);
  }

  public void drawLine(final int x, final int y, final int x2, final int y2, final int size) {
    for (int i = 1; i < size; i++) {
      drawLine(x - i, y - i, x2 - i, y2 - i);
      drawLine(x + i, y + i, x2 + i, y2 + i);
    }
    drawLine(x, y, x2, y2);
  }

  public void drawRectangle(final int x, final int y, final int w, final int h, final int size) {
    for (int i = 1; i < size; i++) {
      drawRectangle(x + i, y + i, w - i * 2, h - i * 2);
      drawRectangle(x - i, y - i, w + i * 2, h + i * 2);
    }
    drawRectangle(x, y, w, h);
  }

  public void drawTextureRegionCenter(TextureRegion tr) {
    int sx = (getWidth() - tr.getRegionWidth()) / 2;
    int sy = (getHeight() - tr.getRegionHeight()) / 2;
    drawTextureRegion(tr, sx, sy);
  }

  public void drawTextureRegion(TextureRegion tr, int sx, int sy) {
    Texture t = tr.getTexture();
    t.getTextureData().prepare();
    com.badlogic.gdx.graphics.Pixmap tp = t.getTextureData().consumePixmap();
    for (int x = 0; x < tr.getRegionWidth(); x++) {
      for (int y = 0; y < tr.getRegionHeight(); y++) {
        drawPixel(x + sx, y + sy, tp.getPixel(x + tr.getRegionX(), y + tr.getRegionY()));
      }
    }
    t.getTextureData().disposePixmap();
  }

}
