package com.techcolon.lookabook;

import android.app.ProgressDialog;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    protected static FirebaseAuth mAuth;
    private static String TAG = "Login";
    private Button logInButton;
    private TextInputLayout phoneLayout, otpLayout;
    private PhoneAuthCredential phoneCredential;
    private ProgressDialog progressDialog;
    String phone,otp;
    private String verification;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        logInButton = findViewById(R.id.loginbutton);
        phoneLayout = findViewById(R.id.phone);
        otpLayout = findViewById(R.id.otp);
        logInButton.setEnabled(false);
    }

    public void onSendOtp(View view){

        //get email and password from edit text layout
        phone = phoneLayout.getEditText().getText().toString();


        //Check if email and password are valid and not empty
        if (!loginAccount(phone)) {
            return;
        }
        showProgressDialog(true);
        onGenerateOtp();

    }


    private void onGenerateOtp() {



        User.setPhoneNumber(phone);


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
                        logInButton.setEnabled(true);
                        showProgressDialog(false) ;
                        onVerify();

                    }
                });

    }


    private void onVerify() {



        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog(true);
                otp = otpLayout.getEditText().getText().toString();
                if (!checkOtp(otp)) {
                    Toast.makeText(getApplicationContext(), "Enter correct OTP", Toast.LENGTH_SHORT).show();
                    showProgressDialog(false);
                    return;
                }

                phoneCredential = PhoneAuthProvider.getCredential(verification, otp);


                if (phoneCredential != null) {
                    onLoginButton();
                }
                else {
                    logInButton.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Unable to Create Account", Toast.LENGTH_SHORT).show();
                }

            }


        });

    }

    private void onLoginButton() {
        mAuth = User.getmAuth();



        //method to check email and password with accounts and if valid user then update ui
        mAuth.signInWithCredential(phoneCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            user = mAuth.getCurrentUser();
                            User.setUser(user);

                            isUserCreated();


                        } else {
                            // If sign in fails, display a message to the user.
                            showProgressDialog(false);
                            Log.w(TAG, "createAccountWithEmail:failure", task.getException());

//                            if(task.getException() instanceof )
                            Toast.makeText(getApplicationContext(), "Unable To Login", Toast.LENGTH_SHORT).show();
                            logInButton.setEnabled(false);


                        }
                        //  showProgressDialog(false);
                    }


                    // private void updateUi(FirebaseUser user) {

                    // }

                });


    }

    private boolean loginAccount(String phone) {


        phoneLayout.setError(null);

        if (!checkPhone(phone)) {
            phoneLayout.setError("This field has some error");
            return false;
        } else {
            phoneLayout.setError(null);
            return true;

        }



    }

    private boolean checkOtp(String otp) {
        return otp.length()==6;
    }

    private boolean checkPhone(String phone) {

        if (phone == null)
            return false;
        else
            return Patterns.PHONE.matcher(phone).matches();

    }
    private void showProgressDialog(Boolean show){
        if (show) {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Loading...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

        } else {
            progressDialog.cancel();
        }
    }

    public void isUserCreated() {

        User.getDatabase().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.hasChild(mAuth.getUid())) {
                    // showProgressDialog(false);
                    Toast.makeText(LoginActivity.this, "Signup First!", Toast.LENGTH_SHORT).show();
                    // isUserCreated = false ;
                    mAuth.getCurrentUser().delete();
                    mAuth.signOut();
                    logInButton.setEnabled(false);

                } else {
                    // isUserCreated=true;

                    Toast.makeText(getApplicationContext(), "Signin Successfull", Toast.LENGTH_SHORT).show();
                    // updateUi(user);
                    User.getUserData();
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
                showProgressDialog(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // return isUserCreated;
    }
}