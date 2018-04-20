package ntnu.codt.core.network;


import com.badlogic.gdx.math.Vector3;
import ntnu.codt.entities.Creeps;
import ntnu.codt.entities.Player;
import ntnu.codt.entities.Towers;

public interface ReceiveEndpoint {

  void receiveTowerPlaced(Vector3 pos, Towers tower, Player player) throws InterruptedException;
  void receiveCreepSpawned(Creeps creep, Player player) throws InterruptedException;

}
