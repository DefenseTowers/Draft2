package ntnu.codt;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;

/**
 * Handles room creation errors.
 */
public class CoDTRoomUpdateCallback extends RoomUpdateCallback {
  private static final String TAG = CoDTRoomUpdateCallback.class.getName();
  private AndroidLauncher activity;

  public CoDTRoomUpdateCallback(AndroidLauncher activity) {
    this.activity = activity;
  }

  @Override
  public void onRoomCreated(int code, @Nullable Room room) {
    if (code == GamesCallbackStatusCodes.OK && room != null) {
      Log.d(TAG, "Room " + room.getRoomId() + "created.");
    } else {
      Log.w(TAG, "Error creatin room: " + code);
      clearFlags();
    }
  }

  @Override
  public void onJoinedRoom(int code, @Nullable Room room) {
    if (code == GamesCallbackStatusCodes.OK && room != null) {
      Log.d(TAG, "Room " + room.getRoomId() + "joined.");
    } else {
      Log.w(TAG, "Error joining room: " + code);
      clearFlags();
    }
  }

  @Override
  public void onLeftRoom(int code, @NonNull String roomId) {
    Log.d(TAG, "Left room" + roomId);
  }

  @Override
  public void onRoomConnected(int code, @Nullable Room room) {
    if (code == GamesCallbackStatusCodes.OK && room != null) {
      Log.d(TAG, "Room " + room.getRoomId() + " connected");
    } else {
      Log.w(TAG, "Error connection to room: " + code);
      clearFlags();
    }
  }

  private void clearFlags() {
    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  }

}
