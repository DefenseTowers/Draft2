package ntnu.codt.core.strategy;


public class Context<I, O> {

  private Strategy<I, O> strategy;

  public void setStrategy(Strategy<I, O> strategy) {
    this.strategy = strategy;
  }

  public O execute(I input) {
    return strategy.execute(input);
  }

}
