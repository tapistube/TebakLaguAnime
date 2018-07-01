package glory.tebaklaguanime;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    TextView txtSkor,txtCoin;
    Button btnKeHome,btnShare;
    Intent i;
    int skor,getCoin;
    DialogInterface.OnClickListener listener;

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
            }
        });
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
