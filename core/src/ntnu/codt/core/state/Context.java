package ntnu.codt.core.state;


public class Context {

  protected State currentState;

  public void operation() {
    currentState.operation();
  }

}
