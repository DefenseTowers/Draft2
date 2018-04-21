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
  private AndroidLauncher activity;

  public CoDTMessageReceivedListener(AndroidLauncher activity) {
    this.activity = activity;
  }

  public void setEndpoint(ReceiveEndpoint endpoint) {
    this.endpoint = endpoint;
  }

  @Override
  public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
    byte[] data = realTimeMessage.getMessageData();
    String message = new String(data);
    String[] format = message.split(":");

    Log.d(TAG, message);

    final String type = format[0];

    if (endpoint != null) {
      switch (type) {
        case "T": {
          Player player = Player.valueOf(format[1]);
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
          Player player = Player.valueOf(format[1]);
          Creeps creep = Creeps.valueOf(format[2]);
          try {
            endpoint.receiveCreepSpawned(creep, player);
          } catch (InterruptedException e) {
            //
          }
          break;
        }

      }
    } else {
      switch (type) {
        case "S": {
          Log.d(TAG, "Setting plyer");
          long timeStamp = Long.valueOf(format[1]);
          if (activity.getRoom().getCreationTimestamp() > timeStamp) {
            activity.setPlayer(Player.P1);
          } else {
            activity.setPlayer(Player.P2);
          }
          Log.d(TAG, "starting ggame");
          activity.startGame();
          break;
        }
      }
    }


  }

}
