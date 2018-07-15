package glory.tebaklaguanime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Kelas.SharedVariable;
import Kelas.Utils;
import Kelas.Voucher;

public class VoucherActivity extends AppCompatActivity {

    EditText emailId;
    TextView submit, back;
    Intent i;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    public static List<String> list_kode_voucher = new ArrayList();
    public static List<String> list_key_voucher = new ArrayList();
    public static List<String> list_keyUsed_voucher = new ArrayList();
    ProgressBar progressBar;
    String kode,key;
    int status;
    ArrayList<Voucher> list_vouchers = new ArrayList<>();
    private String inputVocer,keyVocer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(VoucherActivity.this);
        ref = FirebaseDatabase.getInstance().getReference();
        refUser = ref.child("users");
        ref = ref.child("userReadable").child("voucher");


        fAuth = FirebaseAuth.getInstance();
        //get dulu datanya kayak login kobal
        emailId = (EditText) findViewById(R.id.registered_emailid);
        submit = (TextView) findViewById(R.id.forgot_button);
        back = (TextView) findViewById(R.id.backToLoginBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

       getDataVoucher();
        getUsedVoucher();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButtonTask();
            }
        });
    }

    private void submitButtonTask() {
        String getEmailId = emailId.getText().toString();
        int jmlKode = list_kode_voucher.size();

        // Pattern for email id validation
        Pattern p = Pattern.compile(Utils.regEx);

        // First check if email id is not null else show error toast
        if (getEmailId.equals("") || getEmailId.length() == 0)

            customToast("Please enter voucher code");
        else {
            inputVocer = getEmailId;
            if (list_kode_voucher.contains(inputVocer)) {
                for (int c = 0; c < jmlKode; c++) {

                    if (list_kode_voucher.get(c).toString().equals(inputVocer)) {

                        keyVocer = list_key_voucher.get(c).toString();
                        if (list_keyUsed_voucher.contains(keyVocer)){
                            customToast("Kode Sudah pernah digunakan");
                        }else {
                            customToast("Kode berhasil");

                            String kodeUser = list_kode_voucher.get(c).toString();
                            keyVocer = list_key_voucher.get(c).toString();
                            //Toast.makeText(getApplicationContext(), "Kode nya user : " + kodeUser, Toast.LENGTH_SHORT).show();
                            prosesVoucher();
                            emailId.setText("");
                            break;
                        }
                    }
                }
            }else {
                customToast("Kode Tidak Ditemukan");
            }
        }
    }


    private void getUsedVoucher(){
        progressBar.setVisibility(View.VISIBLE);
        emailId.setEnabled(false);
        back.setEnabled(false);
        submit.setEnabled(false);

        refUser.child(fAuth.getCurrentUser().getUid()).child("usedVoucher").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list_keyUsed_voucher.clear();
                for (DataSnapshot child : dataSnapshot.getChildren() ){
                    String keyUsedVoucer = child.getKey();
                    list_keyUsed_voucher.add(keyUsedVoucer);
                    //Toast.makeText(getApplicationContext(),"used vocer = "+keyUsedVoucer,Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
                emailId.setEnabled(true);
                back.setEnabled(true);
                submit.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void  prosesVoucher(){
        progressBar.setVisibility(View.VISIBLE);

        switch (inputVocer){
            case "ASDFGP" :
                //voucher beta test badge
                Toast.makeText(getApplicationContext(), "Congratulations ! , You get a Beta Test Badge  : " + inputVocer, Toast.LENGTH_SHORT).show();
                refUser.child(fAuth.getCurrentUser().getUid()).child("badges").child("b1").child("b1").setValue("Beta tester");
                refUser.child(fAuth.getCurrentUser().getUid()).child("usedVoucher").child(keyVocer).child(keyVocer).setValue(inputVocer);
                break;

            case "TRKDAG":
                //voucher 300koin
                Toast.makeText(getApplicationContext(), "Congratulations ! , You get Free 300 Coin: " + inputVocer, Toast.LENGTH_SHORT).show();
                SharedVariable.coin = SharedVariable.coin + 300;
                refUser.child(fAuth.getCurrentUser().getUid()).child("coin").setValue(SharedVariable.coin);
                refUser.child(fAuth.getCurrentUser().getUid()).child("usedVoucher").child(keyVocer).child(keyVocer).setValue(inputVocer);
                break;

            case "LRKDGM":
                //voucher 500koin
                Toast.makeText(getApplicationContext(), "Congratulations ! , You get Free 500coin  : " + inputVocer, Toast.LENGTH_SHORT).show();
                SharedVariable.coin = SharedVariable.coin + 500;
                refUser.child(fAuth.getCurrentUser().getUid()).child("coin").setValue(SharedVariable.coin);
                refUser.child(fAuth.getCurrentUser().getUid()).child("usedVoucher").child(keyVocer).child(keyVocer).setValue(inputVocer);
                break;

            case "VBDDGM":
                //voucher badge donator
                Toast.makeText(getApplicationContext(), "Congratulations ! , You get Donator Badge  : " + inputVocer, Toast.LENGTH_SHORT).show();
                break;

            case "VTLDGM":
                //voucher badge TOP leaderboard
                Toast.makeText(getApplicationContext(), "Congratulations ! , You get TOP leaderboard Badge  : " + inputVocer, Toast.LENGTH_SHORT).show();
                break;
        }
        progressBar.setVisibility(View.GONE);
    }

    private void getDataVoucher(){
        progressBar.setVisibility(View.VISIBLE);
        emailId.setEnabled(false);
        back.setEnabled(false);
        submit.setEnabled(false);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list_kode_voucher.clear();
                list_key_voucher.clear();
                for (DataSnapshot child : dataSnapshot.getChildren() ){
                    key = child.getKey();

                    kode = child.child("namaKode").getValue(String.class);

                    status = child.child("status").getValue(Integer.class);
                    Log.d("stts vocer:",String.valueOf(status));
                    if (status == 1){
                        list_kode_voucher.add(kode);
                        list_key_voucher.add(key);
                    }


                }
               // Toast.makeText(getApplicationContext(),"list kode 1 :"+list_kode_voucher.get(0),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                emailId.setEnabled(true);
                back.setEnabled(true);
                submit.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public  void customToast(String s){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_root));

        TextView text = (TextView) layout.findViewById(R.id.toast_error);
        text.setText(s);
        Toast toast = new Toast(getApplicationContext());// Get Toast Context
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
        toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
        toast.setView(layout); // Set Custom View over toast
        toast.show();// Finally show toast
    }
}
