package com.matthewhuie.mrjitters;

import android.app.Activity;
import android.os.Bundle;
import android.transition.Slide;

public class PlacePickerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);

        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
    }
}
