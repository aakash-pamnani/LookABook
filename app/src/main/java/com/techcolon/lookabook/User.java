package com.techcolon.lookabook;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

class User {

    private static String email;
    private static String firstName;
    private static String lastName;
    private static FirebaseDatabase database; // splash screen
    private static FirebaseAuth mAuth; //splash screen
    private static FirebaseUser user;

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

    protected static void loginUser(String email, String password) {


    }

//    protected boolean signUpUser(String email, String password) {
//
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "createUserWithEmail:success");
//                            user = mAuth.getCurrentUser();
//                            String userID = user.getUid();
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(getContext(), "Login failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }


}
