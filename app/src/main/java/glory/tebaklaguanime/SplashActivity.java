package glory.tebaklaguanime;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Kelas.DBAdapter;
import Kelas.SharedVariable;
import Kelas.User;
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
    private int coin,exp;
    private int expInDB;
    DBAdapter mDB;
    public static User mUser;
    public static List<User> mlistUser;
   private String wolf,tiger,shark,unicorn;
    int serverVersionCode = 0;
    DialogInterface.OnClickListener listener;

    String kodeVocer;

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
        int versionCode = BuildConfig.VERSION_CODE;


      /*  if (versionCode != serverVersionCode ){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Aplikasi anda belum di update. Please update your application");
            builder.setCancelable(false);

            listener = new DialogInterface.OnClickListener()
            {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == DialogInterface.BUTTON_POSITIVE){
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=glory.tebaklaguanime"));
                        startActivity(intent);
                    }

                    if(which == DialogInterface.BUTTON_NEGATIVE){
                        finishAffinity();
                        System.exit(0);
                    }
                }
            };
            builder.setPositiveButton("Ya",listener);
            builder.setNegativeButton("Tidak", listener);
            builder.show();
        }else {
        */

            FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
            if (fbUser != null) {
                // User already signed in
                // get the FCM token
                String token = FirebaseInstanceId.getInstance().getToken();

                SharedVariable.isUserLogged = 1;
                SharedVariable.nama = fAuth.getCurrentUser().getDisplayName();
                mCountDownTimer = new CountDownTimer(time * 1000, 50) {
                    @Override
                    public void onTick(long millisUnitFinished) {
                        mTimeRemaining = ((millisUnitFinished / 1000) + 1);

                    }

                    @Override
                    public void onFinish() {
                        i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                };
                mCountDownTimer.start();


                ref.child("users").child(fbUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        coin = (int) dataSnapshot.child("coin").getValue(Integer.class);
                        exp = (int) dataSnapshot.child("exp").getValue(Integer.class);
                        wolf = dataSnapshot.child("level").child("wolf").getValue(String.class);
                        tiger = dataSnapshot.child("level").child("tiger").getValue(String.class);
                        shark = dataSnapshot.child("level").child("shark").getValue(String.class);
                        unicorn = dataSnapshot.child("level").child("unicorn").getValue(String.class);

                        SharedVariable.coin = coin;
                        SharedVariable.exp = exp;
                        SharedVariable.wolf = wolf;
                        SharedVariable.tiger = tiger;
                        SharedVariable.shark = shark;
                        SharedVariable.unicorn = unicorn;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            } else {
                SharedVariable.isUserLogged = 0;
                progressBar.setVisibility(View.GONE);
                i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
            }
        }

  //  }

    private void getVersionCode(){
        progressBar.setVisibility(View.VISIBLE);
        ref.child("userReadable").child("versiApp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                serverVersionCode = dataSnapshot.child("versiCode").getValue(Integer.class);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
