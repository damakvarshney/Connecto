package com.messenger.connecto.screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.messenger.connecto.R;

public class SplashActivity extends AppCompatActivity {

    ImageView sliding_logo;
    Animation animSlide;
    private Handler hdlr = new Handler();
    SharedPreferences sharedPreferences;
    private final int SPLASH_DISPLAY_LENGTH = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Refer the ImageView like this
        sliding_logo = (ImageView) findViewById(R.id.sliding_logo);

        // Load the animation like this
        animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.left_to_right);

        // Start the animation like this
        sliding_logo.startAnimation(animSlide);

        sharedPreferences = getSharedPreferences("AIRNOTES_DATA", MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
            Intent intent = new Intent(SplashActivity.this,RegisterActivity.class);
                overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
            startActivity(intent);
            finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}