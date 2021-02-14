package com.techcolon.lookabook;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class SignupActivity extends AppCompatActivity {
    private Button signUpButton;
    private TextInputLayout firstnameLayout, lastnameLayout, emailLayout, passwordLayout;

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
                createAccount();
            }
        });
    }

    private void createAccount() {

        String firstName, lastName, email, password;
        firstName = firstnameLayout.getEditText().getText().toString();
        lastName = lastnameLayout.getEditText().getText().toString();
        email = emailLayout.getEditText().getText().toString();
        password = passwordLayout.getEditText().getText().toString();

        if (!checkName(firstName)) {
            firstnameLayout.setError("This field has some error");
        } else {
            firstnameLayout.setError(null);
        }
        if (!checkName(lastName)) {
            lastnameLayout.setError("This field has some error");
        } else {
            lastnameLayout.setError(null);
        }
        if (!checkEmail(email)) {
            emailLayout.setError("This field has some error");
        } else {
            emailLayout.setError(null);
        }
        if (!checkPassword(password)) {
            passwordLayout.setError("This field has some error");
        } else {
            passwordLayout.setError(null);
        }

//       mAuth.createUserWithEmailAndPassword(email, password)
//       .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//           @Override
//           public void onComplete(@NonNull Task<AuthResult> task) {
//               if (task.isSuccessful()) {
//                   // Sign in success, update UI with the signed-in user's information
//                   Log.d(TAG, "createUserWithEmail:success");
//                   FirebaseUser user = mAuth.getCurrentUser();
//                   updateUI(user);
//               } else {
//                   // If sign in fails, display a message to the user.
//                   Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                   Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                           Toast.LENGTH_SHORT).show();
//                   updateUI(null);
//               }
//
//               // ...
//           }
//       });

    }

    private boolean checkEmail(String email) {
        // String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
        //                     "[a-zA-Z0-9_+&*-]+)*@" + 
        //                     "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
        //                     "A-Z]{2,7}$"; 

        // Pattern pat = Pattern.compile(emailRegex); 
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