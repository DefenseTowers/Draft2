package ntnu.codt;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;


public class CoDTRoomStatusUpdateCallback extends RoomStatusUpdateCallback {
  private static final String TAG = CoDTRoomStatusUpdateCallback.class.getName();
  private AndroidLauncher activity = null;

  public CoDTRoomStatusUpdateCallback(AndroidLauncher activity) {
    this.activity = activity;
  }

  @Override
  public void onRoomConnecting(@Nullable Room room) {

  }

  @Override
  public void onRoomAutoMatching(@Nullable Room room) {

  }

  @Override
  public void onPeerInvitedToRoom(@Nullable Room room, @NonNull List<String> list) {

  }

  @Override
  public void onPeerDeclined(@Nullable Room room, @NonNull List<String> list) {
    if (!activity.isPlaying() && activity.shouldCancelGame(room)) {
      Games.getRealTimeMultiplayerClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
          .leave(activity.getmJoinedRoomConfig(), room.getRoomId());
      activity.clearFlagKeepScreen();
    }
  }

  @Override
  public void onPeerJoined(@Nullable Room room, @NonNull List<String> list) {
    Log.d(TAG, "SOMEONE JOINED!!!!");
  }

  @Override
  public void onPeerLeft(@Nullable Room room, @NonNull List<String> list) {
    if (!activity.isPlaying() && activity.shouldCancelGame(room)) {
      Games.getRealTimeMultiplayerClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
          .leave(activity.getmJoinedRoomConfig(), room.getRoomId());
      activity.clearFlagKeepScreen();
    }
  }

  @Override
  public void onConnectedToRoom(@Nullable Room room) {
    activity.setRoom(room);
    Games.getPlayersClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
        .getCurrentPlayerId().addOnSuccessListener(new OnSuccessListener<String>() {
      @Override
      public void onSuccess(String playerId) {
        activity.setCurrentParticipantId(activity.getRoom().getParticipantId(playerId));
      }
    });
  }

  @Override
  public void onDisconnectedFromRoom(@Nullable Room room) {
    Games.getRealTimeMultiplayerClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
        .leave(activity.getmJoinedRoomConfig(), room.getRoomId());
    activity.clearFlagKeepScreen();
    activity.setRoom(null);
    activity.setmJoinedRoomConfig(null);
  }

  @Override
  public void onPeersConnected(@Nullable Room room, @NonNull List<String> list) {
    Log.d(TAG, "peer connected");
    if (activity.shouldStartGame(room)) {
      Log.d(TAG, "sending data");
      activity.sendToAllReliably(("S:" + 123123).getBytes());
      //activity.startGame();
      //activity.sendToAllReliably(("Your friend: " + activity.getCurrentParticipantId()).getBytes());
    }
  }

  @Override
  public void onPeersDisconnected(@Nullable Room room, @NonNull List<String> list) {
    if (activity.isPlaying()) {
      //TODO end game
    } else if (activity.shouldCancelGame(room)) {
      Games.getRealTimeMultiplayerClient(activity, GoogleSignIn.getLastSignedInAccount(activity))
          .leave(activity.getmJoinedRoomConfig(), room.getRoomId());
      activity.clearFlagKeepScreen();
    }
  }

  @Override
  public void onP2PConnected(@NonNull String s) {

  }

  @Override
  public void onP2PDisconnected(@NonNull String s) {

  }

}
