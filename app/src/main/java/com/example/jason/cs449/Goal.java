package com.example.jason.cs449;

import android.content.Context;

import java.io.FileOutputStream;

/**
 * Created by Jason on 4/9/2017.
 */

public class Goal {

    public String goal;
    public String description;
    public String length;
    public String goalCompletionPoints;
    public String currentCompletionPoints;


    public Goal() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Goal(String goal, String description, String length, String currentCompletionPoints, String goalCompletionPoints) {
        this.goal = goal;
        this.description = description;
        this.currentCompletionPoints = currentCompletionPoints;
        this.goalCompletionPoints = goalCompletionPoints;
    }

    public void setGoal(String goal){
        this.goal = goal;
    }
}
