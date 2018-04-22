package ntnu.codt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.WindowManager;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.math.Vector3;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import ntnu.codt.core.network.IServiceClient;
import ntnu.codt.core.network.ReceiveEndpoint;
import ntnu.codt.core.network.StartEndpoint;
import ntnu.codt.entities.Creeps;
import ntnu.codt.entities.Player;
import ntnu.codt.entities.Towers;


public class AndroidLauncher extends AndroidApplication implements IServiceClient {
  private final static String TAG = AndroidLauncher.class.getName();
  private static final int RC_SIGN_IN = 9000;
  private static final int RC_WAITING_ROOM = 100002;
  private static final int MIN_PLAYERS = 2;
  private GoogleSignInClient mGoogleSignInClient = null;
  private GoogleSignInAccount signInAccount = null;
  private RoomConfig mJoinedRoomConfig = null;
  private RoomStatusUpdateCallback mRoomStatusCallbackHandler;
  private RoomUpdateCallback mRoomUpdateCallback;
  private CoDTMessageReceivedListener mMessageReceivedHandler;
  private boolean mPlaying = false;
  private Room mRoom = null;
  public String mRoomId = null;
  private String currentParticipantId = null;
  private StartEndpoint startEndpoint;
  private Player player;
  private RealTimeMultiplayerClient mRealTimeMultiplaterClient = null;
  private RealTimeMultiplayerClient.ReliableMessageSentCallback handleMessageSentCallback;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		mRoomStatusCallbackHandler = new CoDTRoomStatusUpdateCallback(this);
		mRoomUpdateCallback = new CoDTRoomUpdateCallback(this);
		mMessageReceivedHandler = new CoDTMessageReceivedListener(this);
		handleMessageSentCallback = new CoDTReliableMessageSentCallback();

		mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);

    System.out.println("ANDROID LAUNCHER::" + "Signed In: " + isSignedIn());


    initialize(new CoDT(this), config);
  }

  public void sendToAllReliably(byte[] message) {
	  Log.d(TAG, String.valueOf(mRoom.getParticipantIds().size()));
	  Log.d(TAG, getCurrentParticipantId());
	  for (String pid : mRoom.getParticipantIds()) {
	    if (!pid.equals(getCurrentParticipantId())) {
	      Log.d(TAG, "Sending to: " + pid);
	      mRealTimeMultiplaterClient
            .sendReliableMessage(message, mRoom.getRoomId(), pid, handleMessageSentCallback)
            .addOnCompleteListener(new OnCompleteListener<Integer>() {
              @Override
              public void onComplete(@NonNull Task<Integer> task) {
                //recordMessageToken(task.getResult());
              }
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
              }
            })
            .addOnSuccessListener(new OnSuccessListener<Integer>() {
              @Override
              public void onSuccess(Integer integer) {
                Log.d(TAG, "Success: " + integer);
              }
            });
      }
    }
  }

  @Override
  public void onResume() {
	  super.onResume();
	  signInSilently();
  }

	private boolean isSignedIn() {
	  return GoogleSignIn.getLastSignedInAccount(this) != null;
  }

  private void signInSilently() {
    Log.d(TAG, "POTATATOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
	  //if (!isSignedIn()) {
	    Log.d(TAG, "Signing in");
      mGoogleSignInClient.silentSignIn().addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
        @Override
        public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
          if (task.isSuccessful()) {
            onConnect(task.getResult());
          } else {
            task.getException().printStackTrace();
            startSignInIntent();
          }
        }
      });
  }

  private void startSignInIntent() {
    GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
        GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
    Intent intent = signInClient.getSignInIntent();
    startActivityForResult(intent, RC_SIGN_IN);
  }

  private void onConnect(GoogleSignInAccount googleSignInAccount) {
    Log.d(TAG, "onConnect(): connected to Google APIs");
    if (signInAccount != googleSignInAccount) {
      signInAccount = googleSignInAccount;

      mRealTimeMultiplaterClient = Games.getRealTimeMultiplayerClient(this, googleSignInAccount);

      PlayersClient playersClient = Games.getPlayersClient(this, googleSignInAccount);
      playersClient.getCurrentPlayer()
          .addOnSuccessListener(new OnSuccessListener<com.google.android.gms.games.Player>() {
            @Override
            public void onSuccess(com.google.android.gms.games.Player player) {
              currentParticipantId = player.getPlayerId();

              //startEndpoint.setWaitingScreen();
            }
          });
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RC_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      if (result.isSuccess()) {
        onConnect(result.getSignInAccount());
        System.out.println("ANDROID LAUNCER::" + signInAccount.getDisplayName());
      } else {
        String message = result.getStatus().getStatusMessage();
        System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW::" + result.getStatus().getStatusCode());
      }
    } else if (requestCode == RC_WAITING_ROOM) {
      if (resultCode == Activity.RESULT_OK) {
        sendToAllReliably(("S:" + mRoom.getCreationTimestamp()).getBytes());
      }
    }
  }

  private void startQuickGame(long role) {
	  Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1, 1, role);
    RoomConfig roomConfig = RoomConfig.builder(mRoomUpdateCallback)
        .setOnMessageReceivedListener(mMessageReceivedHandler)
        .setRoomStatusUpdateCallback(mRoomStatusCallbackHandler)
        .setAutoMatchCriteria(autoMatchCriteria)
        .build();

    mJoinedRoomConfig = roomConfig;

    mRealTimeMultiplaterClient.create(roomConfig);
  }

  public void showWaitingRoom(Room room) {
    mRealTimeMultiplaterClient.getWaitingRoomIntent(room, 2)
        .addOnSuccessListener(new OnSuccessListener<Intent>() {
          @Override
          public void onSuccess(Intent intent) {
            startActivityForResult(intent, RC_WAITING_ROOM);
          }
        });
  }

  public boolean shouldStartGame(Room room) {
	  int connectedPlayers = 0;
	  for (Participant p : room.getParticipants()) {
	    if (p.isConnectedToRoom()) {
	      connectedPlayers++;
      }
    }
    return connectedPlayers >= MIN_PLAYERS;
  }

  public boolean shouldCancelGame(Room room) {
	  return false;
  }

  public boolean isPlaying() {
	  return mPlaying;
  }

  public String getCurrentParticipantId() {
	  return currentParticipantId;
  }

  public void setCurrentParticipantId(String id) {
	  currentParticipantId = id;
  }

  public RoomConfig getmJoinedRoomConfig() {
    return mJoinedRoomConfig;
  }

  public void setmJoinedRoomConfig(RoomConfig mJoinedRoomConfig) {
    this.mJoinedRoomConfig = mJoinedRoomConfig;
  }

  public Room getRoom() {
	  return mRoom;
  }

  public void setRoom(Room room) {
	  this.mRoom = room;
  }

  public void clearFlagKeepScreen() {
	  getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  }

  public void startGame() {
    startEndpoint.setGameScreen();
  }

  @Override
  public boolean disconnect() {
	  if (mRealTimeMultiplaterClient != null && mRoom != null && mRoomId != null) {
	    Log.d(TAG, "" + (mJoinedRoomConfig != null));
	    Log.d(TAG, "" + (mRoomId != null));
      mRealTimeMultiplaterClient
          .leave(mJoinedRoomConfig, mRoomId)
          .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
              mJoinedRoomConfig = null;
              mRoom = null;
              mRoomId = null;
            }
        });
    }
	  //mRealTimeMultiplaterClient = null;
    return true;
  }

  @Override
  public boolean joinGame() {
    startQuickGame(0x0);
    return true;
  }

  @Override
  public Player getPlayer() {
    return player;
  }

  @Override
  public boolean towerPlaced(Vector3 pos, Towers tower, Player player) {
    String data = "T:" + player.name() + ":" + tower.name() + ":" + pos.x + ":" + pos.y;
    sendToAllReliably(data.getBytes());
    return true;
  }

  @Override
  public boolean creepSent(Creeps creep, Player player) {
    String data = "C:" + player.name() + ":" + creep.name();
    sendToAllReliably(data.getBytes());
    return true;
  }

  @Override
  public void setReceiveEndpoint(ReceiveEndpoint endpoint) {
    mMessageReceivedHandler.setEndpoint(endpoint);
  }

  @Override
  public void setStartEndpoint(StartEndpoint endpoint) {
    startEndpoint = endpoint;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

}
