package com.example.jason.cs449;

/**
 * Created by Jason on 4/1/2017.
 */

public class SocialMediaPost {

    public String email;
    public String firstName;
    public String lastName;
    public String post;


    public SocialMediaPost() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public SocialMediaPost(String email, String postx) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.post = post;

    }
}
