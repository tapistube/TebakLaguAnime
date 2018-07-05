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

import Kelas.DBAdapter;
import Kelas.User;

public class MainActivity extends AppCompatActivity {

    Button btnPlay,btnExit;
    Intent i;
    DialogInterface.OnClickListener listener;
    TextView txtCoin;
    public static MediaPlayer clickSound;
    ImageView imgAchievement,imgEvent,imgLeaderboard;

    DBAdapter mDB;
    public static User mUser;
    public static List<User> mlistUser;
    protected Cursor cursor;

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

        mDB = DBAdapter.getInstance(getApplicationContext());
        mlistUser = mDB.getDataUser();
        mUser = mlistUser.get(0);
        txtCoin.setText(String.valueOf(mUser.getCoin()));



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
