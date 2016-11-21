/**
 * Filename: MainActivity.java
 * Author: Matthew Huie
 *
 * MainActivity represents the initial view of the app.  This Activity has a single ImageView which
 * is used to transition the user to the next Activity.
 */

package com.matthewhuie.mrjitters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnClickListener {

    // The ImageView of the Mr. Jitters logo.
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Attaches a listener for animation/transition to the Mr. Jitters logo.
        logo = (ImageView)findViewById(R.id.mrjitters);
        logo.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Adds a jitter animation to the Mr. Jitters logo.
        RotateAnimation jitter = new RotateAnimation(0, 2, 50, 50);
        jitter.setDuration(10);
        jitter.setRepeatCount(Animation.INFINITE);
        jitter.setRepeatMode(Animation.REVERSE);
        logo.startAnimation(jitter);
    }

    @Override
    public void onClick(View view) {

        // Defines a new alpha/scale animation
        Animation click = AnimationUtils.loadAnimation(this, R.anim.click);

        // Defines a listener to transition to the PlacePickerActivity after the animation completes
        click.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent i = new Intent(getApplicationContext(), PlacePickerActivity.class);
                startActivity(i);
            }
        });

        // Attaches the alpha/scale animation to the view
        view.startAnimation(click);
    }
}
