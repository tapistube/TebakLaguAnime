package glory.tebaklaguanime;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
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
    ImageView imgAchievement;

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
                int tesCoin = 100;
                int coinNow = mUser.getCoin();
                int tambah = coinNow+tesCoin;
                SQLiteDatabase db = mDB.getWritableDatabase();
                db.execSQL("update tb_user set coin='"+tambah+"' where nama='User'");

                refreshTblUser();
                txtCoin.setText(String.valueOf(mUser.getCoin()));
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


        String customFont = "font/LemonMilk.otf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), customFont);
        TextView txtA = (TextView) findViewById(R.id.txtA);
        txtA.setTypeface(typeface);

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
