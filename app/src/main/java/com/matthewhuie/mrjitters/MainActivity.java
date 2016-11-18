package com.matthewhuie.mrjitters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotoPlacePicker(View view) {
        Intent i = new Intent(getApplicationContext(), PlacePickerActivity.class);
        startActivity(i);
    }
}
