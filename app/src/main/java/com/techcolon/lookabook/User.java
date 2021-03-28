package com.techcolon.lookabook;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;

class User {

    private static String email;
    private static String firstName;
    private static String lastName;
    private static String phoneNumber;
    private static int noOfBooks = 0;
    private static FirebaseDatabase database; // splash screen
    private static FirebaseAuth mAuth; //splash screen
    private static FirebaseUser user;
    private static ArrayList<Book> userBooks = new ArrayList<>();
    private static StorageReference storageReference;
    private static URL profilePhotoUrl;

    public static URL getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public static void setProfilePhotoUrl(URL profilePhotoUrl) {
        User.profilePhotoUrl = profilePhotoUrl;
    }

    public static String getEmail() {
        return User.email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static void setPhoneNumber(String phoneNumber) {
        User.phoneNumber = phoneNumber;
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

    public static StorageReference getStorageReference() {
        return storageReference;
    }

    public static void setStorageReference(StorageReference storageReference) {
        User.storageReference = storageReference;
    }



    public static void getUserData() { //will be called when user loggedin



        database.getReference().child("Users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firstName = snapshot.child("FirstName").getValue(String.class);
                lastName = snapshot.child("LastName").getValue(String.class);
                profilePhotoUrl = snapshot.child("ProfilePhotoUrl").getValue(URL.class);
                phoneNumber = snapshot.child("PhoneNumber").getValue(String.class);
                noOfBooks = snapshot.child("noOfBooks").getValue(Integer.class);
                email = snapshot.child("Email").getValue(String.class);


                for (int i = 0; i < noOfBooks; i++) {
                    String bookkey = snapshot.child("Books Added by User").child("Book" + (i + 1)).getValue(String.class);
                    database.getReference().child("Books").child(bookkey).addListenerForSingleValueEvent(new ValueEventListener() {
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

    