package com.example.discoverIT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.discoverIT.Constants.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Base64;

public class UserInfo extends AppCompatActivity {

    private DatabaseReference firebaseRootRef;
    private DatabaseReference itemRef;
    private String nome;
    private String mail;
    private String photo;
    Button backtohome;
    TextView email;
    TextView name;
    ImageView profilepic;
    Uri imageUri;
    private String encodedImage;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://discoverit-b459f-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference myRef;
    TextView km;
    TextView score;
    TextView disco;
    private String TAG = "UserInfoActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        //ID utente da visualizzare
        Log.d("DATABASE", "SenderID: "+ Constants.KEY_USER_INFO);

        //richiesta db per i dati
        loadInfo(new FirebaseCallback() {
            @Override
            public void onCallback(String[] informations) {
                nome = informations[0];
                photo = informations[1];
                mail = informations[2];

                Log.d("DATABASE", "NAME: "+nome);
                Log.d("DATABASE", "EMAIL: "+mail);

                name = findViewById(R.id.textView14);
                email = findViewById(R.id.textView15);
                email.setText(mail);
                name.setText(nome);
            }
        });

        score = (TextView) findViewById(R.id.numpoints);
        disco = (TextView) findViewById(R.id.disco);
        myRef = database.getReference("Utenti/"+Constants.KEY_USER_INFO+"/disco");
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
                Toast.makeText(UserInfo.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        myRef = database.getReference("Utenti/"+Constants.KEY_USER_INFO+"/score");
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
                Toast.makeText(UserInfo.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        backtohome = (Button) findViewById(R.id.button9);
        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfo.this, HomeActivity.class));
            }
        });

        profilepic = findViewById(R.id.imageView10);

        myRef = database.getReference("Utenti/"+Constants.KEY_USER_INFO+"/photo");
        myRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                encodedImage = value;
                profilepic.setImageBitmap(getUserImage(encodedImage.trim()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserInfo.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void loadInfo(FirebaseCallback firebaseCallback) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseRootRef = firebaseDatabase.getReference();
        String[] infos = new String[3];
        itemRef = firebaseRootRef.child("Utenti/"+Constants.KEY_USER_INFO);
        Log.d("DATABASE", "fuori ondatachange");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("DATABASE", "Dentro ondatachange");

                String nome = snapshot.child("name").getValue(String.class);
                String pic = snapshot.child("photo").getValue(String.class);
                String mail = snapshot.child("email").getValue(String.class);

                infos[0]=nome;
                infos[1]=pic;
                infos[2]=mail;
                firebaseCallback.onCallback(infos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        itemRef.addValueEventListener(valueEventListener);
    }

    private interface FirebaseCallback {
        void onCallback(String[] informations);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.getMimeDecoder().decode(encodedImage);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}