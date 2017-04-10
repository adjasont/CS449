package com.example.jason.cs449;

/**
 * Created by Jason on 3/5/2017.
 */

public class User {

    public String email;
    public String firstName;
    public String lastName;
    public String birthDate;
    public String gender;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email, String firstName, String lastName, String birthDate, String gender) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
    }

}
