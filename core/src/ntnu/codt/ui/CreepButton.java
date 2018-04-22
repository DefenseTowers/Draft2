package ntnu.codt.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import ntnu.codt.entities.Creeps;

/**
 * Created by oddmrog on 20.04.18.
 */

public class CreepButton extends ImageButton {
  public final Creeps creep;

  public CreepButton(Drawable imageUp, Creeps creep) {
    super(imageUp);
    this.creep = creep;
  }



}
