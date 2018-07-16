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

import java.util.ArrayList;
import java.util.List;

import Kelas.DBAdapter;
import Kelas.SharedVariable;
import Kelas.TingkatKesulitan;
import Kelas.User;
import glory.tebaklaguanime.EarnCoinActivity;
import glory.tebaklaguanime.GameActivity;
import glory.tebaklaguanime.LevelActivity;
import glory.tebaklaguanime.MainActivity;
import glory.tebaklaguanime.R;
import glory.tebaklaguanime.ResultActivity;
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
    public static String wolf,tiger,shark,unicorn,pegasus;
    DialogInterface.OnClickListener listener;
    public static User mUser;
    public static List<User> mlistUser;

    private RewardedVideoAd mRewardedVideoAd;
    private boolean mGameOver;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;


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
        Firebase.setAndroidContext(this.context);
        FirebaseApp.initializeApp(context.getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();


        mDB = DBAdapter.getInstance(context.getApplicationContext());
        mlistLevel= mDB.getDataLevel();
        mLevel = mlistLevel.get(0);
        mlistUser = mDB.getDataUser();
        mUser = mlistUser.get(0);

        wolf = SharedVariable.wolf;
        tiger = SharedVariable.tiger;
        shark = SharedVariable.shark;
        unicorn = SharedVariable.unicorn;
       // pegasus = mLevel.getPegasus();

        coinNow = SharedVariable.coin;
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
                if (tiger.equals("T")) {
                    holder.txtHargaCoin.setText("700");
                    holder.imgCoin.setVisibility(holder.imgCoin.VISIBLE);
                    holder.imgLock.setVisibility(holder.imgLock.VISIBLE);
                }else {
                    holder.imgLock.setVisibility(holder.imgLock.INVISIBLE);
                    holder.imgCoin.setVisibility(holder.imgCoin.INVISIBLE);
                    holder.txtHargaCoin.setVisibility(holder.txtHargaCoin.INVISIBLE);
                }
                break;

            case 3:
                if (shark.equals("T")) {
                    holder.txtHargaCoin.setText("900");
                    holder.imgCoin.setVisibility(holder.imgCoin.VISIBLE);
                    holder.imgLock.setVisibility(holder.imgLock.VISIBLE);
                }else {
                    holder.imgLock.setVisibility(holder.imgLock.INVISIBLE);
                    holder.imgCoin.setVisibility(holder.imgCoin.INVISIBLE);
                    holder.txtHargaCoin.setVisibility(holder.txtHargaCoin.INVISIBLE);
                }
                break;
            case 4:
                if (unicorn.equals("T")) {
                    holder.txtHargaCoin.setText("1200");
                    holder.imgCoin.setVisibility(holder.imgCoin.VISIBLE);
                    holder.imgLock.setVisibility(holder.imgLock.VISIBLE);
                    holder.imgHard.setVisibility(holder.imgHard.VISIBLE);
                }else {
                    holder.imgLock.setVisibility(holder.imgLock.INVISIBLE);
                    holder.imgCoin.setVisibility(holder.imgCoin.INVISIBLE);
                    holder.txtHargaCoin.setVisibility(holder.txtHargaCoin.INVISIBLE);
                    holder.imgHard.setVisibility(holder.imgHard.VISIBLE);
                }
                break;
            case 5:
                holder.txtHargaCoin.setText("1500");
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
                        builder.setMessage("Apakah anda ingin membuka Level ini ?");
                        builder.setCancelable(false);

                        listener = new DialogInterface.OnClickListener()
                        {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == DialogInterface.BUTTON_POSITIVE){

                                    if (coinNow >= 500) {
                                        try {
                                            ref.child("users").child(fAuth.getCurrentUser().getUid()).child("level")
                                                    .child("wolf").setValue("Y");
                                        }catch (Exception e){
                                            Log.d("Eror update user : ",e.toString());
                                        }
                                        SharedVariable.wolf = "Y";

                                        coinNow = coinNow - 500;
                                        try {
                                            ref.child("users").child(fAuth.getCurrentUser().getUid()).child("coin").setValue(coinNow);
                                        }catch (Exception e){
                                            Log.d("Eror update user : ",e.toString());
                                        }
                                        SharedVariable.coin = coinNow;

                                        //langsung buka WolfActivity
                                        i = new Intent(context.getApplicationContext(), WolfActivity.class);
                                        int levelID = 1;
                                        i.putExtra("levelID",levelID);
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
                            builder.setMessage("Coin anda tidak cukup, ingin mendapatkan beberapa free coin ?");
                            builder.setCancelable(false);

                            listener = new DialogInterface.OnClickListener()
                            {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which == DialogInterface.BUTTON_POSITIVE){
                                        i = new Intent(context.getApplicationContext(), EarnCoinActivity.class);
                                        context.startActivity(i);
                                       //intent ke Earn Coin Activity
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
                        int levelID = 1;
                        i.putExtra("levelID",levelID);
                        context.startActivity(i);
                    }

                   // Toast.makeText(v.getContext(),"Level Belum Tersedia (Coming Soon)",Toast.LENGTH_SHORT).show();
                    break;
                case 2 :
                    MainActivity.bunyiKlik();

                    if (tiger.equals("T")){

                        if (coinNow > 700 ){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Apakah anda ingin membuka Level ini ?");
                            builder.setCancelable(false);

                            listener = new DialogInterface.OnClickListener()
                            {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which == DialogInterface.BUTTON_POSITIVE){

                                        if (coinNow >= 700) {
                                            try {
                                                ref.child("users").child(fAuth.getCurrentUser().getUid()).child("level")
                                                        .child("tiger").setValue("Y");
                                            }catch (Exception e){
                                                Log.d("Eror update user : ",e.toString());
                                            }
                                            SharedVariable.tiger = "Y";

                                            coinNow = coinNow - 700;
                                            try {
                                                ref.child("users").child(fAuth.getCurrentUser().getUid()).child("coin").setValue(coinNow);
                                            }catch (Exception e){
                                                Log.d("Eror update user : ",e.toString());
                                            }
                                            SharedVariable.coin = coinNow;

                                            //langsung buka WolfActivity
                                            i = new Intent(context.getApplicationContext(), WolfActivity.class);
                                            int levelID = 2;
                                            i.putExtra("levelID",levelID);
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
                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Coin anda tidak cukup, ingin mendapatkan beberapa free coin ?");
                            builder.setCancelable(false);

                            listener = new DialogInterface.OnClickListener()
                            {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which == DialogInterface.BUTTON_POSITIVE){
                                        i = new Intent(context.getApplicationContext(), EarnCoinActivity.class);
                                        context.startActivity(i);
                                        //intent ke Earn Coin Activity
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

                    }else {
                        i = new Intent(context.getApplicationContext(), WolfActivity.class);
                        int levelID = 2;
                        i.putExtra("levelID",levelID);
                        context.startActivity(i);
                    }
                    break;

                case 3 :
                    MainActivity.bunyiKlik();

                    if (shark.equals("T")){

                        if (coinNow > 900 ){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Apakah anda ingin membuka Level ini ?");
                            builder.setCancelable(false);

                            listener = new DialogInterface.OnClickListener()
                            {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which == DialogInterface.BUTTON_POSITIVE){

                                        if (coinNow >= 700) {
                                            try {
                                                ref.child("users").child(fAuth.getCurrentUser().getUid()).child("level")
                                                        .child("shark").setValue("Y");
                                            }catch (Exception e){
                                                Log.d("Eror update user : ",e.toString());
                                            }
                                            SharedVariable.shark = "Y";

                                            coinNow = coinNow - 900;
                                            try {
                                                ref.child("users").child(fAuth.getCurrentUser().getUid()).child("coin").setValue(coinNow);
                                            }catch (Exception e){
                                                Log.d("Eror update user : ",e.toString());
                                            }
                                            SharedVariable.coin = coinNow;

                                            //langsung buka WolfActivity
                                            i = new Intent(context.getApplicationContext(), WolfActivity.class);
                                            int levelID = 3;
                                            i.putExtra("levelID",levelID);
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
                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Coin anda tidak cukup, ingin mendapatkan beberapa free coin ?");
                            builder.setCancelable(false);

                            listener = new DialogInterface.OnClickListener()
                            {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which == DialogInterface.BUTTON_POSITIVE){
                                        i = new Intent(context.getApplicationContext(), EarnCoinActivity.class);
                                        context.startActivity(i);
                                        //intent ke Earn Coin Activity
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

                    }else {
                        i = new Intent(context.getApplicationContext(), WolfActivity.class);
                        int levelID = 3;
                        i.putExtra("levelID",levelID);
                        context.startActivity(i);
                    }
                    break;

                case 4 :
                    if (unicorn.equals("T")){

                        if (coinNow > 1200 ){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Apakah anda ingin membuka Level ini ?");
                            builder.setCancelable(false);

                            listener = new DialogInterface.OnClickListener()
                            {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which == DialogInterface.BUTTON_POSITIVE){

                                        if (coinNow >= 1200) {
                                            try {
                                                ref.child("users").child(fAuth.getCurrentUser().getUid()).child("level")
                                                        .child("unicorn").setValue("Y");
                                            }catch (Exception e){
                                                Log.d("Eror update user : ",e.toString());
                                            }
                                            SharedVariable.unicorn = "Y";

                                            coinNow = coinNow - 1200;
                                            try {
                                                ref.child("users").child(fAuth.getCurrentUser().getUid()).child("coin").setValue(coinNow);
                                            }catch (Exception e){
                                                Log.d("Eror update user : ",e.toString());
                                            }
                                            SharedVariable.coin = coinNow;


                                            //langsung buka WolfActivity
                                            i = new Intent(context.getApplicationContext(), WolfActivity.class);
                                            int levelID = 4;
                                            i.putExtra("levelID",levelID);
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
                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Coin anda tidak cukup, ingin mendapatkan beberapa free coin ?");
                            builder.setCancelable(false);

                            listener = new DialogInterface.OnClickListener()
                            {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(which == DialogInterface.BUTTON_POSITIVE){
                                        i = new Intent(context.getApplicationContext(), EarnCoinActivity.class);
                                        context.startActivity(i);
                                        //intent ke Earn Coin Activity
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

                    }else {
                        i = new Intent(context.getApplicationContext(), WolfActivity.class);
                        int levelID = 4;
                        i.putExtra("levelID",levelID);
                        context.startActivity(i);
                    }
                    break;

                case 5:
                    Toast.makeText(context.getApplicationContext(),"Level belum tersedia (Coming Soon)",Toast.LENGTH_SHORT).show();
                    break;
            }

           // Toast.makeText(v.getContext(),"Item diklik "+position,Toast.LENGTH_SHORT).show();
        }
    };



    public int getItemCount() {
        return namaDoa.length;
    }
}
