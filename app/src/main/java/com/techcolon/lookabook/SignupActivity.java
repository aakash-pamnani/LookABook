package com.techcolon.lookabook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


public class SignupActivity extends AppCompatActivity {
    private Button signUpButton;
    private TextInputLayout firstnameLayout, lastnameLayout, emailLayout, passwordLayout;
    private FirebaseAuth mAuth;
    private String TAG = "SignUp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signUpButton = findViewById(R.id.signupbutton);
        firstnameLayout = findViewById(R.id.firstname);
        lastnameLayout = findViewById(R.id.lastname);
        emailLayout = findViewById(R.id.email);
        passwordLayout = findViewById(R.id.password);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password, firstName, lastName;
                firstName = firstnameLayout.getEditText().getText().toString();
                lastName = lastnameLayout.getEditText().getText().toString();
                email = emailLayout.getEditText().getText().toString();
                password = passwordLayout.getEditText().getText().toString();
                if (!createAccount(firstName, lastName, email, password)) {
                    return;
                }

                //initialize firebase authentication in user class
                User.intializemAuth(); //shift this to splash screen
                mAuth = User.getmAuth();

                //method to check email and password with accounts and if valid user then update ui
                mAuth.createUserWithEmailAndPassword(email, password)

                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    User.setUser(user);
                                    String userID = user.getUid();
                                    User.initializeFirebaseDatabase(); // Shift this to Flash Screen
                                    // Write a message to the database

                                    DatabaseReference myRef = User.getFirebaseDatabase().getReference().child("Users");

                                    User.setEmail(email);
                                    User.setFirstName(firstName);
                                    User.setLastName(lastName);
                                    User.setNoOfBooks(0);

                                    myRef.child(userID);

                                    myRef = myRef.child(userID);
                                    myRef.child("FirstName").setValue(User.getFirstName());
                                    myRef.child("LastName").setValue(User.getLastName());
                                    myRef.child("Email").setValue(User.getEmail());
                                    myRef.child("noOfBooks").setValue(User.getNoOfBooks());

                                    Toast.makeText(SignupActivity.this, "Signup Successfull", Toast.LENGTH_SHORT).show();
                                    updateUi(user);


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createAccountWithEmail:failure", task.getException());

                                    Toast.makeText(SignupActivity.this, "Signup Unsuccessfull", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }

            private void updateUi(FirebaseUser user) {

                if (user != null) {
                    //if user is not empty
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("SignedUp", true);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    //if user is empty
                }
            }
        });
    }

    private boolean createAccount(String firstName, String lastName, String email, String password) {

        firstnameLayout.setError(null);
        lastnameLayout.setError(null);
        emailLayout.setError(null);
        passwordLayout.setError(null);

        if (!checkName(firstName)) {
            firstnameLayout.setError("This field has some error");
            return false;
        } else {
            firstnameLayout.setError(null);
        }


        if (!checkName(lastName)) {
            lastnameLayout.setError("This field has some error");
            return false;
        } else {
            lastnameLayout.setError(null);

        }


        if (!checkEmail(email)) {
            emailLayout.setError("This field has some error");
            return false;
        } else {

        }


        if (!checkPassword(password)) {
            passwordLayout.setError("This field has some error");
            return false;
        } else {
            passwordLayout.setError(null);
            return true;
        }


    }

    private boolean checkEmail(String email) {

        if (email == null)
            return false;
        else {
            return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
        }
    }

    private boolean checkPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        return true;
    }

    private boolean checkName(String name) {
        if (name.length() <= 2) {
            return false;
        }
        return true;
    }
}