package com.techcolon.lookabook;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;


public class SignupActivity extends AppCompatActivity {


    String email, firstName, lastName, phone;
    private EditText otp1, otp2, otp3, otp4, otp5, otp6;
    private Button signUpBtn;
    private TextInputLayout phoneLayout;
    private int buttonState = 1;
    private PhoneAuthCredential phoneCredential;
    private String verification;
    private ShapeableImageView profilePic;
    private FirebaseAuth mAuth;
    private TextInputLayout firstnameLayout, lastnameLayout, emailLayout, passwordLayout;
    private String TAG = "SignUp";
    private Uri profileUri = null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        phoneLayout = findViewById(R.id.phonenumber);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);


        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    otp1.clearFocus();
                    otp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    otp2.clearFocus();
                    otp3.requestFocus();
                } else if (charSequence.length() == 0) {
                    otp2.clearFocus();
                    otp1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    otp3.clearFocus();
                    otp4.requestFocus();
                } else if (charSequence.length() == 0) {
                    otp3.clearFocus();
                    otp2.requestFocus();
                }
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    otp4.clearFocus();
                    otp5.requestFocus();
                } else if (charSequence.length() == 0) {
                    otp4.clearFocus();
                    otp3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    otp5.clearFocus();
                    otp6.requestFocus();
                } else if (charSequence.length() == 0) {
                    otp5.clearFocus();
                    otp4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        otp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (charSequence.length() == 1) {


                } else if (charSequence.length() == 0) {
                    otp6.clearFocus();
                    otp5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        firstnameLayout = findViewById(R.id.firstname);
        lastnameLayout = findViewById(R.id.lastname);
        emailLayout = findViewById(R.id.email);
        passwordLayout = findViewById(R.id.password);
        profilePic = findViewById(R.id.profilepic);


        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });


        signUpBtn = findViewById(R.id.signupbutton);
        signUpBtn.setEnabled(false);

    }

    public void onSendOtp(View view) {

        firstName = firstnameLayout.getEditText().getText().toString();
        lastName = lastnameLayout.getEditText().getText().toString();
        email = emailLayout.getEditText().getText().toString();
        phone = phoneLayout.getEditText().getText().toString();
        if (!createAccount(firstName, lastName, email, phone)) {
            return;
        }
        showProgressDialog(true);
        onGenerateOtp();
    }

    private void onGenerateOtp() {


        phone = phoneLayout.getEditText().getText().toString();

        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phone, 10, TimeUnit.SECONDS, this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

//                        String otp = phoneAuthCredential.getSmsCode();
//                        otp1.setText(otp.charAt(0));
//                        otp2.setText(otp.charAt(1));
//                        otp3.setText(otp.charAt(2));
//                        otp4.setText(otp.charAt(3));
//                        otp5.setText(otp.charAt(4));
//                        otp6.setText(otp.charAt(5));


                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {


                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forcedResendingToken) {
                        super.onCodeSent(verificationId, forcedResendingToken);
                        verification = verificationId;
                        Log.d("id", verificationId);
                        signUpBtn.setEnabled(true);
                        showProgressDialog(false) ;
                        onVerify();

                    }
                });

    }


    private void onVerify() {



        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog(true);
                if (!checkOtp()) {
                    Toast.makeText(SignupActivity.this, "Enter correct OTP", Toast.LENGTH_SHORT).show();
                    showProgressDialog(false);
                    return;
                }

                String otp = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString() + otp5.getText().toString() + otp6.getText().toString();
                phoneCredential = PhoneAuthProvider.getCredential(verification, otp);


                if (phoneCredential != null) {
                    onSignUpButton();
                }
                 else {
                    signUpBtn.setEnabled(false);
                    Toast.makeText(SignupActivity.this, "Unable to Create Account", Toast.LENGTH_SHORT).show();
                }

            }


        });

    }
    private boolean checkOtp() {

        if (otp1.getText().toString().length() == 0 || otp2.getText().toString().length() == 0 || otp3.getText().toString().length() == 0 || otp4.getText().toString().length() == 0 || otp5.getText().toString().length() == 0 || otp6.getText().toString().length() == 0) {
            return false;
        }
        return true;
    }

    private void onSignUpButton() {
        mAuth = User.getmAuth();

        firstName = firstnameLayout.getEditText().getText().toString();
        lastName = lastnameLayout.getEditText().getText().toString();
        email = emailLayout.getEditText().getText().toString();


        //method to check email and password with accounts and if valid user then update ui
        mAuth.signInWithCredential(phoneCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
                            User.setPhoneNumber(phone);
                            User.setFirstName(firstName);
                            User.setLastName(lastName);
                            User.setNoOfBooks(0);
                            User.setProfilePhotoUrl(null);

                            myRef.child(userID);

                            myRef = myRef.child(userID);
                            myRef.child("FirstName").setValue(firstName);
                            myRef.child("LastName").setValue(lastName);
                            myRef.child("Email").setValue(email);
                            myRef.child("noOfBooks").setValue(0);
                            myRef.child("PhoneNumber").setValue(User.getPhoneNumber());
                            myRef.child("ProfilePhotoUrl").setValue(null);


                            Toast.makeText(getApplicationContext(), "Signup Successfull", Toast.LENGTH_SHORT).show();
                            updateUi(user);
                            User.getUserData();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createAccountWithEmail:failure", task.getException());

//                            if(task.getException() instanceof )
                            signUpBtn.setEnabled(false);

                            Toast.makeText(getApplicationContext(), "Unable To Create Account", Toast.LENGTH_SHORT).show();


                        }
                        showProgressDialog(false);
                    }

                    private void updateUi(FirebaseUser user) {

                        if (user != null) {
                            //if user is not empty

                            if (profileUri != null)
                                uploadProfilePic(profileUri);

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


    private boolean createAccount(String firstName, String lastName, String email, String phone) {

        firstnameLayout.setError(null);
        lastnameLayout.setError(null);
        emailLayout.setError(null);
        phoneLayout.setError(null);


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

        if (!checkPhone(phone)) {
            phoneLayout.setError("Enter Correct Phone Number");
            return false;
        } else {
            phoneLayout.setError(null);
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

    private boolean checkPhone(String phone) {

        if (phone == null)
            return false;
        else
            return Patterns.PHONE.matcher(phone).matches();

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
                            Toast.makeText(SignupActivity.this, task.getResult().toString(), Toast.LENGTH_LONG).show();
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
            }
        });
    }

    private void showProgressDialog(Boolean show){
        if(show) {
            progressDialog = new ProgressDialog(SignupActivity.this);
            progressDialog.setTitle("Loading...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

        }
        else{
            progressDialog.cancel();
        }
    }
}