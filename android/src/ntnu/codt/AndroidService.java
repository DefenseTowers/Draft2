package ntnu.codt;


import com.badlogic.gdx.math.Vector3;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import ntnu.codt.core.network.IServiceClient;
import ntnu.codt.core.network.ReceiveEndpoint;
import ntnu.codt.entities.Creeps;
import ntnu.codt.entities.Player;

public class AndroidService implements IServiceClient {
  private ReceiveEndpoint endpoint;

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

  @Override
  public void setReceiveEndpoint(ReceiveEndpoint endpoint) {
    this.endpoint = endpoint;
  }
}
