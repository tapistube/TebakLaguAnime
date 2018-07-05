package glory.tebaklaguanime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import Adapter.RecycleAdapterLevel;

public class LevelActivity extends AppCompatActivity {

    RecyclerView recyclerLevel;
    RecycleAdapterLevel adapterLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_level);

        recyclerLevel = (RecyclerView) findViewById(R.id.recycler_listlevel);
        adapterLevel = new RecycleAdapterLevel(this);
        recyclerLevel.setAdapter(adapterLevel);
        recyclerLevel.setLayoutManager(new LinearLayoutManager(this));

        AdView adView = (AdView) findViewById(R.id.spanduk);
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
                                 //    Toast.makeText(getApplicationContext(), "Iklan Gagal Dimuat", Toast.LENGTH_SHORT).show();
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

    }
}
