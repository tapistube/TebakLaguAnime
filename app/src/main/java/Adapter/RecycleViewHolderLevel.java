package Adapter;

import android.support.v7.widget.CardView;
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

    public TextView txtNamaLevel,txtHargaCoin;
    public ImageView gmbrList,imgLock,imgCoin,imgHard;
    public CardView cardItem;
    public RelativeLayout relaCard;


    public RecycleViewHolderLevel(View itemView) {
        super(itemView);

        txtNamaLevel = (TextView) itemView.findViewById(R.id.txt_namaLevel);
        txtHargaCoin = (TextView) itemView.findViewById(R.id.txtHargaCoin);
        gmbrList = (ImageView) itemView.findViewById(R.id.img_iconlist);
        cardItem = (CardView) itemView.findViewById(R.id.cardlist_item);
        relaCard = (RelativeLayout) itemView.findViewById(R.id.relaCard);
        imgLock = (ImageView) itemView.findViewById(R.id.img_lock);
        imgCoin = (ImageView) itemView.findViewById(R.id.imgCoin);
        imgHard = (ImageView) itemView.findViewById(R.id.imgHard);


    }
}
