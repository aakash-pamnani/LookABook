package com.techcolon.lookabook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;


public class ProfileFragement extends Fragment {

    MaterialToolbar toolbar;

    TextView email;
    TextView firstName;
    TextView lastName;
    TextView noOfBooks;
    TextView phoneNumber;
    ShapeableImageView profilepic;
    private Button signupBtn;
    private TextView loginBtn;
    public ProfileFragement() {
        // Required empty public constructor
    }


    public static ProfileFragement newInstance() {
        ProfileFragement fragment = new ProfileFragement();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = null;
        if (User.getmAuth().getCurrentUser() != null) {
            //user is not null(logged in)

            v = inflater.inflate(R.layout.fragment_profile_user, container, false);


            toolbar = v.findViewById(R.id.topAppBar);
            toolbar.setTitle("Profile");
            toolbar.inflateMenu(R.menu.profile_fragment_menu);


            email = v.findViewById(R.id.emailtv);
            firstName = v.findViewById(R.id.firstnametv);
            lastName = v.findViewById(R.id.lastnametv);
            noOfBooks = v.findViewById(R.id.noofbookstv);
            profilepic = v.findViewById(R.id.profilepic);
            phoneNumber = v.findViewById(R.id.phonenumbertv);


            email.setText(User.getEmail());
            phoneNumber.setText(User.getPhoneNumber());
            firstName.setText(User.getFirstName());
            lastName.setText(User.getLastName());
            noOfBooks.setText(User.getNoOfBooks() + "");
            if (User.getProfilePhotoUrl() != null) {

                Bitmap bmp = null;
                try {
                    bmp = BitmapFactory.decodeStream(User.getProfilePhotoUrl().openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                profilepic.setImageBitmap(bmp);
            }


        } else {
            //if user null(not logged in)
            v = inflater.inflate(R.layout.fragment_profile_nouser, container, false);
            loginBtn = v.findViewById(R.id.login);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), LoginActivity.class);
                    startActivity(i);
                }
            });

            // SignUp button working
            signupBtn = v.findViewById(R.id.signup);
            signupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), SignupActivity.class);
                    startActivity(i);
                }
            });
        }


        return v;
    }



}