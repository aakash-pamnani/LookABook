package com.techcolon.lookabook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignupFragment1 extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = null;
        v = inflater.inflate(R.layout.fragment_signup1, container, false);

//        phoneLayout = v.findViewById(R.id.phonenumber);
//
//
//        otp1 = v.findViewById(R.id.otp1);
//        otp2 = v.findViewById(R.id.otp2);
//        otp3 = v.findViewById(R.id.otp3);
//        otp4 = v.findViewById(R.id.otp4);
//        otp5 = v.findViewById(R.id.otp5);
//        otp6 = v.findViewById(R.id.otp6);
//
//
//        generateOtp = v.findViewById(R.id.generateotp);
//        nextBtn = v.findViewById(R.id.nextbtn);
//        nextBtn.setVisibility(View.GONE);
//        generateOtp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String phone = phoneLayout.getEditText().getText().toString();
//                if (!checkPhone(phone)) {
//                    phoneLayout.setError("Enter correct Phone Number");
//                    return;
//                }


//                PhoneAuthOptions options =
//                        PhoneAuthOptions.newBuilder(mAuth)
//                                .setPhoneNumber(phoneNumber)       // Phone number to verify
//                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                                .setActivity(this)                 // Activity (for callback binding)
//                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                                .build();
//                PhoneAuthProvider.verifyPhoneNumber(options);
//
//
//
//                mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//                    @Override
//                    public void onVerificationCompleted(PhoneAuthCredential credential) {
//                        // This callback will be invoked in two situations:
//                        // 1 - Instant verification. In some cases the phone number can be instantly
//                        //     verified without needing to send or enter a verification code.
//                        // 2 - Auto-retrieval. On some devices Google Play services can automatically
//                        //     detect the incoming verification SMS and perform verification without
//                        //     user action.
//                        Log.d(TAG, "onVerificationCompleted:" + credential);
//
//                        signInWithPhoneAuthCredential(credential);
//                    }
//
//                    @Override
//                    public void onVerificationFailed(FirebaseException e) {
//                        // This callback is invoked in an invalid request for verification is made,
//                        // for instance if the the phone number format is not valid.
//                        Log.w(TAG, "onVerificationFailed", e);
//
//                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                            // Invalid request
//                        } else if (e instanceof FirebaseTooManyRequestsException) {
//                            // The SMS quota for the project has been exceeded
//                        }
//
//                        // Show a message and update the UI
//                    }
//
//                    @Override
//                    public void onCodeSent(@NonNull String verificationId,
//                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
//                        // The SMS verification code has been sent to the provided phone number, we
//                        // now need to ask the user to enter the code and then construct a credential
//                        // by combining the code with a verification ID.
//                        Log.d(TAG, "onCodeSent:" + verificationId);
//
//                        // Save verification ID and resending token so we can use them later
//                        mVerificationId = verificationId;
//                        mResendToken = token;
//                    }
//                };


//                PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phone, 60, TimeUnit.SECONDS, getActivity(),
//                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//                            @Override
//                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//                            }
//
//                            @Override
//                            public void onVerificationFailed(@NonNull FirebaseException e) {
//                                Toast.makeText(getActivity(), "There was some error", Toast.LENGTH_SHORT);
//
//                            }
//
//                            @Override
//                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forcedResendingToken) {
//
//                                nextBtn.setVisibility(View.VISIBLE);
//                                String otp = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString() + otp5.getText().toString() + otp6.getText().toString();
//                                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, otp);
//                                phoneAuthCredential.getSmsCode();
//                            }
//
//                        });


//                private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//                    mAuth.signInWithCredential(credential)
//                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (task.isSuccessful()) {
//                                        // Sign in success, update UI with the signed-in user's information
//                                        Log.d(TAG, "signInWithCredential:success");
//
//                                        FirebaseUser user = task.getResult().getUser();
//                                        // Update UI
//                                    } else {
//                                        // Sign in failed, display a message and update the UI
//                                        Log.w(TAG, "signInWithCredential:failure", task.getException());
//                                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                            // The verification code entered was invalid
//                                        }
//                                    }
//                                }
//                            });
//                }
//
//
//            }
        //    });


//        if (!checkPhone(phone)) {
//            phoneLayout.setError("Enter correct Phone Number");
//            return false;
//        } else {
//
//        }


        return v;
    }


}
