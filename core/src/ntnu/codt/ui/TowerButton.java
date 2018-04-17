package ntnu.codt.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import ntnu.codt.entities.Towers;

/**
 * Created by oddmrog on 16.04.18.
 */

public class TowerButton extends ImageButton {

  public final Towers towerType;

  public TowerButton(Drawable imageUp, Drawable imageDown, Towers towerType) {
    super(imageUp, imageDown);
    this.towerType = towerType;
  }

  public TowerButton(Drawable imageUp, Towers towerType) {
    super(imageUp);
    this.towerType = towerType;
  }

  public TowerButton(Skin skin, Towers towerType) {
    super(skin);
    this.towerType = towerType;
  }
}
