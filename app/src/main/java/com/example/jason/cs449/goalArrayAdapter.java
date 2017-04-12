package com.example.jason.cs449;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason on 4/11/2017.
 */

public class goalArrayAdapter extends ArrayAdapter {

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

        //int max = Integer.parseInt(goal.goalCompletionPoints);
        //int progress = Integer.parseInt(goal.currentCompletionPoints);
        //pbar.setMax(max);
        //pbar.setProgress(progress);


        title.setText(goal.goal);
        //completion.setText(goal.currentCompletionPoints + "/" + goal.goalCompletionPoints);

        return view;
    }
}
