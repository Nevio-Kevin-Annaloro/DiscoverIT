package com.example.discoverIT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizResultFailed extends AppCompatActivity {

    private Button backhome;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://discoverit-b459f-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference myRef;
    private String founds;
    private DatabaseReference firebaseRootRef;
    private DatabaseReference itemRef;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result_failed);

        // Get Correct and Incorrect answers from MainActivity.class via intent
        final String getFoundedDisco = getIntent().getStringExtra("topicname");

        Log.d("DATABASE", "TOPIC FAILED: "+getFoundedDisco);

        getFounded(new FirebaseFoundCallback() {
            @Override
            public void onCallback(String[] disco) {
                try {
                    founds = disco[0];
                    Log.d("DATABASE", "Founds: "+founds);
                    if (founds.equals("") || founds.equals(" ") || founds.equals(null)) {
                        Log.d("DATABASE", "Non trovato");
                    } else {
                        Log.d("DATABASE", "Trovato");
                    }
                } catch (Exception n){
                    myRef = database.getReference("Utenti/"+uid+"/DiscoFound/"+getFoundedDisco);
                    myRef.setValue(getFoundedDisco);
                    Log.d("DATABASE", "Non trovato: "+n);
                }
            }
        }, getFoundedDisco);

        backhome = (Button) findViewById(R.id.backhome);
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuizResultFailed.this, HomeActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(QuizResultFailed.this, HomeActivity.class));
        finish();
    }

    private void getFounded(QuizResultFailed.FirebaseFoundCallback firebasediscoCallback, String t) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseRootRef = firebaseDatabase.getReference();
        String[] info = new String[2];
        itemRef = firebaseRootRef.child("Utenti/"+uid+"/DiscoFound");
        Log.d("DATABASE", "fuori ondatachange: "+t);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DATABASE", "Dentro ondatachange");

                String discos = snapshot.child(t).getValue(String.class);

                info[0] = discos;

                firebasediscoCallback.onCallback(info);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        itemRef.addListenerForSingleValueEvent(valueEventListener);
    }

    private interface FirebaseFoundCallback {
        void onCallback(String[] founded);
    }

}