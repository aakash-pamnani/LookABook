package com.techcolon.lookabook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
            toolbar = v.findViewById(R.id.topAppBar);
            toolbar.setTitle("Login/Signup");
        }


        return v;
    }
}