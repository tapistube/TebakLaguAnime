package Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import Kelas.Album;
import Kelas.SharedVariable;
import glory.tebaklaguanime.EarnCoinActivity;
import glory.tebaklaguanime.R;
import glory.tebaklaguanime.ResultActivity;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Album> albumList;
    private RewardedVideoAd mRewardedVideoAd;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public AlbumsAdapter(final Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
        Firebase.setAndroidContext(this.mContext);
        FirebaseApp.initializeApp(mContext.getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        refUser = ref.child("users");
        fAuth = FirebaseAuth.getInstance();

        MobileAds.initialize(this.mContext, mContext.getString(R.string.tesUnitIDVideo));
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(mContext.getApplicationContext());
        mRewardedVideoAd.loadAd(mContext.getString(R.string.tesUnitIDVideo), new AdRequest.Builder().build());

        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(mContext.getApplicationContext(), "Video siap ditonton", Toast.LENGTH_SHORT).show();
                EarnCoinActivity.progressBar.setVisibility(View.GONE);
                if (SharedVariable.free_coin>0) {
                    mRewardedVideoAd.show();
                }
            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                SharedVariable.coin = SharedVariable.coin + 50;
                try {
                    ref.child("users").child(SharedVariable.uId).child("coin").setValue(SharedVariable.coin);
                }catch (Exception e){
                    Log.d("ErorUpdateFreeCoin : ",e.toString());
                }
                Toast.makeText(mContext.getApplicationContext(), "Selamat, Anda Berhasil Mendapatkan 50 FreeCoin !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }
        });

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Album album = albumList.get(position);
        holder.title.setText(album.getName());
        holder.count.setText(album.getNumOfSongs() + " Kesempatan / day");

        switch (position){
            case 0 :
                if (SharedVariable.free_coin > 0) {
                    holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showRewardedVideo();
                        }
                    });
                    holder.title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showRewardedVideo();
                        }
                    });
                    holder.count.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showRewardedVideo();
                        }
                    });

                    SharedVariable.free_coin = SharedVariable.free_coin - 1;
                    try {
                        ref.child("users").child(SharedVariable.uId).child("freeCoin").setValue(SharedVariable.free_coin);
                    }catch (Exception e){
                        Log.d("ErorUpdateFreeCoin : ",e.toString());
                    }

                    Log.d("freeCoin : ",""+SharedVariable.free_coin);

                }else {
                    Toast.makeText(mContext.getApplicationContext(),"Kesempatan FreeCoin Video harian telah habis",Toast.LENGTH_SHORT).show();
                    holder.thumbnail.setEnabled(false);
                    holder.title.setEnabled(false);
                    holder.count.setEnabled(false);
                }
                break;

            case 1 :
                holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareBiasa();
                    }
                });
                holder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareBiasa();
                    }
                });
                holder.count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareBiasa();
                    }
                });
                break;
        }


        // loading album cover using Glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);


    }


    /**
     * Showing popup menu when tapping on 3 dots
     */


    /**
     * Click listener for popup menu items
     */


    @Override
    public int getItemCount() {
        return albumList.size();
    }

    private void showRewardedVideo(){

        EarnCoinActivity.progressBar.setVisibility(View.VISIBLE);
        if (mRewardedVideoAd.isLoaded()) {

            mRewardedVideoAd.show();
            EarnCoinActivity.progressBar.setVisibility(View.GONE);
        }else {
            EarnCoinActivity.progressBar.setVisibility(View.VISIBLE);
            mRewardedVideoAd.loadAd(mContext.getString(R.string.tesUnitIDVideo), new AdRequest.Builder().build());
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
            mContext.startActivity(Intent.createChooser(sharingIntent,  mContext.getResources().getString(R.string.app_name)));
            SharedVariable.coin = SharedVariable.coin + 10;
            try {
                ref.child("users").child(SharedVariable.uId).child("coin").setValue(SharedVariable.coin);
            }catch (Exception e){
                Log.d("ErorUpdateFreeCoin : ",e.toString());
            }
           // Toast.makeText(mContext.getApplicationContext(), "Selamat, Anda Berhasil Mendapatkan 10 FreeCoin !", Toast.LENGTH_SHORT).show();

        }catch (ActivityNotFoundException ex){
            Toast.makeText(mContext.getApplicationContext(),"Gagal Share",Toast.LENGTH_SHORT).show();
        }
    }

}
