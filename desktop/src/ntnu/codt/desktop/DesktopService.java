package ntnu.codt.desktop;

import com.badlogic.gdx.math.Vector3;
import ntnu.codt.core.network.IServiceClient;
import ntnu.codt.core.network.ReceiveEndpoint;
import ntnu.codt.core.network.StartEndpoint;
import ntnu.codt.entities.Creeps;
import ntnu.codt.entities.Player;
import ntnu.codt.entities.Towers;


public class DesktopService implements IServiceClient {
  private ReceiveEndpoint receiveEndpoint;
  private StartEndpoint startEndpoint;


  @Override
  public boolean disconnect() {
    return false;
  }

  @Override
  public boolean joinGame() {
    startEndpoint.setGameScreen();
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          while (true) {
            Thread.sleep(1000);
            receiveEndpoint.receiveCreepSpawned(Creeps.SMALL_BOI, Player.P2);
          }
        } catch (InterruptedException e) {
          //
        }
      }
    });
    t1.start();

    return true;
  }

  @Override
  public Player getPlayer() {
    return Player.P1;
  }

  @Override
  public boolean towerPlaced(Vector3 pos, Towers towers, Player player) {
    return true;
  }


  @Override
  public boolean creepSent(Creeps creep, Player player) {
    return true;
  }

  @Override
  public void setReceiveEndpoint(ReceiveEndpoint endpoint) {
    receiveEndpoint= endpoint;
  }

  @Override
  public void setStartEndpoint(StartEndpoint endpoint) {
    startEndpoint = endpoint;
  }
}
