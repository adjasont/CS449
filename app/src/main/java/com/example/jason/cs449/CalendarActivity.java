package com.example.jason.cs449;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.tabhost;

/**
 * Created by Jason on 2/19/2017.
 */

public class CalendarActivity extends AppCompatActivity {

    private TabHost tabhost;
    private CalendarView calendar;
    private TextView progressTab, dateDisplay;
    private ListView eventList;
    private Button addButton;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private String EventTitle, sDate;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        calendar = (CalendarView) findViewById(R.id.calendarView);
        tabhost = (TabHost) findViewById(R.id.tabHost);
        progressTab = (TextView) findViewById(R.id.textProgressTab);
        dateDisplay = (TextView) findViewById(R.id.textDateDisplay);
        eventList = (ListView) findViewById(R.id.eventList);
        addButton = (Button) findViewById(R.id.addButton);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid()).child("Events");


        tabhost.setup();
        TabHost.TabSpec ts = tabhost.newTabSpec("tag1");
        ts.setContent(R.id.tab1);
        ts.setIndicator("Progress");
        tabhost.addTab(ts);
        ts = tabhost.newTabSpec("tag2");
        ts.setContent(R.id.tab2);
        ts.setIndicator("Events");
        tabhost.addTab(ts);
        ts = tabhost.newTabSpec("tag3");
        ts.setContent(R.id.tab3);
        ts.setIndicator("Third Tab");
        tabhost.addTab(ts);

        calendar.setClickable(true);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //Everytime you click a different day on the calendar. Update the displayed Event settings form the DB
            //
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                dateDisplay.setText((Integer.toString(month + 1)) + "/" +
                        Integer.toString(dayOfMonth) + "/" +
                        Integer.toString(year));
                sDate = ((Integer.toString(month + 1) + (Integer.toString(dayOfMonth)) + (Integer.toString(year))));

                mDatabase.child(sDate).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Event> events = new ArrayList<Event>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Event event = snapshot.getValue(Event.class);
                            events.add(event);
                        }
                        ArrayAdapter<Event> adapter = new eventArrayAdapter(CalendarActivity.this, 0, events);
                        eventList.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        eventList.setClickable(true);

        addButton.setOnClickListener(new View.OnClickListener() {
            //When clicked add a new event to the current chosen date.
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = LayoutInflater.from(CalendarActivity.this);
                View promptView = layoutInflater.inflate(R.layout.activity_event_input, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(CalendarActivity.this);
                builder.setMessage("New Event");
                builder.setCancelable(true);
                builder.setView(promptView);

                final EditText EventTitleInput = (EditText) promptView.findViewById(R.id.EventTitleText);
                final EditText EventTimeInput = (EditText) promptView.findViewById(R.id.EventTimeText);
                final EditText EventDescriptionInput = (EditText) promptView.findViewById(R.id.EventDescriptionText);

                builder.setPositiveButton(
                        "Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                event = new Event(EventTimeInput.getText().toString(),
                                        EventTitleInput.getText().toString(),
                                        EventDescriptionInput.getText().toString(),
                                        sDate);
                                mDatabase.child(sDate).child(EventTitleInput.getText().toString()).setValue(event);
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

        class eventArrayAdapter extends ArrayAdapter<Event> {
            private Context context;
            private List<Event> events;

            public eventArrayAdapter(Context context, int resource, ArrayList<Event> objects) {
                super(context, resource, objects);

                this.context = context;
                this.events = objects;
            }
        // pretty sure you want to check if convertView is null first, no? This is how views get recycled (if neccessary), this may
        // not be needed when dealing with TextViews only, I know I had to do it that way for ImageViews.
       
            public View getView(int position, View convertView, ViewGroup parent) {

                Event event = events.get(position);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.activity_event_layout, null);

                TextView description = (TextView) view.findViewById(R.id.descriptionText);
                TextView title = (TextView) view.findViewById(R.id.titleText);

                title.setText(event.title);
                description.setText(event.description);

                return view;
            }
        }
    }
