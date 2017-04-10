package com.example.jason.cs449;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

/**
 * Created by Jason on 4/9/2017.
 */

public class AppTheme extends AppCompatActivity{

    private TabHost tabhost;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.footer);

            tabhost = (TabHost) findViewById(R.id.buttonTabHost);

            tabhost.setup();
            TabHost.TabSpec ts = tabhost.newTabSpec("tag1");
            ts.setContent(R.id.stats);
            ts.setIndicator("Stats");
            tabhost.addTab(ts);

            ts = tabhost.newTabSpec("tag2");
            ts.setContent(R.id.goals);
            ts.setIndicator("Goals");
            tabhost.addTab(ts);

            ts = tabhost.newTabSpec("tag3");
            ts.setContent(R.id.events);
            ts.setIndicator("Events");
            tabhost.addTab(ts);

            ts = tabhost.newTabSpec("tag4");
            ts.setContent(R.id.competition);
            ts.setIndicator("Competitions");
            tabhost.addTab(ts);

    }
}
