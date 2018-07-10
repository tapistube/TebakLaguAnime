package glory.tebaklaguanime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import Adapter.RecycleAdapterAchievement;
import Adapter.RecycleAdapterLevel;
import Kelas.DBAdapter;
import Kelas.User;

public class AchievementActivity extends AppCompatActivity {

    RecyclerView recyclerLevel;
    RecycleAdapterAchievement adapterAchievement;

    DBAdapter mDB;
    public static User mUser;
    public static List<User> mlistUser;
    int expNow,rank;
    TextView txtExp,txtRank;
    ImageView imgRank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_achievement);

        recyclerLevel = (RecyclerView) findViewById(R.id.recycler_listlevel);
        adapterAchievement = new RecycleAdapterAchievement(this);
        recyclerLevel.setAdapter(adapterAchievement);
        recyclerLevel.setLayoutManager(new LinearLayoutManager(this));
        txtExp = (TextView) findViewById(R.id.txt_exp);
        txtRank = (TextView) findViewById(R.id.txt_rank);
        imgRank = (ImageView) findViewById(R.id.img_iconlist);

        int [] resDoa = {R.drawable.icon_normies,R.drawable.icon_adventurer,R.drawable.icon_captain,
                R.drawable.icon_hero,R.drawable.icon_shichibukai,
                R.drawable.icon_yonkou,R.drawable.icon_king,
                R.drawable.icon_gorosei,R.drawable.icon_beast,R.drawable.icon_fallen_angel,R.drawable.icon_sky_angel};

        mDB = DBAdapter.getInstance(getApplicationContext());
        mlistUser = mDB.getDataUser();
        mUser = mlistUser.get(0);
        expNow = mUser.getExp();
        rank = mUser.getRank();
        txtExp.setText(String.valueOf(expNow));


                if (expNow<2000) {
                    txtRank.setText("Normies");
                    imgRank.setImageResource(resDoa[0]);
                }else if (expNow < 3500) {
                    txtRank.setText("Adventurer");
                    imgRank.setImageResource(resDoa[1]);
                }else if (expNow < 5500) {
                    txtRank.setText("Captain");
                    imgRank.setImageResource(resDoa[2]);
                }else if (expNow < 8500) {
                    txtRank.setText("Hero");
                    imgRank.setImageResource(resDoa[3]);
                }else if (expNow < 12000) {
                    txtRank.setText("Shichibukai");
                    imgRank.setImageResource(resDoa[4]);
                }else if (expNow < 16000) {
                    txtRank.setText("Yonkou");
                    imgRank.setImageResource(resDoa[5]);
                }else if (expNow < 23000){
                    txtRank.setText("KING");
                    imgRank.setImageResource(resDoa[6]);
                } else if (expNow < 30000){
                    txtRank.setText("Gorosei");
                    imgRank.setImageResource(resDoa[7]);
                }else if (expNow < 38000){
                    txtRank.setText("Beast");
                    imgRank.setImageResource(resDoa[8]);
                }else if (expNow < 48000){
                    txtRank.setText("Fallen Angel");
                    imgRank.setImageResource(resDoa[9]);
                }else {
                    txtRank.setText("Sky Angel");
                    imgRank.setImageResource(resDoa[10]);
                }

    }
}
