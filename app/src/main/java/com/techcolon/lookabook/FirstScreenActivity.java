package com.techcolon.lookabook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstScreenActivity extends AppCompatActivity {
    private Button signupBtn;
    private TextView loginBtn;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data.getBooleanExtra("LoggedIn", false)) {
                    Intent i = new Intent(this, MainActivity.class);

                    startActivity(i);
                    finish();
                } else {

                }
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                if (data.getBooleanExtra("SignedUp", false)) {
                    Intent i = new Intent(this, MainActivity.class);

                    startActivity(i);
                    finish();
                } else {

                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//splash screen

        if (user != null) {
            // User is signed in
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
            // No user is signed in
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        //Login button working
        loginBtn = findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(i, 1);
            }
        });

        // SignUp button working
        signupBtn = findViewById(R.id.signup);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(i, 2);
            }
        });
        TextView skip = findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}