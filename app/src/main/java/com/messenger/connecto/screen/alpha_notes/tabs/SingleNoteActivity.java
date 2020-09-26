package com.messenger.connecto.screen.alpha_notes.tabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.messenger.connecto.R;
import com.messenger.connecto.screen.alpha_notes.tabs.user.UserNotes;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;

public class SingleNoteActivity extends AppCompatActivity {

    EditText title,description;
    DatabaseReference databaseReference;
    FirebaseUser current_user;
    private SpotsDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String context_title;
    String context_desc;
    String context_id;
    String timeStamp;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_note);
        title = findViewById(R.id.editTextTitle);
        description = findViewById(R.id.editTextDescription);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        progressDialog = new SpotsDialog(this, R.style.custom_progressDialog);

        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Getting data for editing
        Intent intent = getIntent();
        context_title=intent.getStringExtra("TITLE");
        context_desc=intent.getStringExtra("DESCRIPTION");
        context_id = intent.getStringExtra("NOTE_ID");
        title.setText(context_title);
        description.setText(context_desc);


    }

    //Save onBackPressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        editor.putString("title", title.getText().toString());
        editor.putString("description", description.getText().toString());
        editor.apply();
        editor.commit();
        super.onBackPressed();


        context_title = title.getText().toString();
        context_desc = description.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar calendar = Calendar.getInstance();
        String context_date = simpleDateFormat.format(calendar.getTime());

        if(!context_title.equalsIgnoreCase("") || !context_desc.equalsIgnoreCase("")){
            if(context_id==null || context_id.isEmpty()){
                save_notes_to_database(context_title,context_desc,context_date);
            }else {
                edited_note_to_database(context_title,context_desc,context_date);
            }
        }else{
            return;
        }

    }

    //Save NOTES
    private void save_notes_to_database(String context_title, String context_desc, String context_date) {
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        uid = current_user.getUid();
        Long tsLong = System.currentTimeMillis() / 1000;
        timeStamp = tsLong.toString();
        UserNotes userNotes = new UserNotes(timeStamp, context_date, context_title, context_desc);


        databaseReference.child(uid).child("user_notes").child(timeStamp).setValue(userNotes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(SingleNoteActivity.this, "Your Note is Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SingleNoteActivity.this, AllNotesFragment.class);
                    startActivity(intent);
                    finish();
                } else {

                    Toast.makeText(SingleNoteActivity.this, "Error in saving your Notes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Edit Notes
    private void edited_note_to_database(String context_title, String context_desc, String context_date) {
        current_user = FirebaseAuth.getInstance().getCurrentUser();
        uid = current_user.getUid();
        UserNotes userNotes = new UserNotes(context_id,context_date,context_title,context_desc);

        databaseReference.child(uid).child("user_notes").child(context_id).setValue(userNotes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(SingleNoteActivity.this, "Your Note is Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SingleNoteActivity.this,AllNotesFragment.class);
                    overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                    startActivity(intent);
                    finish();
                }
                else {

                    Toast.makeText(SingleNoteActivity.this, "Error in saving your Notes", Toast.LENGTH_SHORT).show();
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


}