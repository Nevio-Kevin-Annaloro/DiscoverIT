package com.example.discoverIT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

public class ScanActivity extends AppCompatActivity {

    private Button back;
    private CodeScanner mCodeScanner;
    private String TAG = "ScanActivity";
    private String scan;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://discoverit-b459f-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference myRef;
    private String selectedQrCode = "";
    private String founds;
    private DatabaseReference firebaseRootRef;
    private DatabaseReference itemRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScanActivity.this, MapsActivity.class));
            }
        });

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(ScanActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        scan = result.getText();
                        Log.d(TAG, "risultato codice qrcode: "+scan);
                        myRef = database.getReference("QrCode/Alessandria/"+scan);
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String value = snapshot.getValue(String.class);
                                if(value!=null){
                                    Log.d(TAG, "Codice trovato");
                                    selectedQrCode = value;
                                    if(selectedQrCode.isEmpty()){
                                        Toast.makeText(ScanActivity.this, "Please scan a valid Qr-code", Toast.LENGTH_SHORT).show();
                                    }else {
                                        getFounded(new FirebaseFoundCallback() {
                                            @Override
                                            public void onCallback(String[] disco) {
                                                try {
                                                    founds = disco[0];
                                                    Log.d("DATABASE", "Founds: "+founds);
                                                    if (founds.equals("") || founds.equals(" ") || founds.equals(null)) {
                                                        Log.d("DATABASE", "Non trovato");
                                                        //Toast.makeText(ScanActivity.this, "DISCO NOT FOUND!",Toast.LENGTH_LONG).show();

                                                        // Create an Object of Intent to open quiz questions screen
                                                        final Intent intent = new Intent(ScanActivity.this, StoriaPreQuizActivity.class);

                                                        //put user entered name and selected topic name to intent for use in next activity
                                                        intent.putExtra("selectedTopic", selectedQrCode);

                                                        // call startActivity to open next activity along with data(userName, selectedTopicName)
                                                        startActivity(intent);

                                                        finish(); // finish (destroy) this activity
                                                    } else {
                                                        Log.d("DATABASE", "Trovato");
                                                        Toast.makeText(ScanActivity.this, "DISCO ALREADY FOUND!",Toast.LENGTH_LONG).show();
                                                        showErrorDialog();
                                                    }
                                                } catch (Exception n){
                                                    Log.d("DATABASE", "Non trovato: "+n);

                                                    // Create an Object of Intent to open quiz questions screen
                                                    final Intent intent = new Intent(ScanActivity.this, StoriaPreQuizActivity.class);

                                                    //put user entered name and selected topic name to intent for use in next activity
                                                    intent.putExtra("selectedTopic", selectedQrCode);

                                                    // call startActivity to open next activity along with data(userName, selectedTopicName)
                                                    startActivity(intent);

                                                    finish(); // finish (destroy) this activity
                                                }
                                            }
                                        }, selectedQrCode);
                                    }
                                }else {
                                    Log.d(TAG, "Codice non trovato");
                                    myRef = database.getReference("QrCode/AcquiTerme/"+scan);
                                    myRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String value = snapshot.getValue(String.class);
                                            if(value!=null){
                                                Log.d(TAG, "Codice trovato");
                                                selectedQrCode = value;
                                                if(selectedQrCode.isEmpty()){
                                                    Toast.makeText(ScanActivity.this, "Please scan a valid Qr-code", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    getFounded(new FirebaseFoundCallback() {
                                                        @Override
                                                        public void onCallback(String[] disco) {
                                                            try {
                                                                founds = disco[0];
                                                                Log.d("DATABASE", "Founds: "+founds);
                                                                if (founds.equals("") || founds.equals(" ") || founds.equals(null)) {
                                                                    Log.d("DATABASE", "Non trovato");
                                                                    //Toast.makeText(ScanActivity.this, "DISCO NOT FOUND!",Toast.LENGTH_LONG).show();

                                                                    // Create an Object of Intent to open quiz questions screen
                                                                    final Intent intent = new Intent(ScanActivity.this, StoriaPreQuizActivity.class);

                                                                    //put user entered name and selected topic name to intent for use in next activity
                                                                    intent.putExtra("selectedTopic", selectedQrCode);

                                                                    // call startActivity to open next activity along with data(userName, selectedTopicName)
                                                                    startActivity(intent);

                                                                    finish(); // finish (destroy) this activity
                                                                } else {
                                                                    Log.d("DATABASE", "Trovato");
                                                                    Toast.makeText(ScanActivity.this, "DISCO ALREADY FOUND!",Toast.LENGTH_LONG).show();
                                                                    showErrorDialog();
                                                                }
                                                            } catch (Exception n){
                                                                Log.d("DATABASE", "Non trovato: "+n);

                                                                // Create an Object of Intent to open quiz questions screen
                                                                final Intent intent = new Intent(ScanActivity.this, StoriaPreQuizActivity.class);

                                                                //put user entered name and selected topic name to intent for use in next activity
                                                                intent.putExtra("selectedTopic", selectedQrCode);

                                                                // call startActivity to open next activity along with data(userName, selectedTopicName)
                                                                startActivity(intent);

                                                                finish(); // finish (destroy) this activity
                                                            }
                                                        }
                                                    }, selectedQrCode);
                                                }
                                            }else
                                                Log.d(TAG, "Codice non trovato");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(ScanActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(ScanActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(ScanActivity.this).inflate(
                R.layout.layout_error_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Disco already found!");
        ((TextView) view.findViewById(R.id.textMessage)).setText("You already used that disco! \nYou are authorized to use it only one time");
        ((Button) view.findViewById(R.id.buttonAction)).setText("I UNDERSTAND");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_info);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                startActivity(new Intent(ScanActivity.this, HomeActivity.class));
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void getFounded(ScanActivity.FirebaseFoundCallback firebasediscoCallback, String t) {
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