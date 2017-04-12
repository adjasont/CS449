package com.example.jason.cs449;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 4/9/2017.
 */

public class GoalsActivity extends AppCompatActivity {

    private TabHost tabhost, tabhost2;
    private ListView dayList, weekList, monthList, yearList;
    private Button addGoal;
    private Goal goal;
    private menuTabs menu;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private String[] dateView = new String[4];
    private ListView[] dateList = new ListView[4];
    private String temp;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);


        dateView[0] = "Day";
        dateView[1] = "Week";
        dateView[2] = "Month";
        dateView[3] = "Year";

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid()).child("Goals");

        tabhost = (TabHost) findViewById(R.id.buttonTabHost);
        tabhost2 = (TabHost) findViewById(R.id.datesTabHost);
        dateList[0] = (ListView) findViewById(R.id.dayList);
        dateList[1] = (ListView) findViewById(R.id.monthList);
        dateList[2] = (ListView) findViewById(R.id.weekList);
        dateList[3] = (ListView) findViewById(R.id.yearList);
        dayList = (ListView) findViewById(R.id.dayList);
        addGoal = (Button) findViewById(R.id.addGoalButton);

        menu = new menuTabs(tabhost);

        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(menu.ts2.equals("tag2"))
                    startActivity(new Intent(GoalsActivity.this, StatsActivity.class));
                else if(menu.ts3.equals("tag3"))
                    startActivity(new Intent(GoalsActivity.this, CalendarActivity.class));
            }
        });


        tabhost2.setup();
        TabHost.TabSpec ts2 = tabhost2.newTabSpec("Day");
        ts2.setContent(R.id.tab1);
        ts2.setIndicator("Day");
        tabhost2.addTab(ts2);

        ts2 = tabhost2.newTabSpec("Week");
        ts2.setContent(R.id.tab2);
        ts2.setIndicator("Week");
        tabhost2.addTab(ts2);

        ts2 = tabhost2.newTabSpec("Month");
        ts2.setContent(R.id.tab3);
        ts2.setIndicator("Month");
        tabhost2.addTab(ts2);

        ts2 = tabhost2.newTabSpec("Year");
        ts2.setContent(R.id.tab4);
        ts2.setIndicator("Year");
        tabhost2.addTab(ts2);

        addGoal.setOnClickListener(new View.OnClickListener() {
            //When clicked add a new event to the current chosen date.
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = LayoutInflater.from(GoalsActivity.this);
                View promptView = layoutInflater.inflate(R.layout.activity_goal_input, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(GoalsActivity.this);
                builder.setMessage("New Goal");
                builder.setCancelable(true);
                builder.setView(promptView);

                final EditText GoalTitleInput = (EditText) promptView.findViewById(R.id.GoalTitleText);
                final EditText GoalDeadlineInput = (EditText) promptView.findViewById(R.id.GoalDeadline);
                final EditText GoalDescriptionInput = (EditText) promptView.findViewById(R.id.GoalDescriptionText);

                builder.setPositiveButton(
                        "Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                goal = new Goal(GoalTitleInput.getText().toString(),
                                        GoalDescriptionInput.getText().toString(), tabhost2.getCurrentTabTag(),
                                        GoalDeadlineInput.getText().toString(), "4");
                                mDatabase.child(tabhost2.getCurrentTabTag()).child(GoalTitleInput.getText().toString()).setValue(goal);
                                dialog.cancel();
                            }
                        }
                );

                builder.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }
                );

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public void onStart() {

        super.onStart();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i = 0; i < 4; i++) {
                    dataSnapshot.child("Users").child(auth.getCurrentUser().getUid()).child("Goals").child(dateView[i]);
                    ArrayList<Goal> goals = new ArrayList<Goal>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Goal goal = snapshot.getValue(Goal.class);
                        goals.add(goal);
                    }
                    ArrayAdapter<Goal> adapter = new goalArrayAdapter(GoalsActivity.this, 0, goals);
                    dateList[i].setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(userListener);
    }

}
