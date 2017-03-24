package com.example.jason.cs449;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by Jason on 2/20/2017.
 */

// firebase keeps the code really clean and easy to read, nice -BS

public class ProfileSettingActivity extends AppCompatActivity {

    private EditText firstName, lastName, birthDate;
    private String email, sFirstName, sLastName, sBirthDate, sGender;
    private Spinner gender;
    private Button save;
    private User user;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());

        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        birthDate = (EditText) findViewById(R.id.birthDate);
        gender = (Spinner) findViewById(R.id.gender);
        save = (Button) findViewById(R.id.saveSettings);

        email = (auth.getCurrentUser().getEmail()).toString().trim();

        save.setOnClickListener(new View.OnClickListener() { //When the save button is clicked, push the user information to the DB.
            @Override
            public void onClick(View v) {

                sFirstName = firstName.getText().toString();
                sLastName = lastName.getText().toString();
                sBirthDate = birthDate.getText().toString();
                sGender = gender.getSelectedItem().toString();

                user = new User(email, sFirstName, sLastName, sBirthDate, sGender);
                mDatabase.setValue(user);
            }
        });

    }
    @Override
    public void onStart() { //Update the user display settings to their DB settings.

        super.onStart();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                firstName.setText(user.firstName);
                lastName.setText(user.lastName);
                birthDate.setText(user.birthDate);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.addValueEventListener(userListener);
    }
}
