package com.techcolon.lookabook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;


import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


public class SignupFragment2 extends Fragment {


    private Button signUpButton;
    private ShapeableImageView profilePic;
    private FirebaseAuth mAuth;
    private TextInputLayout firstnameLayout, lastnameLayout, emailLayout, passwordLayout;
    private String TAG = "SignUp";
    private Uri profileUri = null;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = null;
        v = inflater.inflate(R.layout.fragment_signup2, container, false);
        v.requestFocus();

        signUpButton = v.findViewById(R.id.signupbutton);
         firstnameLayout = v.findViewById(R.id.firstname);
         lastnameLayout = v.findViewById(R.id.lastname);
         emailLayout = v.findViewById(R.id.email);
         passwordLayout = v.findViewById(R.id.password);
         profilePic = v.findViewById(R.id.profilepic);




        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password, firstName, lastName, phone;
                firstName = firstnameLayout.getEditText().getText().toString();
                lastName = lastnameLayout.getEditText().getText().toString();
                email = emailLayout.getEditText().getText().toString();
                password = passwordLayout.getEditText().getText().toString();
                if (!createAccount(firstName, lastName, email, password)) {
                    return;
                }

                //get mauth refrence
                mAuth = User.getmAuth();

                //method to check email and password with accounts and if valid user then update ui
                mAuth.createUserWithEmailAndPassword(email, password)

                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    User.setUser(user);
                                    String userID = user.getUid();
                                    // Write a message to the database

                                    DatabaseReference myRef = User.getDatabase().getReference().child("Users");

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
                                    myRef.child("PhoneNumber").setValue(User.getPhoneNumber());
                                    myRef.child("ProfilePhotoUrl").setValue(null);


                                    Toast.makeText(getContext(), "Signup Successfull", Toast.LENGTH_SHORT).show();
                                    updateUi(user);
                                    User.getUserData();
                                }
                                else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createAccountWithEmail:failure", task.getException());

                                    Toast.makeText(getContext(), "Signup Unsuccessfull", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }

            private void updateUi(FirebaseUser user) {

                if (user != null) {
                    //if user is not empty

                    if(profileUri != null)
                        uploadProfilePic(profileUri);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("SignedUp", true);
                    getActivity().setResult(RESULT_OK, resultIntent);
                    getActivity().finish();
                } else {
                    //if user is empty
                }
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        });
        return v;
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

    private void uploadProfilePic(Uri profileUri){
        User.getStorageReference().child("ProfilePhotos").child(User.getUser().getUid()).putFile(profileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                User.getDatabase().getReference().child("Users").child(User.getUser().getUid()).child("ProfilePhotoUrl").setValue(User.getStorageReference().child("ProfilePhotos").child(User.getUser().getUid()).getDownloadUrl());

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Some error occurred while uploading Profile Photo", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
