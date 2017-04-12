package com.example.jason.cs449;

import android.content.Intent;
import android.widget.TabHost;

import static android.R.id.tabhost;

/**
 * Created by Jason on 4/11/2017.
 */

public class menuTabs {

    public TabHost.TabSpec ts1;
    public TabHost.TabSpec ts2;
    public TabHost.TabSpec ts3;
    public TabHost.TabSpec ts4;

    public menuTabs(){

    }

    public menuTabs(TabHost tabhost){

        tabhost.setup();
        ts1 = tabhost.newTabSpec("tag1");
        ts1.setContent(R.id.goals);
        ts1.setIndicator("Goals");
        tabhost.addTab(ts1);

        ts2 = tabhost.newTabSpec("tag2");
        ts2.setContent(R.id.stats);
        ts2.setIndicator("Stats");
        tabhost.addTab(ts2);

        ts3 = tabhost.newTabSpec("tag3");
        ts3.setContent(R.id.events);
        ts3.setIndicator("Events");
        tabhost.addTab(ts3);

        ts4 = tabhost.newTabSpec("tag4");
        ts4.setContent(R.id.competition);
        ts4.setIndicator("Competitions");
        tabhost.addTab(ts4);
    }

}
