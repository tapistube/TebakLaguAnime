package Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import glory.tebaklaguanime.R;

/**
 * Created by Glory on 03/10/2016.
 */
public class RecycleViewHolderLevel extends RecyclerView.ViewHolder {

    public TextView txtNamaLevel;
    public ImageView gmbrList;


    public RecycleViewHolderLevel(View itemView) {
        super(itemView);

        txtNamaLevel = (TextView) itemView.findViewById(R.id.txt_namaLevel);
        gmbrList = (ImageView) itemView.findViewById(R.id.img_iconlist);


    }
}