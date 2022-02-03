package com.example.discoverIT;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Base64;

public class AccountInfoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://discoverit-b459f-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    Button saveAccountInfo;
    Button backtoProfile;
    EditText nome;
    TextView email;
    TextView name;
    TextView address;
    TextView city;
    ImageView profilepic;
    private String TAG = "AccountInfoActivity";
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinfo);

        name = (TextView) findViewById(R.id.textView14);
        email = (TextView) findViewById(R.id.textView15);
        address = (TextView) findViewById(R.id.button4);
        city = (TextView) findViewById(R.id.button3);

        myRef = database.getReference("Utenti/"+uid+"/name");
        nome = (EditText) findViewById(R.id.nome);
        email.setText(mAuth.getCurrentUser().getEmail());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                name.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AccountInfoActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                nome.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AccountInfoActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        myRef = database.getReference("Utenti/"+uid+"/phone");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                address.setText(value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AccountInfoActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        myRef = database.getReference("Utenti/"+uid+"/city");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if (value.equals(" ")){
                    city.setText("");
                } else {
                    city.setText(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AccountInfoActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        saveAccountInfo = (Button) findViewById(R.id.button6);
        saveAccountInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "uid:"+uid);
                if(name.getText()!=null){
                    String name = nome.getText().toString();
                    myRef = database.getReference("Utenti/"+uid+"/name");
                    myRef.setValue(name);
                }
                if(address.getText()!=null){
                    String indirizzo = address.getText().toString();
                    myRef = database.getReference("Utenti/"+uid+"/phone");
                    myRef.setValue(indirizzo);
                }
                if(city.getText()!=null){
                    String citta = city.getText().toString();
                    myRef = database.getReference("Utenti/"+uid+"/city");
                    myRef.setValue(citta.trim().toUpperCase());
                }
                startActivity(new Intent(AccountInfoActivity.this, HomeActivity.class));
            }
        });

        profilepic = findViewById(R.id.imageView10);

        myRef = database.getReference("Utenti/"+uid+"/photo");
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
                Toast.makeText(AccountInfoActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        backtoProfile = (Button) findViewById(R.id.button9);
        backtoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountInfoActivity.this, ProfileActivity.class));
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.getMimeDecoder().decode(encodedImage);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
