package com.example.discoverIT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.discoverIT.Constants.Constants;
import com.example.discoverIT.adapters.UserAdapter;
import com.example.discoverIT.databinding.ActivityUsersBinding;
import com.example.discoverIT.listeners.UserListener;
import com.example.discoverIT.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends BaseActivity implements UserListener {

    private ActivityUsersBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://discoverit-b459f-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private AppCompatImageView back;
    private String name;
    private String email;
    private String token;
    private String image;
    List<User> users = new ArrayList<>();
    List<String> uids = new ArrayList<>();
    private int i = 0;
    private DatabaseReference firebaseRootRef;
    private DatabaseReference itemRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getUsers(new FirebaseCallback() {
            @Override
            public void onCallback(List<User> us) {
                getUser();
            }
        });

        back = (AppCompatImageView) findViewById(R.id.imageBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UsersActivity.this, ChatActivity.class));
            }
        });

    }

    private void getUser() {
        UserAdapter userAdapter = new UserAdapter(users, this);
        binding.usersRecycleView.setAdapter(userAdapter);
        binding.usersRecycleView.setVisibility(View.VISIBLE);
    }

    private void getUsers(FirebaseCallback firebaseCallback) {
        loading(true);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseRootRef = firebaseDatabase.getReference();
        itemRef = firebaseRootRef.child("Utenti");
        Log.d("DATABASE", "fuori ondatachange");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DATABASE", "Dentro ondatachange");

                for(DataSnapshot ds : snapshot.getChildren()) {

                    String id = ds.getKey();
                    if(id.equals(uid)) {
                        //SAME USER
                    } else {
                        User user = new User();

                        String nome = ds.child("name").getValue(String.class);
                        String mail = ds.child("email").getValue(String.class);
                        String tok = ds.child("token").getValue(String.class);
                        String pic = ds.child("photo").getValue(String.class);


                        Log.d("DATABASE", "ID: "+id);

                        user.name = nome;
                        user.email = mail;
                        user.token = tok;
                        user.image = pic;
                        user.id = id;

                        users.add(user);
                    }
                }
                firebaseCallback.onCallback(users);
                loading(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        itemRef.addListenerForSingleValueEvent(valueEventListener);
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
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatsActivity.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }

    private interface FirebaseCallback {
        void onCallback(List<User> us);
    }

    private interface FirebaseImageCallback {
        void onImageCallback(Bitmap image);
    }
}