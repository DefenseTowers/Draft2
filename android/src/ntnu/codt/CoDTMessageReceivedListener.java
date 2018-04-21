package ntnu.codt;

import android.support.annotation.NonNull;
import android.util.Log;
import com.badlogic.gdx.math.Vector3;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import ntnu.codt.core.network.ReceiveEndpoint;
import ntnu.codt.entities.Creeps;
import ntnu.codt.entities.Player;
import ntnu.codt.entities.Towers;


public class CoDTMessageReceivedListener implements OnRealTimeMessageReceivedListener {
  private final static String TAG = CoDTMessageReceivedListener.class.getName();
  private ReceiveEndpoint endpoint;

  public void setEndpoint(ReceiveEndpoint endpoint) {
    this.endpoint = endpoint;
  }

  @Override
  public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
    byte[] data = realTimeMessage.getMessageData();
    String message = new String(data);
    String[] format = message.split(":");

    Log.d(TAG, message);

    final Player player = Player.valueOf(format[0]);
    final String type = format[1];

    if (endpoint != null) {
      switch (type) {
        case "T": {
          Towers tower = Towers.valueOf(format[2]);
          Vector3 pos = new Vector3(Float.valueOf(format[3]), Float.valueOf(format[4]), 0);
          try {
            endpoint.receiveTowerPlaced(pos, tower, player);
          } catch (InterruptedException e) {
            //
          }
          break;
        }
        case "C": {
          Creeps creep = Creeps.valueOf(format[2]);
          try {
            endpoint.receiveCreepSpawned(creep, player);
          } catch (InterruptedException e) {
            //
          }
          break;
        }
      }
    }


  }

}
