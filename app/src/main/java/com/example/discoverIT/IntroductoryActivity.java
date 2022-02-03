package com.example.discoverIT;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import com.airbnb.lottie.LottieAnimationView;

public class IntroductoryActivity extends AppCompatActivity {

    public static int SPLASH_SCREEN = 4000;
    ImageView logo;
    ImageView appName;
    ImageView splashImg;
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introductory);

        logo = findViewById(R.id.logo);
        appName = findViewById(R.id.app_name);
        splashImg = findViewById(R.id.img);
        lottieAnimationView = findViewById(R.id.lottie);

        splashImg.animate().translationY(-2800).setDuration(1000).setStartDelay(3000);
        logo.animate().translationY(2400).setDuration(1000).setStartDelay(3000);
        appName.animate().translationY(2400).setDuration(1000).setStartDelay(3000);
        lottieAnimationView.animate().translationY(2400).setDuration(1000).setStartDelay(3000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroductoryActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);

    }
}