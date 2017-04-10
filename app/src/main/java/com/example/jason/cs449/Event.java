package com.example.jason.cs449;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Jason on 3/5/2017.
 */

public class Event {

    public String time, title, description, date;

    public Event() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Event(String time, String title, String description, String date) {
        this.time = time;
        this.title = title;
        this.description = description;
        this.date = date;
    }
}
