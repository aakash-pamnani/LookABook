package com.techcolon.lookabook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FirstScreenActivity extends AppCompatActivity {
    private Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent i = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(i);
//            // finish() ;
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        signupBtn = findViewById(R.id.signup);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(i);
            }
        });
        TextView skip = findViewById(R.id.skip);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });

    }
}