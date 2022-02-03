package com.example.discoverIT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +       //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");
    private FirebaseAuth mAuth;
    private FloatingActionButton bttsubmit = null;
    private EditText etname = null;
    private EditText etmail = null;
    private EditText etpass = null;
    private EditText etconfpass = null;
    private String TAG = "SignupActivity";
    private Button back;
    private RoundedImageView image;
    private Uri imageUri;
    private String encodedImage = "";
    private TextView addImage;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_signup);

        image = findViewById(R.id.imageProfile);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        addImage = findViewById(R.id.textAddImage);

        back = findViewById(R.id.button9);
        bttsubmit = (FloatingActionButton) findViewById(R.id.bttsubmit);
        etname = (EditText) findViewById(R.id.etname);
        etmail = (EditText) findViewById(R.id.etemail);
        etpass = (EditText) findViewById(R.id.etpass);
        etconfpass = (EditText) findViewById(R.id.etconfpass);
        bttsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
            }
        });
    }

    public void OpenSigninPage(View view) {
        startActivity(new Intent(SignupActivity.this,MainActivity.class));
    }

    private void updateUI(FirebaseUser user) {}

    private boolean validateUsername() {
        String usernameInput = etname.getText().toString().trim();

        if (usernameInput.isEmpty()) {
            etname.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 20) {
            etname.setError("Username too long");
            return false;
        } else {
            etname.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String emailInput = etmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            etmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            etmail.setError("Please enter a valid email address");
            return false;
        } else {
            etmail.setError(null);
            return true;
        }
    }

    private boolean valideImage(String encodedImages) {
        if (encodedImages.equals("")) {
            Toast.makeText(SignupActivity.this,"Please upload your image",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePassword(EditText pass) {
        String passwordInput = etpass.getText().toString().trim();
        String confpassInput = etconfpass.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            pass.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            pass.setError("Password should contain at least: 1 lower case letter, 1 upper case letter, 1 special character");
            return false;
        } else if (!passwordInput.equals(confpassInput)){
            Toast.makeText(SignupActivity.this,"Password not matching",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            pass.setError(null);
            return true;
        }
    }

    private String encodeImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight()*previewWidth/bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            try {
                Uri imageUri = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(bitmap);
                addImage.setVisibility(View.GONE);
                encodedImage = encodeImage(bitmap);
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private void performRegistration () {
        String name = etname.getText().toString();
        String email = etmail.getText().toString();
        String pass = etpass.getText().toString();
        String confpass = etconfpass.getText().toString();
        if(name.isEmpty() || email.isEmpty() || pass.isEmpty() || confpass.isEmpty()) {
            Toast.makeText(this,"Compila tutti i campi",Toast.LENGTH_LONG).show();
            return;
        }
        if(!validateEmail() || !validateUsername() || !validatePassword(etpass) || !validatePassword(etconfpass) || !valideImage(encodedImage)) {
            return;
        }
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,pass)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    long RESET = 0;
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    String uid = user.getUid();
                    updateUI(user);
                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://discoverit-b459f-default-rtdb.europe-west1.firebasedatabase.app/");
                    DatabaseReference myRef = database.getReference("Utenti/"+uid+"/name");
                    myRef.setValue(name);
                    myRef = database.getReference("Utenti/"+uid+"/email");
                    myRef.setValue(email);
                    myRef = database.getReference("Utenti/"+uid+"/photo");
                    myRef.setValue(encodedImage);
                    myRef = database.getReference("Utenti/"+uid+"/disco");
                    myRef.setValue(RESET);
                    myRef = database.getReference("Utenti/"+uid+"/score");
                    myRef.setValue(RESET);
                    myRef = database.getReference("Utenti/"+uid+"/city");
                    myRef.setValue(" ");

                    startActivity(new Intent(SignupActivity.this, HomeActivity.class));

                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
            });
    }
}