package ntnu.codt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.math.Vector3;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import ntnu.codt.CoDT;
import ntnu.codt.core.network.IServiceClient;
import ntnu.codt.core.network.ReceiveEndpoint;
import ntnu.codt.core.network.StartEndpoint;
import ntnu.codt.entities.Creeps;
import ntnu.codt.entities.Player;
import ntnu.codt.entities.Towers;

import java.util.HashSet;
import java.util.List;

public class AndroidLauncher extends AndroidApplication implements IServiceClient {
  private final static String TAG = AndroidLauncher.class.getName();
  private final static int MIN_PLAYERS = 2;
  private GoogleSignInClient mGoogleSignInClient = null;
  private GoogleSignInAccount signInAccount = null;
  private RoomConfig mJoinedRoomConfig = null;
  private RoomStatusUpdateCallback mRoomStatusCallbackHandler;
  private RoomUpdateCallback mRoomUpdateCallback;
  private CoDTMessageReceivedListener mMessageReceivedHandler;
  private boolean mPlaying = false;
  private Room mRoom = null;
  private String currentParticipantId = null;
  private StartEndpoint startEndpoint;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		mRoomStatusCallbackHandler = new CoDTRoomStatusUpdateCallback(this);
		mRoomUpdateCallback = new CoDTRoomUpdateCallback(this);
		mMessageReceivedHandler = new CoDTMessageReceivedListener();

    System.out.println("ANDROID LAUNCHER::" + "Signed In: " + isSignedIn());


    initialize(new CoDT(this), config);
  }

  public void sendToAllReliably(byte[] message) {
	  for (String pid : mRoom.getParticipantIds()) {
	    if (!pid.equals(currentParticipantId)) {
	      Task<Integer> task = Games.getRealTimeMultiplayerClient(this, GoogleSignIn.getLastSignedInAccount(this))
            .sendReliableMessage(message, mRoom.getRoomId(), pid, handleMessageSentCallback)
            .addOnCompleteListener(new OnCompleteListener<Integer>() {
              @Override
              public void onComplete(@NonNull Task<Integer> task) {
                recordMessageToken(task.getResult());
              }
            });
      }
    }
  }

  private HashSet<Integer> pendingMessageSet = new HashSet<>();
  private RealTimeMultiplayerClient.ReliableMessageSentCallback handleMessageSentCallback = new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
    @Override
    public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientId) {
      synchronized (this) {
        pendingMessageSet.remove(tokenId);
      }
    }
  };

  synchronized void recordMessageToken(int tokenId) {
    pendingMessageSet.add(tokenId);
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
	  if (!isSignedIn()) {
	    Log.d(TAG, "Signing in");
      GoogleSignInClient signInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
      signInClient.silentSignIn().addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
        @Override
        public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
          if (task.isSuccessful()) {
            signInAccount = task.getResult();
            //startQuickGame(0x0);
          } else {
            task.getException().printStackTrace();
            startSignInIntent();
          }
        }
      });
    } else {
	    signInAccount = GoogleSignIn.getLastSignedInAccount(this);
    }
  }

  private void startSignInIntent() {
    GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
        GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
    Intent intent = signInClient.getSignInIntent();
    startActivityForResult(intent, 9000);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 9000) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      if (result.isSuccess()) {
        signInAccount = result.getSignInAccount();
        System.out.println("ANDROID LAUNCER::" + signInAccount.getDisplayName());
      } else {
        String message = result.getStatus().getStatusMessage();
        System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW::" + result.getStatus().getStatusCode());
        if (message == null || message.isEmpty()) {
          //message = getString(R.string.signin_other_error);
        }
        //new AlertDialog.Builder(this).setMessage(message)
        //    .setNeutralButton(android.R.string.ok, null).show();
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

    //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    mJoinedRoomConfig = roomConfig;


    Games.getRealTimeMultiplayerClient(this, GoogleSignIn.getLastSignedInAccount(this)).create(roomConfig);
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
  public boolean joinGame() {
    startQuickGame(0x0);
    return true;
  }

  @Override
  public Player getPlayer() {
    return mRoom.getCreatorId().equals(currentParticipantId) ? Player.P1 : Player.P2;
  }

  @Override
  public boolean towerPlaced(Vector3 pos, Towers tower, Player player) {
    String data = player.name() + ":" + "T:" + tower.name() + ":" + pos.x + ":" + pos.y;
    sendToAllReliably(data.getBytes());
    return true;
  }

  @Override
  public boolean creepSent(Creeps creep, Player player) {
    String data = player.name() + ":C:" + creep.name();
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


}
