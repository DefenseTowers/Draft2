package ntnu.codt.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import ntnu.codt.entities.Towers;

/**
 * Created by oddmrog on 16.04.18.
 */

public class TowerButton extends ImageButton {

  public final Towers towerType;
  public final Image rangeImage;

  public TowerButton(Drawable imageUp, Drawable imageDown, Towers towerType, Image rangeImage) {
    super(imageUp, imageDown);
    this.towerType = towerType;
    this.rangeImage = rangeImage;
  }

  public TowerButton(Drawable imageUp, Towers towerType, Image rangeImage) {
    super(imageUp);
    this.towerType = towerType;
    this.rangeImage = rangeImage;
  }


}
