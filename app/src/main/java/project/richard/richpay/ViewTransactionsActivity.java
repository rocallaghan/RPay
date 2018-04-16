package project.richard.richpay;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ViewTransactionsActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();;
    private DatabaseReference mDatabase;
    private String allTransId;
    private String allTransInfo;
    private String allTransAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transactions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference myRef = database.getReference();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fuser = firebaseAuth.getCurrentUser();
                if (fuser == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(ViewTransactionsActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        myRef.child("users").child(fuser.getUid()).child("transactions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Long currentAmount = ds.child("amount").getValue(Long.class);
                    String scurrentamount = Long.toString(currentAmount);
                    String currentdescription = ds.child("transInfo").getValue(String.class);
                    String currentdate = ds.child("transDate").getValue(String.class);
                    CreateTable(currentdescription, scurrentamount, currentdate);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        Button gotoaccount = (Button)findViewById(R.id.go_to_account);
        gotoaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTransactionsActivity.this, AccountActivity.class);
                startActivity(intent);

            }
        });


        Button gotomain = (Button)findViewById(R.id.go_to_main);
        gotomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewTransactionsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button addtrans = (Button) findViewById(R.id.test_trans);
        addtrans.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String uniqueID = UUID.randomUUID().toString();
                String today = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
                String newchild = today + uniqueID;
                Transaction trans = new Transaction(uniqueID, 20, "second added", today);
                myRef.child("users").child(fuser.getUid()).child("transactions").child(newchild).setValue(trans);

            }
        });

        myRef.child("users").child(fuser.getUid()).child("balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long userBalance = (Long) dataSnapshot.getValue();
                TextView balancetest = (TextView) findViewById(R.id.total_balance);
                balancetest.setText("Current Balance: " + userBalance);;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void CreateTable(String a, String b, String c){
            int count = 0;
            TableRow tr = new TableRow(this);
            tr.setBackgroundColor(Color.GRAY);
            tr.setId(100 + count);
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            TableLayout t1;
            TableLayout tl = (TableLayout) findViewById(R.id.main_table);
            TextView labelinfo = new TextView(this);
            labelinfo.setId(200 + count);
            labelinfo.setText(a + " ");
            labelinfo.setPadding(2, 0, 5, 0);
            labelinfo.setTextColor(Color.WHITE);
            tr.addView(labelinfo);
            TextView labelamount = new TextView(this);
            labelamount.setId(200 + count);
            labelamount.setText("€"+b);
            labelamount.setTextColor(Color.WHITE);
            tr.addView(labelamount);
        TextView labeldate = new TextView(this);
        labeldate.setId(200 + count);
        labeldate.setText(c);
        labeldate.setTextColor(Color.WHITE);
        tr.addView(labeldate);

// finally add this to the table row
            tl.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            count++;
    }
}
