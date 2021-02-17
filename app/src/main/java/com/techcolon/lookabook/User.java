package com.techcolon.lookabook;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

class User {

    private static String email;
    private static String firstName;
    private static String lastName;
    private static FirebaseDatabase database; // splash screen
    private static FirebaseAuth mAuth; //splash screen
    private static FirebaseUser user;
    private static ArrayList<Book> userBooks = new ArrayList<>();


    public static String getEmail() {
        return User.email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static String getFirstName() {
        return User.firstName;
    }

    public static void setFirstName(String firstName) {
        User.firstName = firstName;
    }

    public static String getLastName() {
        return User.lastName;
    }

    public static void setLastName(String lastName) {
        User.lastName = lastName;
    }

    public static FirebaseAuth getmAuth() {
        return mAuth;
    }

    public static void intializemAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseUser getUser() {
        return user;
    }

    public static void setUser(FirebaseUser user) {
        User.user = user;
    }

    public static void initializeFirebaseDatabase() {

        User.database = FirebaseDatabase.getInstance();
    }

    public static FirebaseDatabase getFirebaseDatabase() {
        return User.database;
    }

    public static void addBooks(Book b) {
        User.userBooks.add(b);
    }

    public static ArrayList<Book> getUserBooks() {
        return userBooks;
    }

}

    