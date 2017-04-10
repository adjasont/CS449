package com.example.jason.cs449;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private String[] dateView = new String[4];
    private ListView[] dateList = new ListView[4];
    private String temp;
    FileOutputStream outStream;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);


        try {
            outStream = openFileOutput("goals.txt", Context.MODE_PRIVATE);
        }
        catch(FileNotFoundException ex){

        }

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
        addGoal = (Button) findViewById(R.id.addGoalButton);

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

        tabhost2.setup();
        TabHost.TabSpec ts2 = tabhost2.newTabSpec("Day");
        ts2.setContent(R.id.tab1);
        ts2.setIndicator("Daily");
        tabhost2.addTab(ts2);

        ts2 = tabhost2.newTabSpec("Week");
        ts2.setContent(R.id.tab2);
        ts2.setIndicator("Weekly");
        tabhost2.addTab(ts2);

        ts2 = tabhost2.newTabSpec("Month");
        ts2.setContent(R.id.tab3);
        ts2.setIndicator("Monthly");
        tabhost2.addTab(ts2);

        ts2 = tabhost2.newTabSpec("Year");
        ts2.setContent(R.id.tab4);
        ts2.setIndicator("Yearly");
        tabhost2.addTab(ts2);

        for(int i = 0; i < 4; i++){
            ArrayList<Goal> goals = new ArrayList<Goal>();
            try {
                FileInputStream fis = openFileInput("goals.txt");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                    goal = new Goal(line, " ", " ", "1", "4");
                    goals.add(goal);
                }
            }
            catch(FileNotFoundException ex){

            }
            catch(IOException ex){

            }
            ArrayAdapter<Goal> adapter = new GoalsActivity.goalArrayAdapter(GoalsActivity.this, 0, goals);
            dateList[1].setAdapter(adapter);
        }

        mDatabase.child(temp = tabhost2.getCurrentTabTag()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Goal> goals = new ArrayList<Goal>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Goal goal = snapshot.getValue(Goal.class);
                    goals.add(goal);
                }
                ArrayAdapter<Goal> adapter = new GoalsActivity.goalArrayAdapter(GoalsActivity.this, 0, goals);
                if(temp.equals("Day"))
                    dateList[0].setAdapter(adapter);
                else if(temp.equals("Week"))
                    dateList[1].setAdapter(adapter);
                else if(temp.equals("Month"))
                    dateList[2].setAdapter(adapter);
                else
                    dateList[3].setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
             }
        });

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
                                String input = GoalDeadlineInput.getText().toString();
                                goal = new Goal(GoalTitleInput.getText().toString(),
                                        GoalDescriptionInput.getText().toString(), tabhost2.getCurrentTabTag(),
                                        input, "4");
                                        try {
                                            outStream.write((GoalTitleInput.getText().toString()).getBytes());
                                            outStream.write((GoalDescriptionInput.getText().toString()).getBytes());
                                            outStream.write((tabhost2.getCurrentTabTag()).getBytes());
                                            outStream.write((GoalDeadlineInput.getText().toString()).getBytes());
                                        } catch (IOException ex){

                                        }
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
    class goalArrayAdapter extends ArrayAdapter<Goal> {
        private Context context;
        private List<Goal> goals;

        public goalArrayAdapter(Context context, int resource, ArrayList<Goal> objects) {
            super(context, resource, objects);

            this.context = context;
            this.goals = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            Goal goal = goals.get(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.activity_goal_layout, null);

            TextView completion = (TextView) view.findViewById(R.id.textView12);
            TextView title = (TextView) view.findViewById(R.id.textView11);
            ProgressBar pbar = (ProgressBar) view.findViewById(R.id.progressBar2);

            int max = Integer.parseInt(goal.goalCompletionPoints);
            int progress = Integer.parseInt(goal.currentCompletionPoints);
            pbar.setMax(max);
            pbar.setProgress(progress);


            title.setText(goal.goal);
            completion.setText(goal.currentCompletionPoints + "/" + goal.goalCompletionPoints);

            return view;
        }
    }

}
