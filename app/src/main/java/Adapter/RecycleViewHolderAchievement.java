package Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import glory.tebaklaguanime.R;

/**
 * Created by Glory on 03/10/2016.
 */
public class RecycleViewHolderAchievement extends RecyclerView.ViewHolder {

    public TextView txtNamaRank;
    public TextView txtExpDibutuhkan;
    public ImageView gmbrList;


    public RecycleViewHolderAchievement(View itemView) {
        super(itemView);

        txtNamaRank= (TextView) itemView.findViewById(R.id.txt_namaRank);
        txtExpDibutuhkan = (TextView) itemView.findViewById(R.id.txt_expDibutuhkan);
        gmbrList = (ImageView) itemView.findViewById(R.id.img_iconlist);

    }
}
