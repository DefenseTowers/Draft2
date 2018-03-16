package ntnu.codt.graphics;


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

}
