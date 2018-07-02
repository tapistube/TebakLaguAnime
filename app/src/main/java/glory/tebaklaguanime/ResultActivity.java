package glory.tebaklaguanime;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import Kelas.DBAdapter;
import Kelas.User;

public class ResultActivity extends AppCompatActivity {

    TextView txtSkor,txtCoin;
    Button btnKeHome,btnShare;
    Intent i;
    int skor,getCoin;
    DialogInterface.OnClickListener listener;

    DBAdapter mDB;
    public static User mUser;
    public static List<User> mlistUser;
    protected Cursor cursor;
    private int coinNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_result);

        i = getIntent();
        skor = i.getIntExtra("kirimSkor",skor);
        getCoin = i.getIntExtra("kirimKoin",getCoin);
        GameActivity.mp.stop();

        txtSkor = (TextView) findViewById(R.id.txtSkor);
        txtCoin = (TextView) findViewById(R.id.txtCoin);
        btnKeHome = (Button) findViewById(R.id.btnKeHome);
        btnShare = (Button) findViewById(R.id.btnShare);

        //ada penambahan coin
        mDB = DBAdapter.getInstance(getApplicationContext());
        mlistUser = mDB.getDataUser();
        mUser = mlistUser.get(0);
        coinNow = mUser.getCoin();

        int tambah = coinNow + getCoin;
        SQLiteDatabase db = mDB.getWritableDatabase();
        db.execSQL("update tb_user set coin='"+tambah+"' where nama='User'");


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
        String shareBodyText = "Suka Anime ? Yuk Main Tebak Lagu Anime, Donwload di PlayStore Sekarang ! ";
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
}
