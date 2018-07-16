package glory.tebaklaguanime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import Kelas.SharedVariable;

public class LeaderboardActivity extends AppCompatActivity {

    ListView listView;
    String[] listviewTitle = new String[]{
            "ListView Title 1", "ListView Title 2", "ListView Title 3", "ListView Title 4",
            "ListView Title 5", "ListView Title 6", "ListView Title 7", "ListView Title 8",

    };
    String[] listviewNama = new String[]{};
    int[] listviewImage = new int[]{
            R.drawable.icon_sky_angel, R.drawable.icon_tiger, R.drawable.icon_fallen_angel, R.drawable.icon_beast,
            R.drawable.icon_wolf, R.drawable.icon_captain, R.drawable.icon_adventurer, R.drawable.icon_gorosei,

    };

    String[] listviewShortDescription = new String[]{
            "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
            "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",

    };
    int [] listViewNumber = new int[]{
            1,2,3,4,5,6,7,8
    };
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    public static List<String> list_nama = new ArrayList();
    public static List<Integer> list_exp = new ArrayList();
    public static List<Integer> list_icon = new ArrayList();
    public static List<String> list_key = new ArrayList();
    ProgressBar progressBar;
    public static  List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
    Button btnRefresh;
    ListView androidListView;
    public static  ArrayList<String> listBadge = new ArrayList();
    public static List<ArrayList> listArrayBadge = new ArrayList<>();
    private String keyBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(LeaderboardActivity.this);
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        androidListView = (ListView) findViewById(R.id.list_view);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);

        ref = ref.child("users");
        progressBar.setVisibility(View.VISIBLE);
        final String[] from = {"listview_image", "listview_title", "listview_discription","listview_number"};

       final int[] to = {R.id.listviewImage, R.id.listview_item_title, R.id.listview_item_short_description,R.id.listview_item_number};

        ambilDataNama();
        if (list_nama.size() != 0){
            masukkanKeListView();
            SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.custom_listview, from, to);
            androidListView.setAdapter(simpleAdapter);
        }else {
            ambilDataNama();
            if (list_nama.size() == 0){
                ambilDataNama();
            }
        }
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ambilDataNama();
                masukkanKeListView();
                SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.custom_listview, from, to);
                androidListView.setAdapter(simpleAdapter);
            }
        });
        /*
        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              //  Toast.makeText(getApplicationContext(),"posisi : "+i,Toast.LENGTH_SHORT).show();
                String uId = list_key.get(i);
                String nama = list_nama.get(i).toString();
                int exp = list_exp.get(i);

                Intent intent = new Intent(LeaderboardActivity.this,UserProfileActivity.class);
                intent.putExtra("uId",uId);
                intent.putExtra("nama",nama);
                intent.putExtra("exp",exp);
                startActivity(intent);
            }
        });
        */

    }

    private void masukkanKeListView(){
        aList.clear();
        for (int i = 0; i < list_nama.size(); i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            //hm.put("listview_title", listviewTitle[i]);
            hm.put("listview_title", list_nama.get(i).toString());
           // hm.put("listview_discription", listviewShortDescription[i]);
            hm.put("listview_discription", Integer.toString(list_exp.get(i))+" EXP");
            hm.put("listview_image", Integer.toString(list_icon.get(i)));
            hm.put("listview_number",Integer.toString(listViewNumber[i]));
            aList.add(hm);
        }

    }

    private void ambilDataNama(){

        progressBar.setVisibility(View.VISIBLE);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        com.google.firebase.database.Query maxExpQuery = ref.orderByChild("exp").limitToLast(50);
        maxExpQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list_exp.clear();
                list_nama.clear();
                list_icon.clear();
                list_key.clear();
                listArrayBadge.clear();
                int expNya = 0;
                String nama = "kosong";
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    expNya = child.child("exp").getValue(Integer.class);
                    nama = child.child("displayName").getValue().toString();
                    String uID = child.getKey();

                    list_exp.add(expNya);
                    list_nama.add(nama);
                    int icon =  SharedVariable.expToIcon(expNya);
                    list_icon.add(icon);
                    list_key.add(uID);

                }
                progressBar.setVisibility(View.GONE);
                Log.d("jml LE : ",""+list_exp.size());
                Collections.reverse(list_exp);
                Collections.reverse(list_nama);
                Collections.reverse(list_icon);
                Collections.reverse(list_key);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
