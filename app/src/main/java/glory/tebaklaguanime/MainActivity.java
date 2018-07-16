package glory.tebaklaguanime;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Currency;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import Kelas.BaseDrawerActivity;
import Kelas.DBAdapter;
import Kelas.SharedVariable;
import Kelas.User;
import butterknife.BindView;

public class MainActivity extends BaseDrawerActivity {

    Button btnPlay,btnExit;
    Intent i;
    DialogInterface.OnClickListener listener;
    TextView txtCoin;
    public static MediaPlayer clickSound;
    ImageView imgAchievement,imgLeaderboard,imgRate;

    DBAdapter mDB;
    public static User mUser;
    public static List<User> mlistUser;
    protected Cursor cursor;

    private CountDownTimer mCountDownTimer;
    private RewardedVideoAd mRewardedVideoAd;
    private long mTimeRemaining;
    private boolean mGameOver;
    private boolean mGamePaused;
    private static final long COUNTER_TIME = 10;
    private static final int GAME_OVER_REWARD = 1;
    private int kesempatanFreeCoin,coinNow;
    private ScheduledExecutorService scheduleTaskExecutor;
    private String srcBadge;

    SharedPreferences preferences;
    public static final String KEYPREF = "Key Preferences";
    public static final String KEYKesempatanFreeCoin = "Key FreeCoin";

    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";

    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;




    private boolean pendingIntroAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnExit = (Button) findViewById(R.id.btnExit);
        clickSound = MediaPlayer.create(MainActivity.this,R.raw.pop);
        imgAchievement = (ImageView) findViewById(R.id.imgAchievement);
        imgLeaderboard = (ImageView) findViewById(R.id.imgLeaderboard);
        preferences = getSharedPreferences(KEYPREF, Context.MODE_PRIVATE);
        imgRate = (ImageView) findViewById(R.id.imgRate);

        mDB = DBAdapter.getInstance(getApplicationContext());
        mlistUser = mDB.getDataUser();
        mUser = mlistUser.get(0);

        kesempatanFreeCoin = mUser.getFree_coin();
        coinNow = SharedVariable.coin;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEYKesempatanFreeCoin,3);
        editor.apply();

        if (SharedVariable.isUserLogged == 0){
            showDialogKeLogin();
        }
       


        imgRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bunyiKlik();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=glory.tebaklaguanime"));
                startActivity(intent);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bunyiKlik();
               i = new Intent(getApplicationContext(),LevelActivity.class);
                startActivity(i);
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bunyiKlik();
                //finish();

                int getcoin =  100;
                coinNow = coinNow + getcoin;
                SharedVariable.coin = coinNow;
              //  txtCoin.setText(String.valueOf(coinNow));

            }
        });
        imgAchievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bunyiKlik();
                i = new Intent(getApplicationContext(),AchievementActivity.class);
                startActivity(i);
            }
        });
        imgLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bunyiKlik();
               // openLeaderboardPage();
                i = new Intent(MainActivity.this,LeaderboardActivity.class);
                startActivity(i);
            }
        });
        if (SharedVariable.list_badge_user.size() != 0){
            srcBadge = "";
            SharedVariable.list_src_badge_user.clear();
            for (int c=0;c<SharedVariable.list_badge_user.size();c++){
                srcBadge = setBadgeSrc(SharedVariable.list_badge_user.get(c).toString());
                SharedVariable.list_src_badge_user.add(srcBadge);
                Log.d("srcJadi[Splash] : ",srcBadge);
            }
        }

        //code buat ads Video
        MobileAds.initialize(this, getString(R.string.tesUnitIDVideo));
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getApplicationContext());

        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                //Toast.makeText(getBaseContext(), "Iklan dimuat", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdOpened() {
                //Toast.makeText(getBaseContext(), "Iklan dibuka.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
               // Toast.makeText(getBaseContext(), "Iklan dimulai.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                Toast.makeText(getBaseContext(), "Anda berhasil mendapatkan 40 coin.", Toast.LENGTH_SHORT).show();
                kesempatanFreeCoin = kesempatanFreeCoin - 1;

                SQLiteDatabase db = mDB.getWritableDatabase();
                /*db.execSQL("update tb_user set free_coin='"+kesempatanFreeCoin+"' where nama='User'");
                mlistUser = mDB.getDataUser();
                mUser = mlistUser.get(0);
                kesempatanFreeCoin = mUser.getFree_coin();*/

                coinNow = coinNow + 40;
                SQLiteDatabase db2 = mDB.getWritableDatabase();
                db.execSQL("update tb_user set coin='"+coinNow+"' where nama='User'");
                mlistUser = mDB.getDataUser();
                mUser = mlistUser.get(0);
                coinNow = mUser.getCoin();
             //   txtCoin.setText(String.valueOf(coinNow));

                Log.d("kesempatan Free main : ",String.valueOf(kesempatanFreeCoin));
                muatUlangIklan();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                //Toast.makeText(getBaseContext(), "Ad left application.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
               // Toast.makeText(getBaseContext(), "Ad failed to load.", Toast.LENGTH_SHORT).show();
                Log.e("Error video", "Code " + i);
            }
        });

        if (kesempatanFreeCoin>0){
           // startGame();
            gameOver();
        }

        for (int c=0;c<SharedVariable.list_badge_user.size();c++){
            Log.d("badgeUser :",SharedVariable.list_badge_user.get(c).toString());
        }


        scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        //Schedule a task to run every 5 seconds (or however long you want)
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                // Do stuff here!

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update DB Free_coin
                        SQLiteDatabase db = mDB.getWritableDatabase();
                        db.execSQL("update tb_user set free_coin='2' where nama='User'");
                        mlistUser = mDB.getDataUser();
                        mUser = mlistUser.get(0);
                        kesempatanFreeCoin = mUser.getFree_coin();

                        muatUlangIklan();
                    }
                });

            }
        }, 0, 2, TimeUnit.HOURS); // or .MINUTES, .HOURS etc.


    }

    private void muatUlangIklan(){
        if (kesempatanFreeCoin>0){
            startGame();
        }else {

        }
    }

    private void startGame() {
        // Hide the retry button and start the timer.


        createTimer(COUNTER_TIME);
        mGamePaused = false;
        mGameOver = false;

        mRewardedVideoAd.loadAd(getString(R.string.tesUnitIDVideo), new AdRequest.Builder().build());
    }

    private void createTimer(long time) {

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
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
                gameOver();
            }
        };
        mCountDownTimer.start();
    }

    private void gameOver() {

        mDB = DBAdapter.getInstance(getApplicationContext());
        mlistUser = mDB.getDataUser();
        mUser = mlistUser.get(0);
        //txtCoin.setText(String.valueOf(mUser.getCoin()));
        kesempatanFreeCoin = mUser.getFree_coin();

        if (kesempatanFreeCoin >0 ) {
            if (mRewardedVideoAd.isLoaded()) {

            }

            mGameOver = true;
        }else {

        }
    }

    private void showRewardedVideo(){

        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    protected void OpenFacebookPage() {

        // FacebookページのID
        String facebookPageID = "Tebak-Lagu-Anime-2161268944110667";

        // URL
        String facebookUrl = "https://www.facebook.com/" + facebookPageID;

        // URLスキーム
        String facebookUrlScheme = "fb://page/" + facebookPageID;

        try {
            // Facebookアプリのバージョンを取得
            int versionCode = getPackageManager().getPackageInfo("com.facebook", 0).versionCode;

            if (versionCode >= 3002850) {
                // Facebook アプリのバージョン 11.0.0.11.23 (3002850) 以上の場合
                Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else {
                // Facebook アプリが古い場合
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Facebookアプリがインストールされていない場合は、ブラウザで開く
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));

        }
    }

    protected void openLeaderboardPage(){
        // FacebookページのID
        String facebookPageID = "notes/tebak-lagu-anime/leaderboard-tebak-lagu-anime/2161745040729724";

        // URL
        String facebookUrl = "https://www.facebook.com/" + facebookPageID;

        // URLスキーム
        String facebookUrlScheme = "fb://page/" + facebookPageID;

        try {
            // Facebookアプリのバージョンを取得
            int versionCode = getPackageManager().getPackageInfo("com.facebook", 0).versionCode;

            if (versionCode >= 3002850) {
                // Facebook アプリのバージョン 11.0.0.11.23 (3002850) 以上の場合
                Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } else {
                // Facebook アプリが古い場合
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Facebookアプリがインストールされていない場合は、ブラウザで開く
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));

        }
    }

    public void refreshTblUser(){
        mlistUser = mDB.getDataUser();
        mUser = mlistUser.get(0);
    }

    public static void bunyiKlik(){
        clickSound.start();
    }

    @Override
    public void onBackPressed() {
        bunyiKlik();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakan anda ingin keluar dari aplikasi ?");
        builder.setCancelable(false);

        listener = new DialogInterface.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE){
                    finishAffinity();
                    System.exit(0);
                }

                if(which == DialogInterface.BUTTON_NEGATIVE){
                    dialog.cancel();
                }
            }
        };
        builder.setPositiveButton("Ya",listener);
        builder.setNegativeButton("Tidak", listener);
        builder.show();
    }

    private void showDialogKeLogin(){
        LayoutInflater minlfater = LayoutInflater.from(this);
        View v = minlfater.inflate(R.layout.custom_dialog_goto_login, null);
        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this).create();
        dialog.setView(v);

        final Button btnDialogKeLogin = (Button) v.findViewById(R.id.btnDialogKeLogin);
        btnDialogKeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });


        dialog.show();
    }

    private String setBadgeSrc(String s){
        String src = "";
        switch (s){
            case "b1":
                src =  SharedVariable.list_src_badge.get(0).toString();
                break;

            case "b2" :
                src =  SharedVariable.list_src_badge.get(1).toString();
                break;

            case "b3" :
                src =  SharedVariable.list_src_badge.get(2).toString();
                break;

            case "b4" :
                src =  SharedVariable.list_src_badge.get(3).toString();
                break;

            case "b5" :
                src =  SharedVariable.list_src_badge.get(4).toString();
                break;

            case "b6" :
                src =  SharedVariable.list_src_badge.get(5).toString();
                break;

        }
        return src;
    }
}
