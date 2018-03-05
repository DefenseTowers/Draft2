package ntnu.codt.components;


import com.badlogic.ashley.core.Component;

public class StateComponent implements Component {

  private int state = 0;
  private float time = 0.0f;

  public int get() {
    return state;
  }

  public void set(int state) {
    this.state = state;
  }

}
