package ntnu.codt.core.network;


import com.badlogic.gdx.math.Vector3;
import ntnu.codt.entities.Creeps;
import ntnu.codt.entities.Player;

public interface IServiceClient {

  boolean connect();
  boolean joinGame();
  Player getPlayer();
  boolean towerPlaced(Vector3 pos, Player player);
  boolean creepSent(Creeps creep, Player player);
  void setReceiveEndpoint(ReceiveEndpoint endpoint);

}
