package ntnu.codt.core.strategy;


public interface Strategy<I, O> {

  O execute(I input);

}
