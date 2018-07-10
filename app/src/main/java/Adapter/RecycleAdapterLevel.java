package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;
import java.util.List;

import Kelas.DBAdapter;
import Kelas.TingkatKesulitan;
import Kelas.User;
import glory.tebaklaguanime.GameActivity;
import glory.tebaklaguanime.LevelActivity;
import glory.tebaklaguanime.MainActivity;
import glory.tebaklaguanime.R;
import glory.tebaklaguanime.WolfActivity;

/**
 * Created by Glory on 03/10/2016.
 */
public class RecycleAdapterLevel extends RecyclerView.Adapter<RecycleViewHolderLevel> {


    LayoutInflater inflater;
    Context context;
    Intent i;
    public static String GpubjudulEdit;
    public static String GpubisiEdit;
    private int coinNow,kesempatanFreeCoin;
    public static ArrayList listDoa;

    DBAdapter mDB;
    public static TingkatKesulitan mLevel;
    public static List<TingkatKesulitan> mlistLevel;
    protected Cursor cursor;
    public static String wolf,tiger;
    DialogInterface.OnClickListener listener;
    public static User mUser;
    public static List<User> mlistUser;

    private RewardedVideoAd mRewardedVideoAd;
    private boolean mGameOver;


    //dekalrasi buat List nya
    String[] nama ={"Doa 1 ","Doa 2","Doa 3"};

    String[] namaDoa={"Slime","Wolf","Tiger","Shark","Unicorn","Pegasus"};

    int [] resDoa = {R.drawable.slime,R.drawable.icon_wolf,R.drawable.icon_tiger,R.drawable.icon_shark,R.drawable.icon_unicorn,
            R.drawable.icon_pegasus};




    //int ic_aja = R.drawable.greencircle;

    public RecycleAdapterLevel(final Context context) {

        inflater = LayoutInflater.from(context);
        this.context = context;
        listDoa = new ArrayList();
        mDB = DBAdapter.getInstance(context.getApplicationContext());
        mlistLevel= mDB.getDataLevel();
        mLevel = mlistLevel.get(0);
        mlistUser = mDB.getDataUser();
        mUser = mlistUser.get(0);

        wolf = mLevel.getWolf();
        tiger = mLevel.getTiger();
        coinNow = mUser.getCoin();
        kesempatanFreeCoin = mUser.getFree_coin();

        MobileAds.initialize(context.getApplicationContext(), context.getString(R.string.tesUnitIDVideo));
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context.getApplicationContext());
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                muatUlangIklan();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                Toast.makeText(context.getApplicationContext(), "Anda berhasil mendapatkan 40 coin.", Toast.LENGTH_SHORT).show();
                kesempatanFreeCoin = kesempatanFreeCoin - 1;

                SQLiteDatabase db = mDB.getWritableDatabase();
                db.execSQL("update tb_user set free_coin='"+kesempatanFreeCoin+"' where nama='User'");
                mlistUser = mDB.getDataUser();
                mUser = mlistUser.get(0);
                kesempatanFreeCoin = mUser.getFree_coin();

                coinNow = coinNow + 40;
                SQLiteDatabase db2 = mDB.getWritableDatabase();
                db.execSQL("update tb_user set coin='"+coinNow+"' where nama='User'");
                mlistUser = mDB.getDataUser();
                mUser = mlistUser.get(0);
                coinNow = mUser.getCoin();
                LevelActivity.txtCoin.setText(String.valueOf(coinNow));

                Log.d("kesempatan Free adap : ",String.valueOf(kesempatanFreeCoin));
                muatUlangIklan();
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                muatUlangIklan();
            }
        });
        muatUlangIklan();

        //Glist_dari_berita.add("Berita di arraylist1");
    }


    @Override
    public RecycleViewHolderLevel onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.itemlist_pilihsewa,parent,false);
        RecycleViewHolderLevel viewHolderLevel= new RecycleViewHolderLevel(v);
       // list_dari_berita.add("Berita di arraylist1");

        return viewHolderLevel;
    }


    @Override
    public void onBindViewHolder(RecycleViewHolderLevel holder , int position) {

        Resources res = context.getResources();
        //holder.checkBoxJudul.setText(nama[position]);

        holder.gmbrList.setImageResource(resDoa[position]);
        holder.txtNamaLevel.setText(namaDoa[position]);

        holder.gmbrList.setOnClickListener(clicklistener);
        holder.txtNamaLevel.setOnClickListener(clicklistener);
        holder.cardItem.setOnClickListener(clicklistener);
        holder.relaCard.setOnClickListener(clicklistener);

        //mengatur penguncian Level
        switch (position){
            case 0 :
                holder.imgLock.setVisibility(holder.imgLock.INVISIBLE);
                holder.imgCoin.setVisibility(holder.imgCoin.INVISIBLE);
                holder.txtHargaCoin.setVisibility(holder.txtHargaCoin.INVISIBLE);
                break;

            case 1 :
                if (wolf.equals("T")) {
                    holder.txtHargaCoin.setText("500");
                    holder.imgCoin.setVisibility(holder.imgCoin.VISIBLE);
                    holder.imgLock.setVisibility(holder.imgLock.VISIBLE);
                }else {
                    holder.imgLock.setVisibility(holder.imgLock.INVISIBLE);
                    holder.imgCoin.setVisibility(holder.imgCoin.INVISIBLE);
                    holder.txtHargaCoin.setVisibility(holder.txtHargaCoin.INVISIBLE);
                }
                break;

            case 2 :
                holder.txtHargaCoin.setText("700");
                break;

            case 3:
                holder.txtHargaCoin.setText("800");
                break;
            case 4:
                holder.txtHargaCoin.setText("1000");
                break;
            case 5:
                holder.txtHargaCoin.setText("1300");
                break;
        }

        holder.gmbrList.setTag(holder);
        holder.txtNamaLevel.setTag(holder);
        holder.relaCard.setTag(holder);
        holder.cardItem.setTag(holder);

    }

    View.OnClickListener clicklistener = new View.OnClickListener() {


        @Override
        public void onClick(View v) {


            RecycleViewHolderLevel vHolder = (RecycleViewHolderLevel) v.getTag();
            int position = vHolder.getPosition();
            int posdoa = 0;

            switch (position){
                case  0 :
                    MainActivity.bunyiKlik();
                    i = new Intent(context.getApplicationContext(), GameActivity.class);
                    context.startActivity(i);
                    break;
                case 1 :
                    MainActivity.bunyiKlik();

                    if (wolf.equals("T")){


                        if (coinNow >500){


                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Apakan anda ingin membuka Level ini ?");
                        builder.setCancelable(false);

                        listener = new DialogInterface.OnClickListener()
                        {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == DialogInterface.BUTTON_POSITIVE){

                                    if (coinNow >= 500) {
                                        SQLiteDatabase db = mDB.getWritableDatabase();
                                        db.execSQL("update tb_level set wolf='Y' where id=1");

                                        coinNow = coinNow - 500;
                                        db.execSQL("update tb_user set coin='"+coinNow+"' where nama='User'");

                                        //langsung buka WolfActivity
                                        i = new Intent(context.getApplicationContext(), WolfActivity.class);
                                        context.startActivity(i);
                                    }else {
                                        Toast.makeText(context.getApplicationContext(),"Coin anda tidak cukup",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                if(which == DialogInterface.BUTTON_NEGATIVE){
                                    dialog.cancel();
                                }
                            }
                        };
                        builder.setPositiveButton("Ya",listener);
                        builder.setNegativeButton("Tidak", listener);
                        builder.show();

                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Coin anda tidak cukup, ingin mendapatkan free coin dengan menonton video ?");
                            builder.setCancelable(false);

                            listener = new DialogInterface.OnClickListener()
                            {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which == DialogInterface.BUTTON_POSITIVE){

                                        if (kesempatanFreeCoin > 0){
                                            showRewardedVideo();
                                            Log.d("kese pas klik yes  : ",String.valueOf(kesempatanFreeCoin));
                                        }else {
                                            Toast.makeText(context.getApplicationContext(),"Maaf Kesempatan Free Coin anda habis, silakan menunggu 12jam lagi",Toast.LENGTH_SHORT).show();
                                        }
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

                    }else{
                        i = new Intent(context.getApplicationContext(), WolfActivity.class);
                        context.startActivity(i);
                    }

                   // Toast.makeText(v.getContext(),"Level Belum Tersedia (Coming Soon)",Toast.LENGTH_SHORT).show();
                    break;
                case 2 :
                    MainActivity.bunyiKlik();
                    Toast.makeText(v.getContext(),"Level Belum Tersedia (Coming Soon)",Toast.LENGTH_SHORT).show();
                    break;
            }


           // Toast.makeText(v.getContext(),"Item diklik "+position,Toast.LENGTH_SHORT).show();



        }
    };


    public void muatUlangIklan(){
        mRewardedVideoAd.loadAd(context.getString(R.string.myVideoRewardUnitID), new AdRequest.Builder().build());
    }
    private void showRewardedVideo(){

        mDB = DBAdapter.getInstance(context.getApplicationContext());
        mlistUser = mDB.getDataUser();
        mUser = mlistUser.get(0);
        kesempatanFreeCoin = mUser.getFree_coin();
        if (kesempatanFreeCoin>0) {
            if (mRewardedVideoAd.isLoaded()) {
                mRewardedVideoAd.show();
            }
        }else {
            Toast.makeText(context.getApplicationContext(),"Maaf Kesempatan Free Coin anda habis, silakan menunggu 12jam lagi",Toast.LENGTH_SHORT).show();
        }
    }

    public int getItemCount() {
        return namaDoa.length;
    }
}
