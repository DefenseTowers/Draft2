package ntnu.codt;


import com.badlogic.gdx.math.Vector3;
import ntnu.codt.core.network.IServiceClient;
import ntnu.codt.entities.Creeps;
import ntnu.codt.entities.Player;

public class AndroidService implements IServiceClient {
  @Override
  public boolean connect() {
    return false;
  }

  @Override
  public boolean joinGame() {
    return false;
  }

  @Override
  public Player getPlayer() {
    return null;
  }

  @Override
  public boolean towerPlaced(Vector3 pos, Player player) {
    return false;
  }

  @Override
  public boolean creepSent(Creeps creep, Player player) {
    return false;
  }
}
