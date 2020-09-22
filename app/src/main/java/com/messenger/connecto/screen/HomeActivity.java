package com.messenger.connecto.screen;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.messenger.connecto.R;
import com.ncorti.slidetoact.SlideToActView;

public class HomeActivity extends AppCompatActivity {

    SlideToActView alpha_notes, beta_messenger;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        alpha_notes = (SlideToActView) findViewById(R.id.alpha_notes);
        beta_messenger = (SlideToActView) findViewById(R.id.beta_messenger);
        searchView = (SearchView)findViewById(R.id.searchView);

        alpha_notes.setVisibility(View.VISIBLE);

        alpha_notes.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                beta_messenger.setVisibility(View.VISIBLE);
                alpha_notes.setVisibility(View.GONE);
                alpha_notes.resetSlider();
            }
        });

        beta_messenger.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                alpha_notes.setVisibility(View.VISIBLE);
                beta_messenger.setVisibility(View.GONE);
                beta_messenger.resetSlider();
            }
        });



}


//    public void alpha_searchBar_enabled(View view) {
//        alpha_notes.setVisibility(View.GONE);
//        alpha_notes.resetSlider();
//        searchView.setVisibility(View.VISIBLE);
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                alpha_notes.setVisibility(View.VISIBLE);
//                searchView.setVisibility(View.GONE);
//                return true;
//            }
//        });
//
//    }
//
//    public void beta_searchBar_enabled(View view) {
//        beta_messenger.setVisibility(View.GONE);
//        beta_messenger.resetSlider();
//        searchView.setVisibility(View.VISIBLE);
//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                beta_messenger.setVisibility(View.VISIBLE);
//                searchView.setVisibility(View.GONE);
//                return true;
//            }
//        });
//    }
}