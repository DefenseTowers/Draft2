package ntnu.codt.core.prototype;


public interface Prototype<O, I> {

  O copy(I input);

}
