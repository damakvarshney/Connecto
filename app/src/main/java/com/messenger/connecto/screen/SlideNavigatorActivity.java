package com.messenger.connecto.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.messenger.connecto.R;
import com.ncorti.slidetoact.SlideToActView;

public class SlideNavigatorActivity extends AppCompatActivity {

    SlideToActView alpha_notes_slider, beta_messenger_slider;
    SearchView searchView;
    View alpha_notes_layout,beta_messenger_layout;
    ImageView display_searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_navigator);

        alpha_notes_slider = (SlideToActView) findViewById(R.id.alpha_notes_slider);
        beta_messenger_slider = (SlideToActView) findViewById(R.id.beta_messenger_slider);
        alpha_notes_layout = findViewById(R.id.alpha_notes_layout);
        beta_messenger_layout = findViewById(R.id.beta_messenger_layout);
        searchView = (SearchView)findViewById(R.id.searchView);
        display_searchView = findViewById(R.id.display_searchView);

        alpha_notes_slider.setVisibility(View.VISIBLE);

        alpha_notes_slider.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                beta_messenger_slider.setVisibility(View.VISIBLE);
                alpha_notes_slider.setVisibility(View.GONE);
                alpha_notes_slider.resetSlider();
                alpha_notes_layout.setVisibility(View.GONE);
                beta_messenger_layout.setVisibility(View.VISIBLE);
                
            }
        });

        beta_messenger_slider.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                alpha_notes_slider.setVisibility(View.VISIBLE);
                beta_messenger_slider.setVisibility(View.GONE);
                beta_messenger_slider.resetSlider();
                alpha_notes_layout.setVisibility(View.VISIBLE);
                beta_messenger_layout.setVisibility(View.GONE);
            }
        });


    }

    public void searchView_icon(View view) {
        if(searchView.getVisibility()==View.VISIBLE) {
            searchView.setVisibility(View.GONE);
            alpha_notes_slider.setVisibility(View.VISIBLE);
            beta_messenger_slider.setVisibility(View.GONE);
        }else if(searchView.getVisibility()==View.GONE){
            //savedState needed
            alpha_notes_slider.setVisibility(View.GONE);
            beta_messenger_slider.setVisibility(View.GONE);
            searchView.setVisibility(View.VISIBLE);
        }

    }
}