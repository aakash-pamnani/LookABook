package com.techcolon.lookabook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        User.setDatabase(FirebaseDatabase.getInstance());
        User.setmAuth(FirebaseAuth.getInstance());
        User.setStorageReference(FirebaseStorage.getInstance().getReference());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, FirstScreenActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }
}