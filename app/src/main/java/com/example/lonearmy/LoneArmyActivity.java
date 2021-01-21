package com.example.lonearmy;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

public class LoneArmyActivity extends AppCompatActivity {

    // view handling object
    private LAView laView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make game fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        //Load the resolution into a point obj
        Point resolution = new Point();
        display.getSize(resolution);

        //And finally set the view for our game
        //Also passing in the screen resolution
        laView = new LAView(this, resolution.x, resolution.y);

        //Make LAView the view for the Activity
        setContentView(laView);
    }

    //If the Activity is paused make sure to pause our thread
    @Override
    protected void onPause() {
        super.onPause();
        laView.pause();
    }

    //If the Activity is resumed make sure to resume the thread
    @Override
    protected void onResume() {
        super.onResume();
        laView.resume();
    }
}