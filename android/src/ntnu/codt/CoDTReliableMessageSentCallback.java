package ntnu.codt;

import android.util.Log;
import com.google.android.gms.games.RealTimeMultiplayerClient;


public class CoDTReliableMessageSentCallback implements RealTimeMultiplayerClient.ReliableMessageSentCallback {
  private final static String TAG = CoDTReliableMessageSentCallback.class.getName();

  @Override
  public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientId) {
    Log.d(TAG, "RealTime message sent");
    Log.d(TAG, "  statusCode: " + statusCode);
    Log.d(TAG, "  tokenId: " + tokenId);
    Log.d(TAG, "  recipientParticipantId: " + recipientId);
  }

}
