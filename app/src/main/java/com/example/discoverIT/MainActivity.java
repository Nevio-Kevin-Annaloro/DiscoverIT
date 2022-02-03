package com.example.discoverIT;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.discoverIT.Constants.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FloatingActionButton bttlogin = null;
    private EditText etmaillogin = null;
    private EditText etpasslogin = null;
    private String TAG = "SigninActivity";
    public PreferenceManager preferenceManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main);

        /*if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }*/

        bttlogin = (FloatingActionButton) findViewById(R.id.bttlogin);
        etmaillogin = (EditText) findViewById(R.id.etemaillogin);
        etpasslogin = (EditText) findViewById(R.id.etpasslogin);
        bttlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });
    }

    private void updateUI(FirebaseUser user) {}

    private void performLogin () {
        String email = etmaillogin.getText().toString().trim();
        String pass = etpasslogin.getText().toString().trim();
        if(email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this,"Compila tutti i campi",Toast.LENGTH_LONG).show();
            return;
        }
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "login:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            //preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                            //preferenceManager.putString(Constants.KEY_USER_ID, user.getUid());
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        } else {
                            Log.w(TAG, "login:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void OpenSignupPage(View view) {
        startActivity(new Intent(MainActivity.this,SignupActivity.class));
    }

    public void OpenResetPassword(View view) {
        startActivity(new Intent(getApplicationContext(),ResetPasswordActivity.class));
    }
}
