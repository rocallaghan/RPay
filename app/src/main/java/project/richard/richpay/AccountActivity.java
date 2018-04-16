package project.richard.richpay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();;
    private DatabaseReference mDatabase;
    private DatabaseReference readUser;
    private double userBalance ;
    private double newBalance;

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

        myRef.child("users").child(fuser.getUid()).child("balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userBalance = (Long) dataSnapshot.getValue();
                Log.i(TAG, "onDataChange: " + "my balance is " + userBalance);
                TextView accountbalance = (TextView)findViewById(R.id.account_balance);
                accountbalance.setText(String.valueOf(userBalance));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button gotomain = (Button)findViewById(R.id.go_to_main);
        gotomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button gototransactions = (Button)findViewById(R.id.go_to_transactions);
        gototransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, ViewTransactionsActivity.class);
                startActivity(intent);

            }
        });
        Button topup = (Button) findViewById(R.id.topup_button);
        topup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newBalance = userBalance + 10;
                myRef.child("users").child(fuser.getUid()).child("balance").setValue(newBalance);

            }
        });



        String email = fuser.getEmail();
        TextView accountemail = (TextView)findViewById(R.id.account_email);
        accountemail.setText(email);


    }

}
