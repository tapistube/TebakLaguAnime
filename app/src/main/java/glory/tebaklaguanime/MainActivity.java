package glory.tebaklaguanime;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Currency;
import java.util.List;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import Kelas.DBAdapter;
import Kelas.User;

public class MainActivity extends AppCompatActivity {

    Button btnPlay,btnExit;
    Intent i;
    DialogInterface.OnClickListener listener;
    TextView txtCoin,txtFreeCoin;
    public static MediaPlayer clickSound;
    ImageView imgAchievement,imgEvent,imgLeaderboard,imgGift;

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
    private int kesempatanFreeCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnExit = (Button) findViewById(R.id.btnExit);
        txtCoin = (TextView) findViewById(R.id.txtHargaCoin);
        clickSound = MediaPlayer.create(MainActivity.this,R.raw.pop);
        imgAchievement = (ImageView) findViewById(R.id.imgAchievement);
        imgEvent = (ImageView) findViewById(R.id.heart1);
        imgLeaderboard = (ImageView) findViewById(R.id.imgLeaderboard);
        imgGift = (ImageView) findViewById(R.id.imgGift);
        txtFreeCoin = (TextView) findViewById(R.id.txtFreeCoin);

        mDB = DBAdapter.getInstance(getApplicationContext());
        mlistUser = mDB.getDataUser();
        mUser = mlistUser.get(0);
        txtCoin.setText(String.valueOf(mUser.getCoin()));
        kesempatanFreeCoin = mUser.getFree_coin();



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
                finish();
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
        imgEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bunyiKlik();
                OpenFacebookPage();
            }
        });
        imgLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bunyiKlik();
                openLeaderboardPage();
            }
        });



        String customFont = "font/LemonMilk.otf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), customFont);
        TextView txtA = (TextView) findViewById(R.id.txtA);
        txtA.setTypeface(typeface);

        //code buat ads Video
        imgGift.setVisibility(View.INVISIBLE);
        txtFreeCoin.setVisibility(View.INVISIBLE);
        MobileAds.initialize(this, getString(R.string.myUnitID));
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getApplicationContext());

        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(getBaseContext(), "Iklan dimuat", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Toast.makeText(getBaseContext(), "Iklan dibuka.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
                Toast.makeText(getBaseContext(), "Iklan dimulai.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Toast.makeText(getBaseContext(), "Iklan ditutup", Toast.LENGTH_SHORT).show();
                muatUlangIklan();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                Toast.makeText(getBaseContext(), "Anda berhasil mendapatkan coin.", Toast.LENGTH_SHORT).show();
                kesempatanFreeCoin = kesempatanFreeCoin - 1;
                SQLiteDatabase db = mDB.getWritableDatabase();
                db.execSQL("update tb_user set free_coin='"+kesempatanFreeCoin+"' where nama='User'");
                mlistUser = mDB.getDataUser();
                mUser = mlistUser.get(0);
                kesempatanFreeCoin = mUser.getFree_coin();

                muatUlangIklan();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Toast.makeText(getBaseContext(), "Ad left application.", Toast.LENGTH_SHORT).show();
                muatUlangIklan();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Toast.makeText(getBaseContext(), "Ad failed to load.", Toast.LENGTH_SHORT).show();
                muatUlangIklan();
            }
        });

        if (kesempatanFreeCoin>0){
            startGame();
        }
        imgGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bunyiKlik();
                showRewardedVideo();
            }
        });


    }

    private void muatUlangIklan(){
        if (kesempatanFreeCoin>0){
            startGame();
        }
    }

    private void startGame() {
        // Hide the retry button and start the timer.

        imgGift.setVisibility(View.INVISIBLE);
        txtFreeCoin.setVisibility(View.INVISIBLE);
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

        if (mRewardedVideoAd.isLoaded()) {
            imgGift.setVisibility(View.VISIBLE);
            txtFreeCoin.setVisibility(View.VISIBLE);
        }

        mGameOver = true;
    }

    private void showRewardedVideo(){
        imgGift.setVisibility(View.INVISIBLE);
        txtFreeCoin.setVisibility(View.INVISIBLE);
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
}
