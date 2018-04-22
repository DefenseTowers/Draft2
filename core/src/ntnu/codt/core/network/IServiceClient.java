package ntnu.codt.core.network;


import com.badlogic.gdx.math.Vector3;
import ntnu.codt.entities.Creeps;
import ntnu.codt.entities.Player;
import ntnu.codt.entities.Towers;

public interface IServiceClient {

  boolean joinGame();
  Player getPlayer();
  boolean towerPlaced(Vector3 pos, Towers towers, Player player);
  boolean creepSent(Creeps creep, Player player);
  void setReceiveEndpoint(ReceiveEndpoint endpoint);
  void setStartEndpoint(StartEndpoint endpoint);

}
