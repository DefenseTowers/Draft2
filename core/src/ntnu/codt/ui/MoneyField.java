package ntnu.codt.ui;


import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import ntnu.codt.CoDT;
import ntnu.codt.core.eventhandler.Subscribe;
import ntnu.codt.events.FundsChanged;

/**
 * Created by oddmrog on 16.04.18.
 */

public class MoneyField extends TextField {

  public MoneyField(String text, TextFieldStyle style) {

    super(text, style);
    CoDT.EVENT_BUS.register(this);
  }

  @Subscribe
  public void updateFunds(FundsChanged event){
    super.setText(event.amount+"");
  }
}
