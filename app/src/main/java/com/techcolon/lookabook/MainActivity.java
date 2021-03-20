package com.techcolon.lookabook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FloatingActionButton addNewBookFabBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();


        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav, navController);
        bottomNav.getMenu().getItem(2).setEnabled(false);

        addNewBookFabBtn = findViewById(R.id.addbookfabbtn);

        addNewBookFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(User.getmAuth().getCurrentUser()!=null) {
                       Intent i = new Intent(getApplicationContext(), AddBookActivity.class);
                       startActivity(i);
                }
                else {
                        Toast.makeText(MainActivity.this, "Login/SignUp First", Toast.LENGTH_SHORT).show();
                        bottomNav.setSelectedItemId(R.id.profileFragement);
                }
            }
        });


    }


}