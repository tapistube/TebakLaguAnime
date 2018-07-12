package glory.tebaklaguanime;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.util.List;

import Kelas.DBAdapter;
import Kelas.SharedVariable;
import Kelas.User;

public class ResultActivity extends AppCompatActivity {

    TextView txtSkor,txtCoin;
    Button btnKeHome,btnShare;
    Intent i;
    private int skor,getCoin,exp;
    DialogInterface.OnClickListener listener;

    DBAdapter mDB;
    public static User mUser;
    public static List<User> mlistUser;
    protected Cursor cursor;
    private int coinNow,expNow;
    private int from;
    private InterstitialAd interstitialAd;
    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_result);

        i = getIntent();
        skor = i.getIntExtra("kirimSkor",skor);
        getCoin = i.getIntExtra("kirimKoin",getCoin);
        exp = i.getIntExtra("kirimExp",exp);
        from = i.getIntExtra("from",from);
        SharedVariable.interChance++;

       /* int  audio2 = R.raw.pop;
        GameActivity.mp = MediaPlayer.create(getApplicationContext(),audio2);
        GameActivity.mp.start();
        if (from == 0){

            if (GameActivity.mp.isPlaying()){
                GameActivity.mp.stop();
            }

        }else  if (from == 1){

            if (WolfActivity.mp.isPlaying()) {
                WolfActivity.mp.stop();
                GameActivity.mp.stop();
            }
        }*/


        txtSkor = (TextView) findViewById(R.id.txtSkor);
        txtCoin = (TextView) findViewById(R.id.txtCoin);
        btnKeHome = (Button) findViewById(R.id.btnKeHome);
        btnShare = (Button) findViewById(R.id.btnShare);

        //ada penambahan coin
        mDB = DBAdapter.getInstance(getApplicationContext());
        mlistUser = mDB.getDataUser();
        mUser = mlistUser.get(0);
        coinNow = mUser.getCoin();
        expNow = mUser.getExp();

        int tambah = coinNow + getCoin;
        int tambahExp = expNow + exp;
        SQLiteDatabase db = mDB.getWritableDatabase();
        db.execSQL("update tb_user set coin='"+tambah+"' where nama='User'");
        db.execSQL("update tb_user set exp='"+tambahExp+"' where nama='User'");


        txtSkor.setText(String.valueOf(skor));
        txtCoin.setText(String.valueOf(getCoin));

        btnKeHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.bunyiKlik();
                i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.bunyiKlik();
                //shareWA();
                shareBiasa();
            }
        });

        showDialogShare();

        adView = (AdView) findViewById(R.id.spanduk);
        adView.loadAd(new AdRequest.Builder().build());

        adView.setAdListener(new AdListener(){
                                 @Override
                                 public void onAdClosed() {
                                     //Kode disini akan di eksekusi saat Iklan Ditutup
                                  //   Toast.makeText(getApplicationContext(), "Iklan Dititup", Toast.LENGTH_SHORT).show();
                                     super.onAdClosed();
                                 }

                                 @Override
                                 public void onAdFailedToLoad(int i) {
                                     //Kode disini akan di eksekusi saat Iklan Gagal Dimuat
                                  //   Toast.makeText(getApplicationContext(), "Iklan Gagal Dimuat", Toast.LENGTH_SHORT).show();
                                     super.onAdFailedToLoad(i);

                                 }

                                 @Override
                                 public void onAdLeftApplication() {
                                     //Kode disini akan di eksekusi saat Pengguna Meniggalkan Aplikasi/Membuka Aplikasi Lain
                                    // Toast.makeText(getApplicationContext(), "Iklan Ditinggalkan", Toast.LENGTH_SHORT).show();
                                     super.onAdLeftApplication();
                                 }

                                 @Override
                                 public void onAdOpened() {
                                     //Kode disini akan di eksekusi saat Pengguna Mengklik Iklan
                                    // Toast.makeText(getApplicationContext(), "Iklan Diklik", Toast.LENGTH_SHORT).show();
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



        final InterstitialAd mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.tesUnitIDInter));//harus make ID unit yang asli baru mau  jir
        AdRequest adRequestInter = new AdRequest.Builder().build();

        mInterstitialAd.loadAd(adRequestInter);

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                //Kode disini akan di eksekusi saat Iklan Ditutup
               // Toast.makeText(getApplicationContext(), "Interstitial Dititup", Toast.LENGTH_SHORT).show();
                //Setelah ditutup, Iklan akan memuat ulang kembali

            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.e("Error inter", "Code " + i);
                //Kode disini akan di eksekusi saat Iklan Gagal Dimuat
               // Toast.makeText(getApplicationContext(), "Interstitial Gagal Dimuat", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                //Kode disini akan di eksekusi saat Pengguna Meniggalkan Aplikasi/Membuka Aplikasi Lain
              //  Toast.makeText(getApplicationContext(), "Interstitial Ditinggalkan", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                //Kode disini akan di eksekusi saat Pengguna Mengklik Iklan
              //  Toast.makeText(getApplicationContext(), "Interstitial Diklik", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded() {
                if (SharedVariable.interChance % 3 == 0){
                    mInterstitialAd.show();
                }

                super.onAdLoaded();
                //Kode disini akan di eksekusi saat Iklan Selesai Dimuat
               // Toast.makeText(getApplicationContext(), "Interstitial Selesai Dimuat", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void shareWA(){

        String picture_text = "Ayo main Game Tebak Lagu Anime bersamaku! , Download di ...";
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.share_image2);

        Uri imageUri = Uri.parse("android.resource://glory.tebaklaguanime/drawable/"+R.drawable.share_image2);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //Target whatsapp:
        shareIntent.setPackage("com.whatsapp");
        shareIntent.putExtra(Intent.EXTRA_TITLE,"Ayo main tebak lagu anime");
        shareIntent.putExtra(Intent.EXTRA_TEXT, picture_text);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
           // ToastHelper.MakeShortText("Whatsapp have not been installed.");
            Toast.makeText(getApplicationContext(),"Whatsapp tidak terinstal",Toast.LENGTH_SHORT).show();
        }
    }

    private void shareBiasa(){
        String shareBodyText = "Suka Anime ? Yuk Main Tebak Lagu Anime, Download di PlayStore Sekarang ! " +
                "https://play.google.com/store/apps/details?id=glory.tebaklaguanime&hl=en_US&pageId=none";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ayo mainkan Tebak lagu Anime!");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);

        try{
            startActivity(Intent.createChooser(sharingIntent,  getResources().getString(R.string.app_name)));

            mlistUser = mDB.getDataUser();
            mUser = mlistUser.get(0);
            coinNow = mUser.getCoin();
            int tambah = coinNow + 5;
            SQLiteDatabase db = mDB.getWritableDatabase();
            db.execSQL("update tb_user set coin='"+tambah+"' where nama='User'");

        }catch (ActivityNotFoundException ex){
            Toast.makeText(getApplicationContext(),"Gagal Share",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
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

    private void showDialogShare(){
        LayoutInflater minlfater = LayoutInflater.from(this);
        View v = minlfater.inflate(R.layout.custom_dialog_share, null);
        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this).create();
        dialog.setView(v);

        final Button btnDialogShare = (Button) v.findViewById(R.id.btnDialogShare);
        final Button btnDialogNo = (Button) v.findViewById(R.id.btnDialogNo);
        final ImageView headerImage = (ImageView) v.findViewById(R.id.header_cover_image);
        if (skor < 30 ){
            headerImage.setImageResource(R.drawable.reaksi4);
        }else if (skor < 60){
            headerImage.setImageResource(R.drawable.reaksi3);
        }else if (skor < 80){
            headerImage.setImageResource(R.drawable.reaksi2);
        }else {
            headerImage.setImageResource(R.drawable.reaksi1);
        }


        btnDialogShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.bunyiKlik();
                shareBiasa();
                dialog.dismiss();
            }
        });
        btnDialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
