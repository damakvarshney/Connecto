package com.messenger.connecto.screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.messenger.connecto.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView textView = findViewById(R.id.sign_up);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
                startActivity(intent);
            }
        });


    }

    public void to_sign_up(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        Log.d("LoginActivity", "ANYTHING");
        startActivity(intent);
    }

    public void to_home(View view) {
        Intent intent = new Intent(this,SlideNavigatorActivity.class);
        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
        startActivity(intent);
    }
}