package Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Kelas.DBAdapter;
import Kelas.TingkatKesulitan;
import Kelas.User;
import glory.tebaklaguanime.GameActivity;
import glory.tebaklaguanime.MainActivity;
import glory.tebaklaguanime.R;

/**
 * Created by Glory on 03/10/2016.
 */
public class RecycleAdapterLevel extends RecyclerView.Adapter<RecycleViewHolderLevel> {


    LayoutInflater inflater;
    Context context;
    Intent i;
    public static String GpubjudulEdit;
    public static String GpubisiEdit;
    private int videoKirim;
    public static ArrayList listDoa;

    DBAdapter mDB;
    public static TingkatKesulitan mLevel;
    public static List<TingkatKesulitan> mlistLevel;
    protected Cursor cursor;
    public static String wolf,tiger;


    //dekalrasi buat List nya
    String[] nama ={"Doa 1 ","Doa 2","Doa 3"};

    String[] namaDoa={"Slime","Wolf","Tiger"};

    int [] resDoa = {R.drawable.slime,R.drawable.icon_wolf,R.drawable.icon_tiger};




    //int ic_aja = R.drawable.greencircle;

    public RecycleAdapterLevel(final Context context) {

        inflater = LayoutInflater.from(context);
        this.context = context;
        listDoa = new ArrayList();
        mDB = DBAdapter.getInstance(context.getApplicationContext());
        mlistLevel= mDB.getDataLevel();
        mLevel = mlistLevel.get(0);

        wolf = mLevel.getWolf();
        tiger = mLevel.getTiger();

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
                holder.txtHargaCoin.setText("500");
                break;

            case 2 :
                holder.txtHargaCoin.setText("700");
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
                    Toast.makeText(v.getContext(),"Level Belum Tersedia (Coming Soon)",Toast.LENGTH_SHORT).show();
                    break;
                case 2 :
                    MainActivity.bunyiKlik();
                    Toast.makeText(v.getContext(),"Level Belum Tersedia (Coming Soon)",Toast.LENGTH_SHORT).show();
                    break;
            }


           // Toast.makeText(v.getContext(),"Item diklik "+position,Toast.LENGTH_SHORT).show();



        }
    };


    public int getItemCount() {
        return namaDoa.length;
    }
}
