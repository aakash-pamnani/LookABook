package com.techcolon.lookabook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    private FirebaseAuth mAuth;
    FloatingActionButton addNewBookFabBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.topAppBar);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();


        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        NavigationUI.setupWithNavController(bottomNav, navController);
        bottomNav.getMenu().getItem(2).setEnabled(false);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {


                switch (destination.getId()) {
                    case R.id.bookListFragment: {
                        toolbar.setTitle("LookABook");
                        toolbar.getMenu().clear();
                        toolbar.inflateMenu(R.menu.booklist_menu);
                        break;
                    }
                    case R.id.profileFragement: {
                        toolbar.setTitle("Profile");
                        if(User.getUser()!=null) {
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.profile_fragment_menu);
                            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    if(item.getItemId() == R.id.logout){

                                        User.getmAuth().signOut();
                                        User.setUser(null);
                                        User.setmAuth(null);
                                        navController.navigate(R.id.bookListFragment);
                                    }

                                    return false;
                                }
                            });
                        }
                        else
                            toolbar.getMenu().clear();
                        break;
                    }
                    case R.id.userBookListFragment: {
                        toolbar.getMenu().clear();
                        toolbar.setTitle("My Books");
                        break;
                    }
                    case R.id.settingsFragment: {
                        toolbar.getMenu().clear();
                        toolbar.setTitle("Settings");
                        break;
                    }
                    default: {
                        toolbar.setTitle("LookABook");
                    }

                }

            }
        });


        addNewBookFabBtn = findViewById(R.id.addbookfabbtn);

        addNewBookFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.getmAuth().getCurrentUser() != null) {
                    Intent i = new Intent(getApplicationContext(), AddBookActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Login/SignUp First", Toast.LENGTH_SHORT).show();
                    bottomNav.setSelectedItemId(R.id.profileFragement);
                }
            }
        });


    }


}