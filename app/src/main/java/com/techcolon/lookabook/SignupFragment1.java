package com.techcolon.lookabook;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

public class SignupFragment1 extends Fragment {



    private TextInputLayout phoneLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = null;
        v = inflater.inflate(R.layout.fragment_signup1, container, false);

        phoneLayout = v.findViewById(R.id.phonenumber);
        String phone;


//        if (!checkPhone(phone)) {
//            phoneLayout.setError("Enter correct Phone Number");
//            return false;
//        } else {
//
//        }




        return v;
    }
    private boolean checkPhone(String phone) {

        if (phone == null)
            return false;
        else
            return Patterns.PHONE.matcher(phone).matches();

    }
}
