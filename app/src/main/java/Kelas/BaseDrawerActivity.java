package Kelas;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import glory.tebaklaguanime.EarnCoinActivity;
import glory.tebaklaguanime.LeaderboardActivity;
import glory.tebaklaguanime.LevelActivity;
import glory.tebaklaguanime.LoginActivity;
import glory.tebaklaguanime.R;
import glory.tebaklaguanime.ResultActivity;
import glory.tebaklaguanime.UserProfileActivity;
import glory.tebaklaguanime.VoucherActivity;
import util.CircleTransformation;

/**
 * Created by Miroslaw Stanek on 15.07.15.
 */
public class BaseDrawerActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.vNavigation)
    NavigationView vNavigation;

    @BindDimen(R.dimen.global_menu_avatar_size)
    int avatarSize;
    @BindString(R.string.user_profile_photo)
    String profilePhoto;

    //Cannot be bound via Butterknife, hosting view is initialized later (see setupHeader() method)
    private ImageView ivMenuUserProfilePhoto;
    private TextView txtHargaCoin,txtNamaProfil,txtExp;
    DBAdapter mDB;
    public static User mUser;
    public static List<User> mlistUser;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentViewWithoutInject(R.layout.activity_drawer);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.flContentRoot);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
        bindViews();
        setupHeader();
        setupNavigation();
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (getToolbar() != null) {
            getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            });
        }
    }

    private void setupNavigation(){
        vNavigation.setNavigationItemSelectedListener(this);
    }




    private void setupHeader() {
        View headerView = vNavigation.getHeaderView(0);
        ivMenuUserProfilePhoto = (ImageView) headerView.findViewById(R.id.ivMenuUserProfilePhoto);
        txtHargaCoin = (TextView) headerView.findViewById(R.id.txtHargaCoin);
        txtNamaProfil = (TextView) headerView.findViewById(R.id.txtNamaProfil);
        txtExp = (TextView) headerView.findViewById(R.id.txtExp);
        headerView.findViewById(R.id.vGlobalMenuHeader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGlobalMenuHeaderClick(v);
            }
        });


        txtHargaCoin.setText(String.valueOf(SharedVariable.coin));
        txtExp.setText(String.valueOf(SharedVariable.exp));
        int expNow = SharedVariable.exp;
        int iconProfil =  SharedVariable.expToIcon(expNow);

        Picasso.with(this)
                .load(iconProfil)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(ivMenuUserProfilePhoto);

        txtNamaProfil.setText(SharedVariable.nama);
    }

    public void onGlobalMenuHeaderClick(final View v) {
        drawerLayout.closeDrawer(Gravity.LEFT);

      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] startingLocation = new int[2];
                v.getLocationOnScreen(startingLocation);
                startingLocation[0] += v.getWidth() / 2;
                Log.d("startLocation1:",":"+startingLocation[0]);
                Log.d("startLocation2:",":"+startingLocation[1]);
                UserProfileActivity.startUserProfileFromLocation(startingLocation, BaseDrawerActivity.this);
                overridePendingTransition(0, 0);

            }
        }, 200);*/

        String uId = fAuth.getCurrentUser().getUid();
        String nama = SharedVariable.nama;
        int exp = SharedVariable.exp;
        Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
        i.putExtra("uId",uId);
        i.putExtra("nama",nama);
        i.putExtra("exp",exp);
        startActivity(i);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_logout) {
            // Handle the camera action
            fAuth.signOut();
            SharedVariable.coin = 0;
            SharedVariable.exp = 0;
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }else if (id == R.id.menu_login){
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }else if (id == R.id.menu_top_player){
            Intent i = new Intent(getApplicationContext(), LeaderboardActivity.class);
            startActivity(i);
        }else if (id == R.id.menu_badge){
            String uId = fAuth.getCurrentUser().getUid();
            String nama = SharedVariable.nama;
            int exp = SharedVariable.exp;
            Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
            i.putExtra("uId",uId);
            i.putExtra("nama",nama);
            i.putExtra("exp",exp);
            startActivity(i);
        }else if (id == R.id.menu_voucher){
            Intent i = new Intent(getApplicationContext(), VoucherActivity.class);
            startActivity(i);
        }else if (id == R.id.menu_event){
            OpenFacebookPage();
        } else if (id == R.id.menu_coins){
            Intent i = new Intent(getApplicationContext(), EarnCoinActivity.class);
            startActivity(i);
        }

        drawerLayout.closeDrawer(Gravity.LEFT);
        return true;
    }

    @Override
    protected void onResume() {
        txtHargaCoin.setText(String.valueOf(SharedVariable.coin));
        txtExp.setText(String.valueOf(SharedVariable.exp));
        super.onResume();
    }

    protected void OpenFacebookPage() {

        // FacebookページのID
        String facebookPageID = "tebaklaguanime";

        // URL
        String facebookUrl = "https://www.facebook.com/" + facebookPageID;

        // URLスキーム
        String facebookUrlScheme = "fb://page/" + facebookPageID;

        try {
            // Facebookアプリのバージョンを取得
            int versionCode = getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;

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
