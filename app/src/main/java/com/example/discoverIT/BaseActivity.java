package com.example.discoverIT;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.discoverIT.Constants.Constants;
import com.example.discoverIT.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import javax.annotation.Nullable;

public class BaseActivity extends AppCompatActivity {

    private DocumentReference documentReference;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://discoverit-b459f-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference myRef = database.getReference("Utenti/"+uid+"/availability");

    protected void onCreate(@Nullable Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        setAvailability(new FirebaseCallback() {
            @Override
            public void onCallback(String infos) {
                //DONE
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constants.KEY_AVAILABILITY = "0";
        myRef.setValue(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.KEY_AVAILABILITY = "1";
        myRef.setValue(1);
    }

    private void setAvailability(FirebaseCallback firebaseCallback) {
        myRef.setValue(0);
    }

    private interface FirebaseCallback {
        void onCallback(String infos);
    }

}
