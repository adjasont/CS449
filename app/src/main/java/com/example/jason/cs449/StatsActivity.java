package com.example.jason.cs449;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

/**
 * Created by Jason on 4/8/2017.
 */

public class StatsActivity extends Activity {

    private ProgressBar levelBar;
    private Drawable draw;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        levelBar = (ProgressBar) findViewById(R.id.levelProgressBar);
        draw = getResources().getDrawable(R.drawable.progressbar_horizontal);

        levelBar.setMax(30);
        levelBar.setProgress(7);
        //levelBar.setProgressDrawable(draw);

    }
}
