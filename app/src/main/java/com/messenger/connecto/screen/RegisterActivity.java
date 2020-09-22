package com.messenger.connecto.screen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.messenger.connecto.R;

public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void to_login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        startActivity(intent);
    }


}