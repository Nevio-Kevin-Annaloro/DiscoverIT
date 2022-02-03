package com.example.discoverIT;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.discoverIT.Constants.Constants;
import com.example.discoverIT.adapters.RankingAdapter;
import com.example.discoverIT.databinding.ActivityRankingBinding;
import com.example.discoverIT.listeners.RankingListener;
import com.example.discoverIT.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ranking extends BaseActivity implements RankingListener {

    private ActivityRankingBinding binding;
    private Button back;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://discoverit-b459f-default-rtdb.europe-west1.firebasedatabase.app/");
    List<User> users = new ArrayList<>();
    List<User> userss = new ArrayList<>();
    float scores = 0;
    private int i = 0;
    private DatabaseReference firebaseRootRef;
    private DatabaseReference itemRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRankingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getUsers(new Ranking.FirebaseCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCallback(List<User> us2) {
                getUser();
            }
        });

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Ranking.this, HomeActivity.class));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getUser() {

        for (int i=0;i<users.size();i++) {
            Log.d("DATABASE", "BEFORE: "+users.get(i).id);
        }

        Collections.sort(users);
        Collections.reverse(users);

        RankingAdapter rankAdapter = new RankingAdapter(users, this);
        binding.conversastionsRecyclerView.setAdapter(rankAdapter);
        binding.conversastionsRecyclerView.setVisibility(View.VISIBLE);

        for (int i=0;i<users.size();i++) {
            Log.d("DATABASE", "AFTER: "+users.get(i).id);
        }

    }

    private void getUsers(Ranking.FirebaseCallback firebaseCallback) {
        loading(true);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseRootRef = firebaseDatabase.getReference();
        itemRef = firebaseRootRef.child("Utenti");
        Log.d("DATABASE", "fuori ondatachange");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                Log.d("DATABASE", "Dentro ondatachange");

                for(DataSnapshot ds : snapshot.getChildren()) {

                    String id = ds.getKey();
                    User user = new User();

                    String nome = ds.child("name").getValue(String.class);
                    String mail = ds.child("email").getValue(String.class);
                    String tok = ds.child("token").getValue(String.class);
                    String pic = ds.child("photo").getValue(String.class);
                    Long score = ds.child("score").getValue(Long.class);

                    scores = Float.parseFloat(String.valueOf(score));
                    Log.d("DATABASE", "SCORE: "+scores+" di: "+nome);

                    Log.d("DATABASE", "ID: "+id);

                    user.name = nome;
                    user.email = mail;
                    user.token = tok;
                    user.image = pic;
                    user.id = id;
                    user.score = score;

                    users.add(user);
                }
                firebaseCallback.onCallback(users);
                loading(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        itemRef.addValueEventListener(valueEventListener);
    }

    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s", "No user available" ));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
        //binding.progresBar.setVisibility(View.GONE);
    }

    private void loading(Boolean isLoading) {
        if(isLoading) {
            binding.progresBar.setVisibility(View.VISIBLE);
        } else {
            binding.progresBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRankingClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatsActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }

    private interface FirebaseCallback {
        void onCallback(List<User> us);
    }

}