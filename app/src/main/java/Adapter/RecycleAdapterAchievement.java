package Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import glory.tebaklaguanime.GameActivity;
import glory.tebaklaguanime.MainActivity;
import glory.tebaklaguanime.R;

/**
 * Created by Glory on 03/10/2016.
 */
public class RecycleAdapterAchievement extends RecyclerView.Adapter<RecycleViewHolderAchievement> {


    LayoutInflater inflater;
    Context context;
    Intent i;
    public static String GpubjudulEdit;
    public static String GpubisiEdit;
    private int videoKirim;
    public static ArrayList listDoa;


    //dekalrasi buat List nya
    String[] namaRank ={"Normies","Adventurer","Captain","Hero","Shichibukai","Yonkou","KING"};

    String[] exp={"1.000","2.000","3.500","5.500","8.500","12.000","16.000"};

    int [] resDoa = {R.drawable.icon_normies,R.drawable.icon_adventurer,R.drawable.icon_captain,
            R.drawable.icon_hero,R.drawable.icon_shichibukai,
                    R.drawable.icon_yonkou,R.drawable.icon_king};




    //int ic_aja = R.drawable.greencircle;

    public RecycleAdapterAchievement(final Context context) {

        inflater = LayoutInflater.from(context);
        this.context = context;
        listDoa = new ArrayList();


        //Glist_dari_berita.add("Berita di arraylist1");
    }


    @Override
    public RecycleViewHolderAchievement onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.itemlist_ahievement,parent,false);
        RecycleViewHolderAchievement viewHolderAchievement = new RecycleViewHolderAchievement(v);

        return viewHolderAchievement;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolderAchievement holder , int position) {

        Resources res = context.getResources();
        //holder.checkBoxJudul.setText(nama[position]);

        holder.gmbrList.setImageResource(resDoa[position]);
        holder.txtNamaRank.setText(namaRank[position]);
        holder.txtExpDibutuhkan.setText(exp[position]+" EXP");

        holder.gmbrList.setOnClickListener(clicklistener);
        holder.txtNamaRank.setOnClickListener(clicklistener);
        holder.txtExpDibutuhkan.setOnClickListener(clicklistener);



        holder.gmbrList.setTag(holder);
        holder.txtNamaRank.setTag(holder);
        holder.txtExpDibutuhkan.setTag(holder);

    }

    View.OnClickListener clicklistener = new View.OnClickListener() {


        @Override
        public void onClick(View v) {


            RecycleViewHolderAchievement vHolder = (RecycleViewHolderAchievement) v.getTag();
            int position = vHolder.getPosition();
            int posdoa = 0;

            switch (position){
                case  0 :
                    MainActivity.bunyiKlik();
                    break;
                case 1 :
                    MainActivity.bunyiKlik();
                    break;
                case 2 :
                    MainActivity.bunyiKlik();
                    break;
                case 3 :
                    MainActivity.bunyiKlik();
                    break;
                case 4 :
                    MainActivity.bunyiKlik();
                    break;
                case 5 :
                    MainActivity.bunyiKlik();
                    break;
                case 6 :
                    MainActivity.bunyiKlik();
                    break;
            }


           // Toast.makeText(v.getContext(),"Item diklik "+position,Toast.LENGTH_SHORT).show();



        }
    };


    public int getItemCount() {
        return namaRank.length;
    }
}
