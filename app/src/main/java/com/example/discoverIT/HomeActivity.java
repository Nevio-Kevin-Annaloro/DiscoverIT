package com.example.discoverIT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.discoverIT.Constants.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private String uid = user.getUid();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://discoverit-b459f-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ImageView profile;
    private Button maps;
    private Button stat;
    private Button discover;
    private Button help;
    private String city;
    private TextView chat;
    private String TAG = "HomeActivity";
    private String token = "";
    private int CAMERA_PERMISSION_CODE = 1;
    private int POSITION_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(HomeActivity.this, "You have already granted this permission!",
                    //Toast.LENGTH_SHORT).show();
        } else {
            requestCameraPermission();
        }

        if (ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(HomeActivity.this, "You have already granted this permission!",
                    //Toast.LENGTH_SHORT).show();
        } else {
            requestPositionPermission();
        }

        myRef = database.getReference("Utenti/"+uid+"/name");

        profile = (ImageView) findViewById(R.id.imageView2);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
            }
        });

        help = (Button) findViewById(R.id.button);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openmaps();
            }
        });

        stat = (Button) findViewById(R.id.rank);
        stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, Ranking.class));
            }
        });

        chat = (TextView) findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ChatActivity.class));
            }
        });

        discover = (Button) findViewById(R.id.discover);
        city = null;
        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef = database.getReference("Utenti/"+uid+"/city");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = snapshot.getValue(String.class);
                        if(value==null) {
                            Toast.makeText(HomeActivity.this, "Per procedere alla mappa è necessario inserire inserire la propria città!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(HomeActivity.this, AccountInfoActivity.class));
                        }
                        city = Objects.requireNonNull(value.trim());
                        if(city==null || city.equals("") || city.equals(" ")){
                            Toast.makeText(HomeActivity.this, "Per procedere alla mappa è necessario inserire inserire la propria città!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(HomeActivity.this, AccountInfoActivity.class));
                        }else if(city.equals("ALESSANDRIA") || city.equals("ACQUI TERME")){
                            //Toast.makeText(HomeActivity.this, "Ottimo! Nella tua città sono presenti dei disco!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(HomeActivity.this, MapsActivity.class));
                        }else{
                            Toast.makeText(HomeActivity.this, "Nella tua città purtroppo non ci sono ancora disco! Puoi comunque guardare la mappa", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(HomeActivity.this, MapsActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HomeActivity.this, "Fail to get your city.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);

        myRef = database.getReference("Utenti/"+uid+"/token");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                token = value;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

     }

    private void updateToken(String token) {
        Constants.KEY_FCM_TOKEN = token;
        myRef = database.getReference("Utenti/"+uid+"/token");
        Constants.TOKEN = token;
        myRef.setValue(String.valueOf(token));
    }

    private void openmaps(){
         startActivity(new Intent(HomeActivity.this, InfoActivity.class));
     }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            showWarningCameraDialog();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == POSITION_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestPositionPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            showWarningLocationDialog();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, POSITION_PERMISSION_CODE);
        }
    }

    @Override
    public void onBackPressed() {
        myRef = database.getReference("Utenti/"+uid+"/token");
        myRef.setValue("");
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    private void showWarningLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(HomeActivity.this).inflate(
                R.layout.layout_warning_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Position permission needed");
        ((TextView) view.findViewById(R.id.textMessage)).setText("This permission is needed to track your position during the discovery of discos");
        ((Button) view.findViewById(R.id.buttonNo)).setText("CANCEL");
        ((Button) view.findViewById(R.id.buttonYes)).setText("OK");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_baseline_warning);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, POSITION_PERMISSION_CODE);
            }
        });

        view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    private void showWarningCameraDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(HomeActivity.this).inflate(
                R.layout.layout_warning_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle)).setText("Camera permission needed");
        ((TextView) view.findViewById(R.id.textMessage)).setText("This permission is needed to scan Qr-code attached to discos");
        ((Button) view.findViewById(R.id.buttonNo)).setText("CANCEL");
        ((Button) view.findViewById(R.id.buttonYes)).setText("OK");
        ((ImageView) view.findViewById(R.id.imageIcon)).setImageResource(R.drawable.ic_baseline_warning);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.buttonYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            }
        });

        view.findViewById(R.id.buttonNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }
}
