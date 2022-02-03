package com.example.discoverIT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.L;
import com.example.discoverIT.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class QuizResults extends AppCompatActivity {

    private TextView score;
    private TextView final_score;
    private TextView maxscorereached;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://discoverit-b459f-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference myRef;
    private long num_disco = 0;
    private long num_score = 0;
    private String founds;
    private DatabaseReference firebaseRootRef;
    private DatabaseReference itemRef;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_results);

        //METTERE UN BLOCCO SE SI ARRIVA DA HOMEACTIVITY
        final String getFoundedDisco = getIntent().getStringExtra("topicname");
        Log.d("DATABASE", "parentttttttttttttttttttttt: "+getFoundedDisco);
        if (getFoundedDisco==null || getFoundedDisco == "") {
            Log.d("DATABASE", "ENTRATO");
            startActivity(new Intent(QuizResults.this, Ranking.class));
            finish();
        } else {
            //CONTINUE
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
                            startActivity(new Intent(QuizResults.this, HomeActivity.class));
                            finish();
                        }
                    } catch (Exception n){
                        myRef = database.getReference("Utenti/"+uid+"/DiscoFound/"+getFoundedDisco);
                        myRef.setValue(getFoundedDisco);
                        Log.d("DATABASE", "Non trovato: "+n);
                    }
                }
            }, getFoundedDisco);


            final TextView correctAnswers = findViewById(R.id.correctAnswers);
            final TextView incorrectAnswers = findViewById(R.id.incorrectAnswers);
            final Button startNewQuizBtn = findViewById(R.id.startNewQuizButton);

            // Get Correct and Incorrect answers from MainActivity.class via intent
            final int getCorrectAnswers = getIntent().getIntExtra("correct",0);
            final int getInCorrectAnswers = getIntent().getIntExtra("incorrect",0);

            score = (TextView) findViewById(R.id.score);
            final_score = (TextView) findViewById(R.id.Final_score);
            maxscorereached = (TextView) findViewById(R.id.maxscoreached);

            // set correct and incorrect answers to TextViews
            correctAnswers.setText("Correct Answers : "+getCorrectAnswers);
            incorrectAnswers.setText("Incorrect Answers : "+getInCorrectAnswers);

            long delta, maxPunteggioQuiz,points, MaxScore,Final_score;
            delta = (long) ((getCorrectAnswers*1.5) - (getInCorrectAnswers*0.25));
            maxPunteggioQuiz = (long) ((getCorrectAnswers+getInCorrectAnswers)*1.5);
            MaxScore = 3;
            points = (delta*MaxScore)/maxPunteggioQuiz;
            score.setText("Quiz Score: "+String.valueOf(points));
            Final_score = points + 1;
            final_score.setText("Final Score: "+Final_score);

            getDisco(new FirebasediscoCallback() {
                @Override
                public void onCallback(long[] disco) {
                    myRef = database.getReference("Utenti/"+uid+"/disco");
                    num_disco = disco[0];
                    num_disco = num_disco + 1;
                    Log.d("DATABASE", "num_discoooooooooooooooooooo: "+num_disco);
                    myRef.setValue(num_disco);
                }
            });

            getScore(new FirebasescoreCallback() {
                @Override
                public void onCallback(long[] score) {
                    myRef = database.getReference("Utenti/"+uid+"/score");
                    num_score = score[0];
                    num_score = num_score + Final_score;
                    Log.d("DATABASE", "num_score: "+num_score);
                    myRef.setValue(num_score);
                }
            });

            if(Final_score==4){
                maxscorereached.setVisibility(View.VISIBLE);
            }

            startNewQuizBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(QuizResults.this, Ranking.class));
                    finish();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(QuizResults.this, Ranking.class));
        finish();
    }
    
    private void getDisco(FirebasediscoCallback firebasediscoCallback) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseRootRef = firebaseDatabase.getReference();
        long[] info = new long[2];
        itemRef = firebaseRootRef.child("Utenti/"+uid);
        Log.d("DATABASE", "fuori ondatachange");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DATABASE", "Dentro ondatachange");

                long discos = snapshot.child("disco").getValue(Long.class);

                info[0] = discos;
                
                firebasediscoCallback.onCallback(info);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        itemRef.addListenerForSingleValueEvent(valueEventListener);
    }
    
    private void getScore(FirebasescoreCallback firebasescoreCallback) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseRootRef = firebaseDatabase.getReference();
        long[] info = new long[2];
        itemRef = firebaseRootRef.child("Utenti/"+uid);
        Log.d("DATABASE", "fuori ondatachange");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DATABASE", "Dentro ondatachange");

                long scores = snapshot.child("score").getValue(Long.class);

                info[0] = scores;

                firebasescoreCallback.onCallback(info);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        itemRef.addListenerForSingleValueEvent(valueEventListener);
    }

    private void getFounded(QuizResults.FirebaseFoundCallback firebasediscoCallback, String t) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseRootRef = firebaseDatabase.getReference();
        String[] info = new String[2];
        itemRef = firebaseRootRef.child("Utenti/"+uid+"/DiscoFound");
        Log.d("DATABASE", "fuori ondatachange: "+t);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Log.d("DATABASE", "Dentro ondatachange");

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

    private interface FirebasediscoCallback {
        void onCallback(long[] disco);
    }

    private interface FirebasescoreCallback {
        void onCallback(long[] score);
    }

    private interface FirebaseFoundCallback {
        void onCallback(String[] founded);
    }
    
}