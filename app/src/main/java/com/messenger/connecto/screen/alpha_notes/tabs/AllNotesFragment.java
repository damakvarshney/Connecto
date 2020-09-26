package com.messenger.connecto.screen.alpha_notes.tabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.messenger.connecto.R;
import com.messenger.connecto.screen.alpha_notes.tabs.adapter.AllNotesAdapter;
import com.messenger.connecto.screen.alpha_notes.tabs.user.UserNotes;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

import static android.content.Context.MODE_PRIVATE;

public class AllNotesFragment extends Fragment {

    RecyclerView recyclerView;
    TextView start_textView;
    ImageView floating_button;
    private SpotsDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String received_title, received_desc;
    FirebaseUser current_user;
    DatabaseReference databaseReference, database_note;
    ArrayList<UserNotes> all_notes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_notes, container, false);
        start_textView = view.findViewById(R.id.start_creating_textView);
        recyclerView = view.findViewById(R.id.recyclerView);
        floating_button = view.findViewById(R.id.iv_add_notes);
        progressDialog = new SpotsDialog(getActivity(), R.style.custom_progressDialog);
        sharedPreferences = this.getActivity().getSharedPreferences("AIRNOTES_DATA", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Intent intent = this.getActivity().getIntent();
        received_title = intent.getStringExtra("TITLE");
        received_desc = intent.getStringExtra("DESCRIPTION");

        current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        database_note = databaseReference.child(uid).child("user_notes");

        LinearLayoutManager linearLManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLManager);

        read_from_database();

        floating_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(),SingleNoteActivity.class);
                startActivity(intent1);
            }
        });
        return view;
    }

    //Reading from the Database
    public void read_from_database() {
        all_notes.clear();
        onPreExecute();
        database_note.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    UserNotes userNotes = snapshot.getValue(UserNotes.class);
                    all_notes.add(userNotes);
                }
                if (dataSnapshot.getValue() != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                    start_textView.setVisibility(View.GONE);
                }

                AllNotesAdapter adapter = new AllNotesAdapter(getActivity(), all_notes);
                recyclerView.setAdapter(adapter);
                onPostExecute();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                onPostExecute();
            }
        });
    }

    //get orientation configuration to change Layout Manager
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            read_from_database();

            LinearLayoutManager linearLManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLManager);
            //add your code what you want to do when screen on PORTRAIT MODE
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            read_from_database();
            GridLayoutManager gridLManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(gridLManager);
            //add your code what you want to do when screen on LANDSCAPE MODE
        }
    }

    //Am using it in an AsyncTask. So in  my onPreExecute, I do getActivity():
    public void onPreExecute() {
        progressDialog.show();
    }

    //dismiss in onPostExecute
    public void onPostExecute() {
        progressDialog.dismiss();
    }


}