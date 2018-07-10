package glory.tebaklaguanime;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Collections;
import java.util.List;

import Kelas.DBAdapter;
import Kelas.Quiz;
import Kelas.User;

public class WolfActivity extends AppCompatActivity {

    ProgressBar progressBar;
    private ObjectAnimator mAnimation;
    Button btnTime,btnNext,btnA,btnB,btnC,btnD;
    int duration = 150000;
    private long tambahDurasi = 15000;
    private int skor = 0;
    private int exp = 0;
    private int currentSoal = 0;
    private int cloneCurrentSoal = 0;
    private int itungNextSoal = 0;
    private int getCoin = 0;
    private int nyawa = 3;
    private String getAnswer,tempSoal;
    int coinNow,jmlBenar,totalSkor;
    Intent i;
    ImageView imgPlay,imgCorrect,imgWrong,imgHeart1,imgHeart2,imgHeart3;
    TextView txtCoin,txtSkor;
    public static String a;

    private int audio = R.raw.believe;
    DBAdapter mDB;

    private Quiz mquiz;
    private List<Quiz> mlistQuiz;
    public static MediaPlayer mp,mpKoreksi;
    DialogInterface.OnClickListener listener;

    public static User mUser;
    public static List<User> mlistUser;
    protected Cursor cursor;
    private Uri audioUri;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wolf);

        btnNext = (Button) findViewById(R.id.btnNext);
        btnTime = (Button) findViewById(R.id.btnTime);
        btnA = (Button)findViewById(R.id.btnJwbA);
        btnB = (Button) findViewById(R.id.btnJwbB);
        btnC = (Button) findViewById(R.id.btnJwbC);
        btnD = (Button) findViewById(R.id.btnJwbD);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        imgCorrect = (ImageView) findViewById(R.id.imgCorrect);
        imgWrong= (ImageView) findViewById(R.id.imgWrong);
        imgPlay = (ImageView) findViewById(R.id.imgPlay);
        imgHeart1 = (ImageView) findViewById(R.id.heart1);
        imgHeart2 = (ImageView) findViewById(R.id.heart2);
        imgHeart3 = (ImageView) findViewById(R.id.heart3);
        txtCoin = (TextView) findViewById(R.id.txtCoin);
        txtSkor = (TextView) findViewById(R.id.txtSkor);
        txtSkor.setText("Skor : 0");
        mp = new MediaPlayer();
        mpKoreksi = new MediaPlayer();

        mDB = DBAdapter.getInstance(getApplicationContext());
        mlistQuiz = mDB.getAllWolf();
        Collections.shuffle(mlistQuiz);


        mlistUser = mDB.getDataUser();
        mUser = mlistUser.get(0);
        int coinNow = mUser.getCoin();
        txtCoin.setText(String.valueOf(coinNow));

        mAnimation = ObjectAnimator.ofInt(progressBar,"progress",0,100);
        mAnimation.setDuration(duration);
        mAnimation.setInterpolator(new DecelerateInterpolator());
        mAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
             //   Toast.makeText(getApplicationContext(),"Animasi progres mulai",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
               // Toast.makeText(getApplicationContext(),"Animasi progres berhenti",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
              //  Toast.makeText(getApplicationContext(),"Animasi progres cancel",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
              //  Toast.makeText(getApplicationContext(),"Animasi progres diulang",Toast.LENGTH_SHORT).show();
            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (mp.isPlaying()){
                    mp.stop();
                    mp.start();
                }else {
                    mp.start();
                }*/
            }
        });
        btnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAnswer = "A";
                nextSoal();
            }
        });
        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAnswer = "B";
                nextSoal();
            }
        });
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAnswer = "C";
                nextSoal();
            }
        });
        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAnswer = "D";
                nextSoal();
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mlistUser = mDB.getDataUser();
                    mUser = mlistUser.get(0);
                    int coinNow2 = mUser.getCoin();

                    if (coinNow2>40){
                        coinNow2 = coinNow2 - 40;
                        SQLiteDatabase db = mDB.getWritableDatabase();
                        db.execSQL("update tb_user set coin='"+coinNow2+"' where nama='User'");
                        txtCoin.setText(String.valueOf(coinNow2));

                        long durasiNow =  mAnimation.getDuration();
                        durasiNow = durasiNow + tambahDurasi;
                        mAnimation.setDuration(durasiNow);
                        Toast.makeText(getApplicationContext(),"Waktu Ditambahkan",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"Coin Tidak Cukup !",Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Gagal tambah waktu",Toast.LENGTH_SHORT).show();
                    Log.e("gagal tambah durasi : ",e.toString());
                }
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistUser = mDB.getDataUser();
                mUser = mlistUser.get(0);
                int coinNow2 = mUser.getCoin();
                if (coinNow2>50) {

                    if (cloneCurrentSoal < 3) {
                        mp.stop();
                        setupSoal();
                        cloneCurrentSoal++;

                        coinNow2 = coinNow2 - 50;
                        SQLiteDatabase db = mDB.getWritableDatabase();
                        db.execSQL("update tb_user set coin='"+coinNow2+"' where nama='User'");
                        txtCoin.setText(String.valueOf(coinNow2));
                    } else {
                        Toast.makeText(getApplicationContext(), "Kesempatan Pass lagu habis", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Coin Tidak Cukup !",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mulaiQuiz();
        mAnimation.start();

        adView = (AdView) findViewById(R.id.spanduk);
        adView.loadAd(new AdRequest.Builder().build());

        adView.setAdListener(new AdListener(){
                                 @Override
                                 public void onAdClosed() {
                                     //Kode disini akan di eksekusi saat Iklan Ditutup
                                   //  Toast.makeText(getApplicationContext(), "Iklan Dititup", Toast.LENGTH_SHORT).show();
                                     muatUlangIklan();
                                     super.onAdClosed();
                                 }

                                 @Override
                                 public void onAdFailedToLoad(int i) {
                                     //Kode disini akan di eksekusi saat Iklan Gagal Dimuat
                                    // Toast.makeText(getApplicationContext(), "Iklan Gagal Dimuat", Toast.LENGTH_SHORT).show();
                                     muatUlangIklan();
                                     super.onAdFailedToLoad(i);
                                 }

                                 @Override
                                 public void onAdLeftApplication() {
                                     //Kode disini akan di eksekusi saat Pengguna Meniggalkan Aplikasi/Membuka Aplikasi Lain
                                   //  Toast.makeText(getApplicationContext(), "Iklan Ditinggalkan", Toast.LENGTH_SHORT).show();
                                     muatUlangIklan();
                                     super.onAdLeftApplication();
                                 }

                                 @Override
                                 public void onAdOpened() {
                                     //Kode disini akan di eksekusi saat Pengguna Mengklik Iklan
                                   //  Toast.makeText(getApplicationContext(), "Iklan Diklik", Toast.LENGTH_SHORT).show();
                                     muatUlangIklan();
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
    }

    @Override
    protected void onDestroy() {
        if (mAnimation.isRunning()){
            mAnimation.end();
        }
        super.onDestroy();
    }

    private void muatUlangIklan(){
        adView.loadAd(new AdRequest.Builder().build());
    }

    private void mulaiQuiz(){

        setupSoal();
    }

    public  void setupSoal(){

        mquiz  = mlistQuiz.get(currentSoal);
        imgWrong.setVisibility(View.INVISIBLE);
        imgCorrect.setVisibility(View.INVISIBLE);
        // mpKoreksi.stop();

        //  textSoal.setText(mquiz.getSoal());

        //setupAudio();
        setupAudioWolf();
        if (mp != null){
            if (mp.isPlaying()){
                mp.stop();
            }
            mp.reset();
            mp.release();
            mp = null;
        }
        mp = MediaPlayer.create(WolfActivity.this,audioUri);

        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mp.stop();
            }
        });

        btnA.setText(mquiz.getJawaban_a());
        btnB.setText(mquiz.getJawaban_b());
        btnC.setText(mquiz.getJawaban_c());
        btnD.setText(mquiz.getJawaban_d());


        currentSoal++;
    }

    public void setupAudio(){
        int c = mquiz.getId() - 1 ;

        switch (c) {

            case 0:
                audio = R.raw.thisgame;
                break;

            case 1:
                audio = R.raw.distance;
                break;

            case 2:
                audio = R.raw.kanashimi;
                break;

            case 3:
                audio = R.raw.sobakasu;
                break;

            case 4:
                audio = R.raw.sasageyo;
                break;

            case 5:
                audio = R.raw.kibouuta;
                break;

            case 6:
                audio = R.raw.butterfly;
                break;

            case 7:
                audio = R.raw.believe;
                break;

            case 8:
                audio = R.raw.weare;
                break;

            case 9:
                audio = R.raw.innocence;
                break;
        }
    }

    public void setupAudioWolf(){
        int c = mquiz.getId() - 1 ;
        String lokasiAudio = mquiz.getSoal();

        String dataResourceDirectory = "raw";
        String dataResoruceFilename = lokasiAudio;

        audioUri  =Uri.parse("android.resource://" + getPackageName() + "/" +
                dataResourceDirectory + "/" + dataResoruceFilename);

       // mp = MediaPlayer.create(WolfActivity.this,uri);
    }

    public void nextSoal(){
        itungNextSoal++;
        //mp.stop();
        if (mp != null){
            if (mp.isPlaying()){
                mp.stop();
            }
            mp.reset();
            mp.release();
            mp = null;
        }
        if (getAnswer.equals(mquiz.getJawaban_benar().toUpperCase())){
            skor = skor+7;
            getCoin = getCoin+6;
            exp = exp + 16;
            suaraJwbBenar();
        }else {
            nyawa = nyawa - 1;
            suaraJwbSalah();
        }
        txtSkor.setText("Skor : "+String.valueOf(skor));

        if (nyawa == 2){
            imgHeart3.setVisibility(View.INVISIBLE);
        }else if (nyawa == 1){
            imgHeart2.setVisibility(View.INVISIBLE);
        }else if (nyawa == 0){
            //mp.stop();
            if (mp != null){
                if (mp.isPlaying()){
                    mp.stop();
                }
                mp.reset();
                mp.release();
                mp = null;
            }
            mAnimation.end();
            imgHeart1.setVisibility(View.INVISIBLE);
            totalSkor = skor;
            Intent i = new Intent(this,ResultActivity.class);
            i.putExtra("kirimSkor",totalSkor);
            i.putExtra("kirimKoin",getCoin);
            i.putExtra("kirimExp",exp);
            int from = 1;
            i.putExtra("from",from);
            startActivity(i);
        }

        if (currentSoal<mlistQuiz.size()  && itungNextSoal < 10){
            setupSoal();
        }else {
           // mp.stop();
            if (mp != null){
                if (mp.isPlaying()){
                    mp.stop();
                }
                mp.reset();
                mp.release();
                mp = null;
            }
            mAnimation.end();
            totalSkor = skor;
            Intent i = new Intent(this,ResultActivity.class);
            i.putExtra("kirimSkor",totalSkor);
            i.putExtra("kirimKoin",getCoin);
            i.putExtra("kirimExp",exp);
            int from = 1;
            i.putExtra("from",from);
            startActivity(i);
        }
    }

    private void suaraJwbBenar(){
        imgCorrect.setVisibility(View.VISIBLE);
        mpKoreksi = MediaPlayer.create(WolfActivity.this,R.raw.sound_correct);
       /* try{
            mpKoreksi.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        mpKoreksi.start();
        mpKoreksi.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mpKoreksi.stop();
            }
        });
    }

    private void suaraJwbSalah(){
        imgWrong.setVisibility(View.VISIBLE);
        mpKoreksi = MediaPlayer.create(WolfActivity.this,R.raw.sound_wrong);
      /*  try{
            mpKoreksi.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        mpKoreksi.start();
        mpKoreksi.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mpKoreksi.stop();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakan anda ingin keluar sesi permainan ?");
        builder.setCancelable(false);

        listener = new DialogInterface.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE){
                    //finishAffinity();
                    //System.exit(0);
                    mp.stop();
                    mAnimation.end();
                    mpKoreksi.stop();
                    i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
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

    @Override
    protected void onPause() {
        if (mp != null){
            mp.stop();
            mp.release();
            mp = null;
        }
        super.onPause();
    }
}
