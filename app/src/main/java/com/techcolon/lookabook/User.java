package com.techcolon.lookabook;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

class User {

    private static String email;
    private static String firstName;
    private static String lastName;
    private static int noOfBooks;
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


    public static FirebaseDatabase getDatabase() {
        return database;
    }

    public static void setDatabase(FirebaseDatabase database) {
        User.database = database;
    }

    public static void setmAuth(FirebaseAuth mAuth) {
        User.mAuth = mAuth;
    }

    public static FirebaseUser getUser() {
        return user;
    }

    public static void setUser(FirebaseUser user) {
        User.user = user;
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

    public static int getNoOfBooks() {
        return noOfBooks;
    }

    public static void setNoOfBooks(int noOfBooks) {
        User.noOfBooks = noOfBooks;
    }

    public static void getUserData() { //will be called when user loggedin
        email = user.getEmail();

        database.getReference().child("Users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firstName = snapshot.child("FirstName").getValue(String.class);
                lastName = snapshot.child("LastName").getValue(String.class);
                noOfBooks = snapshot.child("noOfBooks").getValue(Integer.class);

                for (int i = 0; i < noOfBooks; i++) {
                    String bookkey = snapshot.child("Books Added by User").child("Book" + (i + 1)).getValue(String.class);
                    database.getReference().child("Books").child(bookkey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userBooks.add(snapshot.getValue(Book.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

    