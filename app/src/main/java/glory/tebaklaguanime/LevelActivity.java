package glory.tebaklaguanime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Adapter.RecycleAdapterLevel;
import Kelas.DBAdapter;
import Kelas.SharedVariable;
import Kelas.User;

public class LevelActivity extends AppCompatActivity {
    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

    RecyclerView recyclerLevel;
    RecycleAdapterLevel adapterLevel;
    private AdView adView;
    public static TextView txtCoin,txtFreeCoin;
    DBAdapter mDB;
    public static User mUser;
    public static List<User> mlistUser;
    ImageView imgGift;
    public static MediaPlayer clickSound;

    private CountDownTimer mCountDownTimer;
    private RewardedVideoAd mRewardedVideoAd;
    private long mTimeRemaining;
    private boolean mGameOver;
    private boolean mGamePaused;
    private static final long COUNTER_TIME = 10;
    private static final int GAME_OVER_REWARD = 1;
    private int kesempatanFreeCoin,coinNow;
    private ScheduledExecutorService scheduleTaskExecutor;
    public static boolean isInternetOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_level);

        txtCoin = (TextView) findViewById(R.id.txtHargaCoin);
        imgGift = (ImageView) findViewById(R.id.imgGift);
        txtFreeCoin = (TextView) findViewById(R.id.txtFreeCoin);
        clickSound = MediaPlayer.create(LevelActivity.this,R.raw.pop);


        txtCoin.setText(String.valueOf(SharedVariable.coin));
        coinNow = SharedVariable.coin;

        recyclerLevel = (RecyclerView) findViewById(R.id.recycler_listlevel);
        adapterLevel = new RecycleAdapterLevel(this);
        recyclerLevel.setAdapter(adapterLevel);
        recyclerLevel.setLayoutManager(new LinearLayoutManager(this));
        checkConnection();

        adView = (AdView) findViewById(R.id.spanduk);
        adView.loadAd(new AdRequest.Builder().build());

        adView.setAdListener(new AdListener(){
                                 @Override
                                 public void onAdClosed() {
                                     //Kode disini akan di eksekusi saat Iklan Ditutup
                                  //   Toast.makeText(getApplicationContext(), "Iklan Dititup", Toast.LENGTH_SHORT).show();
                                     muatUlangIklanBanner();
                                     super.onAdClosed();
                                 }

                                 @Override
                                 public void onAdFailedToLoad(int i) {
                                     //Kode disini akan di eksekusi saat Iklan Gagal Dimuat
                                 //    Toast.makeText(getApplicationContext(), "Iklan Gagal Dimuat", Toast.LENGTH_SHORT).show();
                                     muatUlangIklanBanner();
                                     super.onAdFailedToLoad(i);
                                 }

                                 @Override
                                 public void onAdLeftApplication() {
                                     //Kode disini akan di eksekusi saat Pengguna Meniggalkan Aplikasi/Membuka Aplikasi Lain
                                    // Toast.makeText(getApplicationContext(), "Iklan Ditinggalkan", Toast.LENGTH_SHORT).show();
                                     muatUlangIklanBanner();
                                     super.onAdLeftApplication();
                                 }

                                 @Override
                                 public void onAdOpened() {
                                     //Kode disini akan di eksekusi saat Pengguna Mengklik Iklan
                                    // Toast.makeText(getApplicationContext(), "Iklan Diklik", Toast.LENGTH_SHORT).show();
                                     muatUlangIklanBanner();
                                     super.onAdOpened();
                                 }

                                 @Override
                                 public void onAdLoaded() {
                                     //Kode disini akan di eksekusi saat Iklan Selesai Dimuat
                                    // Toast.makeText(getApplicationContext(), "Iklan Selesai Dimuat", Toast.LENGTH_SHORT).show();
                                     super.onAdLoaded();
                                 }

                             }
        );

        //video ads
        imgGift.setVisibility(View.INVISIBLE);
        txtFreeCoin.setVisibility(View.INVISIBLE);
        MobileAds.initialize(this, getString(R.string.tesUnitIDVideo));
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getApplicationContext());
        mRewardedVideoAd.loadAd(getString(R.string.tesUnitIDVideo), new AdRequest.Builder().build());

        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(getBaseContext(), "Iklan video dimuat", Toast.LENGTH_SHORT).show();
                //imgGift.setVisibility(View.VISIBLE);
               // txtFreeCoin.setVisibility(View.VISIBLE);
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
                db.execSQL("update tb_user set free_coin='"+kesempatanFreeCoin+"' where nama='User'");
                mlistUser = mDB.getDataUser();
                mUser = mlistUser.get(0);
                kesempatanFreeCoin = mUser.getFree_coin();

                coinNow = coinNow + 40;
                SQLiteDatabase db2 = mDB.getWritableDatabase();
                db.execSQL("update tb_user set coin='"+coinNow+"' where nama='User'");
                mlistUser = mDB.getDataUser();
                mUser = mlistUser.get(0);
                coinNow = mUser.getCoin();
                txtCoin.setText(String.valueOf(coinNow));

                Log.d("kesempatan Fre leve  : ",String.valueOf(kesempatanFreeCoin));
                muatUlangIklan();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                //Toast.makeText(getBaseContext(), "Ad left application.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                // Toast.makeText(getBaseContext(), "Ad failed to load.", Toast.LENGTH_SHORT).show();
               Log.e("eror video","Code : "+i);
            }
        });


        if (kesempatanFreeCoin>0){
           // startGame();
         //   gameOver();
        }
        imgGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bunyiKlik();
              //  showRewardedVideo();
            }
        });

    /*    scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

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
        }, 0, 7, TimeUnit.HOURS); // or .MINUTES, .HOURS etc.
        */

    }

    private void muatUlangIklan(){
        if (kesempatanFreeCoin>0){
           // startGame();
        }else {

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

        mDB = DBAdapter.getInstance(getApplicationContext());
        mlistUser = mDB.getDataUser();
        mUser = mlistUser.get(0);
        txtCoin.setText(String.valueOf(mUser.getCoin()));
        kesempatanFreeCoin = mUser.getFree_coin();

        if (kesempatanFreeCoin > 0) {
            if (mRewardedVideoAd.isLoaded()) {
                imgGift.setVisibility(View.VISIBLE);
                txtFreeCoin.setVisibility(View.VISIBLE);
            }

            mGameOver = true;
        }
    }

    private void showRewardedVideo(){
        imgGift.setVisibility(View.INVISIBLE);
        txtFreeCoin.setVisibility(View.INVISIBLE);
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }

    }

    private void muatUlangIklanBanner(){
        adView.loadAd(new AdRequest.Builder().build());
    }

    public static void bunyiKlik(){
        clickSound.start();
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {

            return false;
        }
    }

    public void checkConnection(){

        if(isOnline()){
            Toast.makeText(LevelActivity.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
            isInternetOn = true;
        }else{
            Toast.makeText(LevelActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
            isInternetOn = false;
        }
    }




}
