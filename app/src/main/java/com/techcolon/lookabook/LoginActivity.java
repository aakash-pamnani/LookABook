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

public class LoginActivity extends AppCompatActivity {
    protected static FirebaseAuth mAuth;
    private static String TAG = "Login";
    private Button logInButton;
    private TextInputLayout emailLayout, passwordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logInButton = findViewById(R.id.loginbutton);
        emailLayout = findViewById(R.id.email);
        passwordLayout = findViewById(R.id.password);

        // Handel login button click
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get email and password from edit text layout
                String email = emailLayout.getEditText().getText().toString();
                String password = passwordLayout.getEditText().getText().toString();

                //Check if email and password are valid and not empty
                if (!loginAccount(email, password)) {
                    return;
                }

                //method to check email and password with accounts and if valid user then update ui
                mAuth.signInWithEmailAndPassword(email, password)

                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    User.setUser(user);
                                    User.getUserData();
                                    Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                    updateUi(user);


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());

                                    Toast.makeText(LoginActivity.this, "Login Unsuccessfull", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }

            private void updateUi(FirebaseUser user) {
                if (user != null) {
                    //if user is not empty
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("LoggedIn", true);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    //if user is empty
                }
            }
        });
    }

    private boolean loginAccount(String email, String password) {


        emailLayout.setError(null);
        passwordLayout.setError(null);

        if (!checkEmail(email)) {
            emailLayout.setError("This field has some error");
            return false;
        } else {
            emailLayout.setError(null);

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
}