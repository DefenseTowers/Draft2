package ntnu.codt;

import android.os.Bundle;

import android.support.annotation.NonNull;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import ntnu.codt.CoDT;

public class AndroidLauncher extends AndroidApplication {

  private GoogleSignInClient mGoogleSignInClient = null;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken("178551181383-05bj62rp3nd7p8idgfr1fga00vdgakbk.apps.googleusercontent.com")
        .build();


		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
		mGoogleSignInClient.silentSignIn().addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
      @Override
      public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
        if (!task.isSuccessful()) {
          task.getException().printStackTrace();
        }
        System.out.println("ANDROID LAUNCHER::" + "Signed In: " + isSignedIn());
      }
    });

    System.out.println("ANDROID LAUNCHER::" + "Signed In: " + isSignedIn());


    initialize(new CoDT(new AndroidService()), config);
  }

	private boolean isSignedIn() {
	  return GoogleSignIn.getLastSignedInAccount(this) != null;
  }


}
