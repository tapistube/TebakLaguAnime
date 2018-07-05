package glory.tebaklaguanime;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        OpenFacebookPage();
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
}
