package project.richard.richpay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AccountActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();;
    private DatabaseReference mDatabase;
    private DatabaseReference readUser;
    private double newBalance;
    private Long userBalance;
    private String lastTransDesc;
    private Long lastTransAmmount;
    private String lastTransId;

    private static final String TAG = "TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = fuser.getUid();
        final DatabaseReference myRef = database.getReference();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fuser = firebaseAuth.getCurrentUser();
                if (fuser == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        myRef.child("users").child(uid).child("balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                userBalance = (Long) dataSnapshot.getValue();
                String ub = Long.toString(userBalance);
                TextView balancetest = (TextView) findViewById(R.id.account_balance);
                balancetest.setText(ub);
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                findViewById(R.id.topup_button).setVisibility(View.VISIBLE);
                Log.e("TESTBALANCE", ub);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        Query query = myRef.child("users").child(uid).child("transactions")
                .orderByKey()
                .limitToLast(1);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    lastTransDesc = childSnapshot.child("transInfo").getValue().toString();
                    lastTransAmmount = (Long) childSnapshot.child("amount").getValue();
                    lastTransId = childSnapshot.child("transId").getValue().toString();
                    String lt = Long.toString(lastTransAmmount);
                    TextView lastranstag = findViewById(R.id.last_trans_desc);
                    TextView lasttransamm = findViewById(R.id.last_trans_amount);
                    TextView lasttransid = findViewById(R.id.last_trans_id);
                    lastranstag.setText(lastTransDesc);
                    lasttransamm.setText(lt);
                    lasttransid.setText(lastTransId);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });

        BottomNavigationView bottomNavigationView= (BottomNavigationView)
                findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_transactions:
                                Intent intent = new Intent(AccountActivity.this, ViewTransactionsActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.action_account:
                                Intent intent1 = new Intent(AccountActivity.this, AccountActivity.class);
                                startActivity(intent1);
                                break;

                            case R.id.action_settings:
                                Intent intent2 = new Intent(AccountActivity.this, SettingsPageActivity.class);
                                startActivity(intent2);
                                break;
                        }
                        return true;
                    }
                });

        Button topup = (Button) findViewById(R.id.topup_button);
        topup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                newBalance = userBalance + 10;
                String uniqueID = UUID.randomUUID().toString();
                String today = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
                String newchild = today + uniqueID;
                Transaction topuptrans = new Transaction(uniqueID, 10, "Topped up account ", today);
                myRef.child("users").child(fuser.getUid()).child("balance").setValue(newBalance);
                myRef.child("users").child(fuser.getUid()).child("transactions").child(newchild).setValue(topuptrans);
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);

            }
        });
        Button homebtn = (Button) findViewById(R.id.homebutton);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, MainActivity.class));
            }
        });

        String email = fuser.getEmail();
        TextView accountemail = (TextView)findViewById(R.id.account_email);
        accountemail.setText(email);


    }

}
