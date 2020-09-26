package com.messenger.connecto.screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.messenger.connecto.R;

import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {

    Button login_btn, submit_btn;
    EditText email_id, password;
    FirebaseAuth firebaseAuth;
    DatabaseReference database_user;
    private SpotsDialog progressDialog;
    TextView  forgot_pass_textView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //findViewBy IDs
        login_btn = findViewById(R.id.login_btn);
        email_id = findViewById(R.id.editTextTextEmailAddress);

        password = findViewById(R.id.editTextTextPassword);

        forgot_pass_textView = findViewById(R.id.forgot_pass_textView);
        submit_btn = findViewById(R.id.submit_btn);


        firebaseAuth = FirebaseAuth.getInstance();
        database_user = FirebaseDatabase.getInstance().getReference("users");
        progressDialog = new SpotsDialog(this, R.style.custom_progressDialog);
        sharedPreferences = getSharedPreferences("AIRNOTES_DATA", MODE_PRIVATE);
        editor = sharedPreferences.edit();

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


    //Login onClick : Email verification
    public void login_now(View view) {
        String userMailId = email_id.getText().toString();
        String userPassword = password.getText().toString();


        if (!userMailId.equals("")) {
            if (!userPassword.equals("")) {
                loginUser(userMailId, userPassword);
            } else {
                Toast.makeText(this, "Enter Your Password", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Enter Your Email Address", Toast.LENGTH_SHORT).show();
        }
    }

    //Submit onClick : forgot password
    public void reset_password(View view) {
        String after_forgot_mailId = email_id.getText().toString();

        if (!after_forgot_mailId.equals("")) {
            onPreExecute();
            firebaseAuth.sendPasswordResetEmail(after_forgot_mailId).addOnCompleteListener(new OnCompleteListener<Void>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        onPostExecute();
                        Toast.makeText(LoginActivity.this, "Check your emails to Reset Your Password", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,LoginActivity.class);
                        overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                        startActivity(intent);
                        finish();
                    } else if (task.isCanceled()) {
                        onPostExecute();
                        Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Cancelled", Objects.requireNonNull(task.getException().getLocalizedMessage()));
                        Intent intent = new Intent(LoginActivity.this,LoginActivity.class);
                        overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                        startActivity(intent);
                        finish();
                    } else {
                        onPostExecute();
                        Toast.makeText(LoginActivity.this, "This Email Id isn't present in the Database", Toast.LENGTH_SHORT).show();
                        Log.e("Error", Objects.requireNonNull(Objects.requireNonNull(task.getException()).getLocalizedMessage()));
                        Intent intent = new Intent(LoginActivity.this,LoginActivity.class);
                        overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Please enter valid Email", Toast.LENGTH_SHORT).show();
        }
    }

    //login using email and password
    private void loginUser(String userMailId, String userPassword) {
        onPreExecute();
        firebaseAuth.signInWithEmailAndPassword(userMailId, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    editor.putBoolean("LOGIN_STATUS",true);
                    editor.commit();
                    onPostExecute();
                    Toast.makeText(LoginActivity.this, "Logged In Successfully. ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, SlideNavigatorActivity.class);
                    overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                    startActivity(intent);
                    finish();
                } else {
                    editor.putBoolean("LOGIN_STATUS",false);
                    editor.commit();
                    onPostExecute();
                    Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Changing layout for forget password textView
    public void get_password(View view) {
        forgot_pass_textView.setVisibility(View.VISIBLE);
        submit_btn.setVisibility(View.VISIBLE);

        password.setVisibility(View.GONE);
        forgot_pass_textView.setVisibility(View.GONE);
        login_btn.setVisibility(View.GONE);
    }

 
    //Am using it in an AsyncTask. So in  my onPreExecute, I do this:
    public void onPreExecute() {
        progressDialog.show();
    }

    //dismiss in onPostExecute
    public void onPostExecute() {
        progressDialog.dismiss();
    }
}
