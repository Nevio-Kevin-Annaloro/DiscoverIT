package com.example.discoverIT;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class StatisticsActivity extends AppCompatActivity {

    Button statstoprofile;
    TextView score;
    TextView disco;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://discoverit-b459f-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference myRef;
    private String TAG = "StatsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        statstoprofile = (Button) findViewById(R.id.statstoprofile);
        statstoprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatisticsActivity.this,HomeActivity.class));
            }
        });

        score = (TextView) findViewById(R.id.numpoints);
        disco = (TextView) findViewById(R.id.disco);
        myRef = database.getReference("Utenti/"+uid+"/disco");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long value = snapshot.getValue(Long.class);
                Log.d(TAG, "valueeeeeeeeeeeeeeeeeeeeeeee: "+value);
                if(value == 0){
                    disco.setText("0");
                }else{
                    disco.setText(String.valueOf(value));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StatisticsActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        myRef = database.getReference("Utenti/"+uid+"/score");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long value = snapshot.getValue(Long.class);
                if(value == 0){
                    score.setText("0");
                }else{
                    score.setText(String.valueOf(value));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StatisticsActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}