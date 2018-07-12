package glory.tebaklaguanime;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Timer;
import java.util.TimerTask;

import Kelas.SharedVariable;
import glory.tebaklaguanime.BuildConfig;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Intent i;
    int delay =  3000;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private static final long time = 3;
    private CountDownTimer mCountDownTimer;
    private long mTimeRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(SplashActivity.this);
       ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        Toast.makeText(SplashActivity.this,"VErsion name : "+versionName,Toast.LENGTH_SHORT).show();

        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser!=null){
            // User already signed in

            // get the FCM token
            String token = FirebaseInstanceId.getInstance().getToken();
            // save the user info in the database to users/UID/
            // we'll use the UID as part of the path
           /* User user = new User(fbUser.getUid(), fbUser.getDisplayName(), token);
            database.child("users").child(user.uid).setValue(user);*/

            // go to feed activity
           // Intent intent = new Intent(this, BerandaActivity.class);
            //startActivity(intent);

            SharedVariable.isUserLogged = 1;
        }else {
            SharedVariable.isUserLogged = 0;
        }

        mCountDownTimer = new CountDownTimer(time * 1000, 50) {
            @Override
            public void onTick(long millisUnitFinished) {
                mTimeRemaining = ((millisUnitFinished / 1000) + 1);
                // textView.setText("seconds remaining: " + mTimeRemaining);
                //  Toast.makeText(getApplicationContext(),"Seconds remaining :"+mTimeRemaining,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                i = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
            }
        };
        mCountDownTimer.start();
    }
}
