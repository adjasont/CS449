package com.example.jason.cs449;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.jason.cs449.R.id.event;
import static com.example.jason.cs449.R.id.everyoneList;

/**
 * Created by Jason on 4/1/2017.
 */

public class SocialActivity extends AppCompatActivity {

    private TabHost tabHost;
    private ListView EveryoneSocialList, TeamSocialList;
    private Button addPost;
    private EditText newPostText;
    private SocialMediaPost post;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        auth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        tabHost = (TabHost) findViewById(R.id.tabHost);
        EveryoneSocialList = (ListView) findViewById(R.id.everyoneList);
        TeamSocialList = (ListView) findViewById(R.id.teamList);
        addPost = (Button) findViewById(R.id.addPost);

        tabHost.setup();
        TabHost.TabSpec ts = tabHost.newTabSpec("tag1");
        ts.setContent(R.id.tab1);
        ts.setIndicator("Everyone");
        tabHost.addTab(ts);
        ts = tabHost.newTabSpec("tag2");
        ts.setContent(R.id.tab2);
        ts.setIndicator("Team");
        tabHost.addTab(ts);
        ts = tabHost.newTabSpec("tag3");
        ts.setContent(R.id.tab3);
        ts.setIndicator("Third Tab");
        tabHost.addTab(ts);

        mDatabase.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<SocialMediaPost> posts = new ArrayList<SocialMediaPost>();

                //Retrieve the user (Who the post belongs too_ first and last name and save it in the string name
                String name = ((dataSnapshot.child("firstName").getValue().toString()) + " " +
                               (dataSnapshot.child("lastName").getValue().toString()));

                //Search through the users Posts and add them into an arraylist of posts
                for (DataSnapshot snapshot : dataSnapshot.child("Posts").getChildren()) {
                    SocialMediaPost post = snapshot.getValue(SocialMediaPost.class);
                    post.firstName = name;
                    posts.add(post);
                }

                //Add the new arraylist of poast to the adpater and then set that adapter to the Social list of EveryoneT5R4FG
                ArrayAdapter<SocialMediaPost> adapter = new SocialActivity.postArrayAdapter(SocialActivity.this, 0, posts);
                EveryoneSocialList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addPost.setOnClickListener(new View.OnClickListener() {
            //When clicked add a new event to the current chosen date.
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = LayoutInflater.from(SocialActivity.this);
                View promptView = layoutInflater.inflate(R.layout.activity_new_post, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(SocialActivity.this);
                builder.setMessage("New Post");
                builder.setCancelable(true);
                builder.setView(promptView);

                final EditText newPostText = (EditText) promptView.findViewById(R.id.newPostText);

                builder.setPositiveButton(
                        "Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                post = new SocialMediaPost(auth.getCurrentUser().getEmail().toString(), newPostText.getText().toString());
                                Calendar c = Calendar.getInstance();
                                String date = c.getTime().toString();
                                mDatabase.child(auth.getCurrentUser().getUid()).child("Posts").child(date).setValue(post);
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

    class postArrayAdapter extends ArrayAdapter<SocialMediaPost> {
        private Context context;
        private List<SocialMediaPost> posts;

        public postArrayAdapter(Context context, int resource, ArrayList<SocialMediaPost> objects) {
            super(context, resource, objects);

            this.context = context;
            this.posts = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            SocialMediaPost post = posts.get(position);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.activity_post_display, null);

            TextView nameText = (TextView) view.findViewById(R.id.name);
            TextView postText = (TextView) view.findViewById(R.id.post);

            nameText.setText(post.firstName);
            postText.setText(post.post);

            return view;
        }
    }
}
