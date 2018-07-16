package Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Kelas.SharedVariable;
import butterknife.BindView;
import butterknife.ButterKnife;
import glory.tebaklaguanime.MainActivity;
import glory.tebaklaguanime.R;
import Kelas.Utils;
import glory.tebaklaguanime.SplashActivity;
import glory.tebaklaguanime.UserProfileActivity;

/**
 * Created by Miroslaw Stanek on 20.01.15.
 */
public class UserProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int PHOTO_ANIMATION_DELAY = 600;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();

    private final Context context;
    private final int cellSize;

    private List<String> photos = new ArrayList<>();

    private boolean lockedAnimations = false;
    private int lastAnimatedItem = -1;
    public static List<String> list_badge = new ArrayList();
    public static List<String> list_badge_user = new ArrayList();


    String badge[] = new String[]{};
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private String keyBadge,srcBadge,keyDIFb;
    private int c = 0;
    private static final long time = 3;
    private CountDownTimer mCountDownTimer;
    private long mTimeRemaining;

    public UserProfileAdapter(final Context context) {
        this.context = context;
        this.cellSize = Utils.getScreenWidth(context) / 3;
        Firebase.setAndroidContext(this.context);
        FirebaseApp.initializeApp(context.getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();


       ref.child("users").child(UserProfileActivity.uId).child("badges").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfileActivity.progressBar.setVisibility(View.VISIBLE);
                keyBadge = "";
                list_badge_user.clear();
                list_badge.clear();
               // list_src_badge.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    keyBadge = child.getKey();
                    if (!keyBadge.equals("") && keyBadge.length() != 0) {

                        list_badge_user.add(keyBadge);
                        Log.d("keyBadgeuser : ",keyBadge);

                    }else {
                        Toast.makeText(context.getApplicationContext(),"badge kosong",Toast.LENGTH_SHORT).show();
                    }
                }
                if (list_badge_user.size() != 0){
                    srcBadge = "";
                    list_badge.clear();
                    for (int c=0;c<list_badge_user.size();c++){
                        srcBadge = setBadgeSrc(list_badge_user.get(c).toString());
                        list_badge.add(srcBadge);
                        Log.d("srcJadi : ",srcBadge);
                    }
                }
                UserProfileActivity.progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getDataBadgeUser(){
        ref.child("users").child(UserProfileActivity.uId).child("badges").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfileActivity.progressBar.setVisibility(View.VISIBLE);
                keyBadge = "";
                list_badge_user.clear();
                list_badge.clear();
                // list_src_badge.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    keyBadge = child.getKey();
                    if (!keyBadge.equals("") && keyBadge.length() != 0) {

                        list_badge_user.add(keyBadge);
                        Log.d("keyBadgeuser : ",keyBadge);

                    }else {
                        Toast.makeText(context.getApplicationContext(),"badge kosong",Toast.LENGTH_SHORT).show();
                    }
                }
                if (list_badge_user.size() != 0){
                    srcBadge = "";
                    list_badge.clear();
                    for (int c=0;c<list_badge_user.size();c++){
                        srcBadge = setBadgeSrc(list_badge_user.get(c).toString());
                        list_badge.add(srcBadge);
                        Log.d("srcJadi : ",srcBadge);
                    }
                }
                UserProfileActivity.progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String setBadgeSrc(String s){
        String src = "";
        switch (s){
           case "b1":
               src =  SharedVariable.list_src_badge.get(0).toString();
               break;

           case "b2" :
               src =  SharedVariable.list_src_badge.get(1).toString();
               break;

            case "b3" :
                src =  SharedVariable.list_src_badge.get(2).toString();
                break;

            case "b4" :
                src =  SharedVariable.list_src_badge.get(3).toString();
                break;

            case "b5" :
                src =  SharedVariable.list_src_badge.get(4).toString();
                break;

            case "b6" :
                src =  SharedVariable.list_src_badge.get(5).toString();
                break;

       }
        return src;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        layoutParams.height = cellSize;
        layoutParams.width = cellSize;
        layoutParams.setFullSpan(false);
        view.setLayoutParams(layoutParams);


        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder,  int position) {
        bindPhoto((PhotoViewHolder) holder, position);

    }

    private void bindPhoto(final PhotoViewHolder holder, int position) {

        Picasso.with(context)
                .load(list_badge.get(position).toString())
                .resize(cellSize, cellSize)
                .centerCrop()
                .into(holder.ivPhoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        animatePhoto(holder);

                    }

                    @Override
                    public void onError() {

                    }
                });
        if (lastAnimatedItem < position) lastAnimatedItem = position;

    }

    private void animatePhoto(PhotoViewHolder viewHolder) {
        if (!lockedAnimations) {
            if (lastAnimatedItem == viewHolder.getPosition()) {
                setLockedAnimations(true);
            }

            long animationDelay = PHOTO_ANIMATION_DELAY + viewHolder.getPosition() * 30;

            viewHolder.flRoot.setScaleY(0);
            viewHolder.flRoot.setScaleX(0);

            viewHolder.flRoot.animate()
                    .scaleY(1)
                    .scaleX(1)
                    .setDuration(200)
                    .setInterpolator(INTERPOLATOR)
                    .setStartDelay(animationDelay)
                    .start();
        }
    }

    @Override
    public int getItemCount() {

        return list_badge == null ? 0 : list_badge.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.flRoot)
        FrameLayout flRoot;
        @BindView(R.id.ivPhoto)
        ImageView ivPhoto;

        public PhotoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setLockedAnimations(boolean lockedAnimations) {
        this.lockedAnimations = lockedAnimations;
    }

}
