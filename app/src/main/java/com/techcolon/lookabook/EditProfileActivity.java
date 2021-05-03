package com.techcolon.lookabook;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;

import java.net.MalformedURLException;
import java.net.URL;


public class EditProfileActivity extends AppCompatActivity {


    String email, firstName, lastName;
    private Button signUpBtn;
    private ShapeableImageView profilePic;
    private FirebaseAuth mAuth;
    private TextInputLayout firstnameLayout, lastnameLayout, emailLayout;
    private String TAG = "Edit";
    private Uri profileUri = null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting Theme
        SharedPreferences prefs = getSharedPreferences("LookABook_Storage", Context.MODE_PRIVATE);
        int theme = prefs.getInt("Theme", 0);


        if (theme == 1) {
            setTheme(R.style.ThemeLight_LookABook);
        } else if (theme == 2) {
            setTheme(R.style.ThemeDark_LookABook);
        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.ThemeDark_LookABook);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    setTheme(R.style.ThemeLight_LookABook);
                    break;
            }
        }

        setContentView(R.layout.activity_edit_profile);


        firstnameLayout = findViewById(R.id.firstname);
        lastnameLayout = findViewById(R.id.lastname);
        emailLayout = findViewById(R.id.email);
        profilePic = findViewById(R.id.profilepic);

        // Setting the current User's details in the respective TextFields;

        firstnameLayout.getEditText().setText(User.getFirstName());
        lastnameLayout.getEditText().setText(User.getLastName());
        emailLayout.getEditText().setText(User.getEmail());

        if (User.getProfilePhotoUrl() != null) {
            Glide.with(this).load(User.getProfilePhotoUrl()).into(profilePic);
        }

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });


        signUpBtn = findViewById(R.id.signupbutton);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showProgressDialog(true);
                onSignUpButton();

            }
        });

    }

    private void onSignUpButton() {

        checkNetwork();


        mAuth = User.getmAuth();

        firstName = firstnameLayout.getEditText().getText().toString();
        lastName = lastnameLayout.getEditText().getText().toString();
        email = emailLayout.getEditText().getText().toString();

        if (!createAccount(firstName, lastName, email)) {
            showProgressDialog(false);
            return;
        }

        DatabaseReference myRef = User.getDatabase().getReference().child("Users").child(User.getmAuth().getCurrentUser().getUid());


        myRef.child("FirstName").setValue(firstName);
        myRef.child("LastName").setValue(lastName);
        myRef.child("Email").setValue(email);

        User.getUserData();

        if (profileUri != null)
            uploadProfilePic(profileUri);

        Toast.makeText(getApplicationContext(), "Edits Saved", Toast.LENGTH_SHORT).show();
        showProgressDialog(false);
        finish();


    }


    private boolean createAccount(String firstName, String lastName, String email) {

        firstnameLayout.setError(null);
        lastnameLayout.setError(null);
        emailLayout.setError(null);


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
            emailLayout.setError(null);

        }


        return true;
    }

    private boolean checkEmail(String email) {

        if (email == null)
            return false;
        else {
            return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
        }
    }


    private boolean checkName(String name) {
        if (name == null) {
            return false;
        } else if (name.length() <= 2) {
            return false;
        } else
            return true;


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                profileUri = data.getData();
                profilePic.setImageURI(profileUri);
            }
        }
    }

    private void uploadProfilePic(Uri profileUri) {
        User.getStorageReference().child("ProfilePhotos").child(User.getUser().getUid()).putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //  User.getDatabase().getReference().child("Users").child(User.getUser().getUid()).child("ProfilePhotoUrl").setValue(User.getStorageReference().child("ProfilePhotos").child(User.getUser().getUid()).getDownloadUrl());

                User.getStorageReference().child("ProfilePhotos").child(User.getUser().getUid()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        URL url = null;
                        try {
                            url = new URL(task.getResult().toString());
                            Toast.makeText(EditProfileActivity.this, task.getResult().toString(), Toast.LENGTH_LONG).show();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        User.getDatabase().getReference().child("Users").child(User.getUser().getUid()).child("ProfilePhotoUrl").setValue(url.toString());
                        User.setProfilePhotoUrl(url.toString());
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Some error occurred while uploading Profile Photo", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private void showProgressDialog(Boolean show) {
        if (show) {
            progressDialog = new ProgressDialog(EditProfileActivity.this);
            progressDialog.setTitle("Loading...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

        } else {
            progressDialog.cancel();
        }
    }

    private void checkNetwork() {
        //checking network state
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        if (!connected) {
            new AlertDialog.Builder(getApplicationContext()).setTitle("Something Went Wrong...")
                    .setMessage("You are not connected to the internet")
                    .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            checkNetwork();
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        } else
            return;
    }


    //code to edit phone number
//    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential( "+91-98298XXXX2", "OTP_CODE" );
//    // Update Mobile Number...
//    firebaseAuth.getCurrentUser().updatePhoneNumber(phoneAuthCredential)
//        .addOnCompleteListener(new OnCompleteListener <Void>() {
//            @Override
//            public void onComplete(@NonNull Task <Void> task) {
//                if (task.isSuccessful()) {
//                    // Update Successfully
//                } else {
//                    // Failed
//                }
//            }
//        }
//    );

}