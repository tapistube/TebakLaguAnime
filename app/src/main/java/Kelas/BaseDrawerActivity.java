package Kelas;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;
import glory.tebaklaguanime.LevelActivity;
import glory.tebaklaguanime.R;
import util.CircleTransformation;

/**
 * Created by Miroslaw Stanek on 15.07.15.
 */
public class BaseDrawerActivity extends BaseActivity {

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

    @Override
    public void setContentView(int layoutResID) {
        super.setContentViewWithoutInject(R.layout.activity_drawer);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.flContentRoot);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
        bindViews();
        setupHeader();


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

        mDB = DBAdapter.getInstance(getApplicationContext());
        mlistUser = mDB.getDataUser();
        mUser = mlistUser.get(0);
        txtHargaCoin.setText(String.valueOf(mUser.getCoin()));
        txtExp.setText(String.valueOf(mUser.getExp()));


        Picasso.with(this)
                .load(R.drawable.icon_utama)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(ivMenuUserProfilePhoto);
    }

    public void onGlobalMenuHeaderClick(final View v) {
        drawerLayout.closeDrawer(Gravity.LEFT);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] startingLocation = new int[2];
                v.getLocationOnScreen(startingLocation);
                startingLocation[0] += v.getWidth() / 2;
                //UserProfileActivity.startUserProfileFromLocation(startingLocation, BaseDrawerActivity.this);
                //overridePendingTransition(0, 0);
                Intent i = new Intent(getApplicationContext(), LevelActivity.class);
                startActivity(i);
            }
        }, 200);

    }

}
