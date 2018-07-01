package glory.tebaklaguanime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import Adapter.RecycleAdapterLevel;

public class LevelActivity extends AppCompatActivity {

    RecyclerView recyclerLevel;
    RecycleAdapterLevel adapterLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_level);

        recyclerLevel = (RecyclerView) findViewById(R.id.recycler_listlevel);
        adapterLevel = new RecycleAdapterLevel(this);
        recyclerLevel.setAdapter(adapterLevel);
        recyclerLevel.setLayoutManager(new LinearLayoutManager(this));
    }
}
