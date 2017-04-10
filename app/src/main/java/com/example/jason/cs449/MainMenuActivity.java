package com.example.jason.cs449;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Jason on 2/18/2017.
 */



public class MainMenuActivity extends AppCompatActivity{

    private Button buttonLogout, buttonCalendar, buttonSocial, buttonStat;
    private FirebaseAuth auth;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        buttonLogout = (Button) findViewById(R.id.logout);
        buttonCalendar = (Button) findViewById(R.id.calendar);
        buttonSocial = (Button) findViewById(R.id.social);
        buttonStat = (Button) findViewById(R.id.workout);
        mProgress = (ProgressBar) findViewById(R.id.experienceBar);

        mProgress.setMax(30);
        mProgress.setProgress(20);

        buttonSocial.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainMenuActivity.this, GoalsActivity.class));
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainMenuActivity.this, LoginActivity.class));
            }
        });

        buttonCalendar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainMenuActivity.this, CalendarActivity.class));
            }
        });

        buttonStat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainMenuActivity.this, StatsActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.

        switch (item.getItemId()) {
            case R.id.event:

            case R.id.status:

            case R.id.settings:
                //Intent intent = new Inten(this, ProfileSettingActivity.class);
                startActivity(new Intent(this, ProfileSettingActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

