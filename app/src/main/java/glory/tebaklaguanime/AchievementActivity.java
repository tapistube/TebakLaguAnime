package glory.tebaklaguanime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
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

        mDB = DBAdapter.getInstance(getApplicationContext());
        mlistUser = mDB.getDataUser();
        mUser = mlistUser.get(0);
        expNow = mUser.getExp();
        rank = mUser.getRank();
        txtExp.setText(String.valueOf(expNow));

        switch (rank){
            case 1 :
                txtRank.setText("Normies");
                break;
            case 2 :
                txtRank.setText("Adventurer");
                break;
            case 3 :
                txtRank.setText("Captain");
                break;
            case 4 :
                txtRank.setText("Hero");
                break;
            case 5 :
                txtRank.setText("Shichibukai");
                break;
            case 6 :
                txtRank.setText("Yonkou");
                break;
            case 7 :
                txtRank.setText("KING");
                break;
        }
    }
}
