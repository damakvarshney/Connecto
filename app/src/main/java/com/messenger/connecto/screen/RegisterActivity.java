package com.messenger.connecto.screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.messenger.connecto.R;
import com.messenger.connecto.screen.user.UserInfo;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    Button sign_up_btn, verify_btn;
    EditText email_id,password,mobile_no,name;
    FirebaseAuth firebaseAuth;
    DatabaseReference database_user;
    private SpotsDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView google_iv,phone_iv,connecto_iv;
    String  user_mobile_no;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationStateChangedCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        google_iv = findViewById(R.id.google_iv);
        phone_iv = findViewById(R.id.phone_iv);
        connecto_iv = findViewById(R.id.connecto_iv);
        email_id = findViewById(R.id.email_editText);
        password = findViewById(R.id.password_editText);
        mobile_no = findViewById(R.id.mobile_editText);
        name = findViewById(R.id.username_editText);
        sign_up_btn = findViewById(R.id.sign_up_btn);
        verify_btn = findViewById(R.id.verify_btn);
                

        firebaseAuth = FirebaseAuth.getInstance();
        database_user = FirebaseDatabase.getInstance().getReference("users");
        progressDialog = new SpotsDialog(this, R.style.custom_progressDialog);
        sharedPreferences = getSharedPreferences("AIRNOTES_DATA", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Callback for Mobile Verification
        verificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn_with_mobile(phoneAuthCredential);
                Log.e("2Callbacks", "onVerificationCompleted: CallBacks" );
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                onPostExecute();
                Toast.makeText(RegisterActivity.this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void phone_signUp(View view) {
        mobile_no.setVisibility(View.VISIBLE);
        connecto_iv.setVisibility(View.VISIBLE);
        google_iv.setVisibility(View.VISIBLE);
        verify_btn.setVisibility(View.VISIBLE);
        email_id.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        phone_iv.setVisibility(View.GONE);
        sign_up_btn.setVisibility(View.GONE);



    }

    public void google_signup(View view) {
    }

    public void connecto_signUp(View view) {

        mobile_no.setVisibility(View.GONE);
        connecto_iv.setVisibility(View.GONE);
        google_iv.setVisibility(View.VISIBLE);
        email_id.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        sign_up_btn.setVisibility(View.VISIBLE );
        phone_iv.setVisibility(View.VISIBLE);

    }
    //verify mobile number to send call back
    private void verify_mobile_number(String user_mobile_no) {
        onPreExecute();
        Log.e("1Verify", "verify_mobile_number: Here");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(user_mobile_no, 60, TimeUnit.SECONDS, this, verificationStateChangedCallbacks);

    }

    //onClick verify number entered
    public void verify_now(View view) {

        user_mobile_no = "+91" + mobile_no.getText().toString();
        Log.e("User Number", "verify_now:"+user_mobile_no );

        if (!mobile_no.getText().toString().equals("")) {
            verify_mobile_number(user_mobile_no);
        } else {
            Toast.makeText(this, "Please enter valid Mobile No.", Toast.LENGTH_SHORT).show();
        }
    }

    //onClick register method
    public void register_now(View view) {

        String username = name.getText().toString();
        String userMailId = email_id.getText().toString();
        String userPassword = password.getText().toString();
        String userPhoneNumber = "+91" + mobile_no.getText().toString();

        if (!username.equals("")) {
            if (!userMailId.equals("")) {
                if (!userPassword.equals("")) {
                    register(username, userMailId, userPassword, userPhoneNumber);
                } else {
                    Toast.makeText(this, "Enter Your Password", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Enter Your Email Address", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show();
        }
    }

    //register method to get user registered
    private void register(final String username, final String userMailId, final String userPassword, final String userPhoneNumber) {
        firebaseAuth.createUserWithEmailAndPassword(userMailId, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    onPreExecute();
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    final String uid = currentUser.getUid();

                    //using data class storing user to database
                    UserInfo userInfo = new UserInfo(username, userMailId, userPhoneNumber);

                    database_user.child(uid).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                editor.putString("user_id_from_email",uid);
                                onPostExecute();
                                Toast.makeText(RegisterActivity.this, "User is Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Failed to Create User", Toast.LENGTH_SHORT).show();
                                firebaseAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.e("Unsuccessful User", "Removed ", task.getException().getCause());
                                        Intent intent = new Intent(RegisterActivity.this, RegisterActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        }
                    });

                } else {
                    Log.e("Unsuccessful User", "Removed", task.getException().getCause());
                    Toast.makeText(RegisterActivity.this, "Already Registered", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    //register with mobile
    private void signIn_with_mobile(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    onPostExecute();
                    Log.e("3Done", "onComplete: " );
                    Toast.makeText(RegisterActivity.this, "Mobile number verified successfully", Toast.LENGTH_SHORT).show();
                    Intent intentWithData = new Intent(RegisterActivity.this, LinkMobileEmailActivity.class);
                    intentWithData.putExtra("MOBILE_NUMBER", user_mobile_no);
                    startActivity(intentWithData);
                    finish();

                } else {
                    onPostExecute();
                    Toast.makeText(RegisterActivity.this, "Mobile number verification failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //Am using it in an AsyncTask. So in  my onPreExecute, I do this:
    public void onPreExecute() {
        progressDialog.show();
    }

    //dismiss in onPostExecute
    public void onPostExecute() {
        progressDialog.dismiss();
    }

    public void to_login(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);

    }
}