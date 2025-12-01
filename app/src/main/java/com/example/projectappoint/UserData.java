package com.example.projectappoint;

import com.google.firebase.Timestamp;

public class UserData {
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String bdate; // Storing date as String (e.g., "YYYY-MM-DD")
    private String passwordHashed;
    private Timestamp createdAt;

    // Required empty public constructor for Firestore
    public UserData() {}

    // Constructor for creating a new user object
    public UserData(String firstName, String lastName, String email, String gender, String bdate, String passwordHashed) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.bdate = bdate;
        this.passwordHashed = passwordHashed;
        // Automatically set the timestamp upon creation
        this.createdAt = Timestamp.now();
    }

    // --- Getters ---

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getBdate() {
        return bdate;
    }

    public String getPassword() {
        return passwordHashed;
    }

    public String getPasswordHashed() {
        return passwordHashed;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // --- Setters ---

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public void setPasswordHashed(String passwordHashed) {
        this.passwordHashed = passwordHashed;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}