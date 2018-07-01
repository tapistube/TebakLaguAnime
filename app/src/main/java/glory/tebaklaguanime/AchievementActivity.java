package glory.tebaklaguanime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import Adapter.RecycleAdapterAchievement;
import Adapter.RecycleAdapterLevel;

public class AchievementActivity extends AppCompatActivity {

    RecyclerView recyclerLevel;
    RecycleAdapterAchievement adapterAchievement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_achievement);

        recyclerLevel = (RecyclerView) findViewById(R.id.recycler_listlevel);
        adapterAchievement = new RecycleAdapterAchievement(this);
        recyclerLevel.setAdapter(adapterAchievement);
        recyclerLevel.setLayoutManager(new LinearLayoutManager(this));
    }
}
