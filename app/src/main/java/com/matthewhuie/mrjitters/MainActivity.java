package com.matthewhuie.mrjitters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotoPlacePicker(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.click));
        Intent i = new Intent(getApplicationContext(), PlacePickerActivity.class);
        startActivity(i);
    }
}
