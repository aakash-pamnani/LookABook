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
import androidx.navigation.Navigation;

import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class ProfileFragement extends Fragment {


    TextView email;
    TextView firstName;
    TextView lastName;
    TextView noOfBooks;
    TextView phoneNumber;
    ShapeableImageView profilepic;
    Bitmap bitmap;
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
        if (User.getUser() != null) {
            //user is not null(logged in)

            v = inflater.inflate(R.layout.fragment_profile_user, container, false);


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


                URL imageurl = null;
                try {
                    imageurl = new URL(User.getProfilePhotoUrl());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                getBitmap(imageurl);


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
                    Navigation.findNavController(container).navigate(R.id.bookListFragment);
                }
            });

            // SignUp button working
            signupBtn = v.findViewById(R.id.signup);
            signupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), SignupActivity.class);
                    startActivity(i);
                    Navigation.findNavController(container).navigate(R.id.bookListFragment);
                }
            });
        }


        return v;
    }

    private void getBitmap(URL imageurl) {

        Thread getImageThread = new Thread(new Runnable() {

            @Override
            public void run() {
                URL url = imageurl;
                try {
                    url = new URL(User.getProfilePhotoUrl());
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        profilepic.setImageBitmap(bitmap);
                    }
                });

            }


        });
        getImageThread.start();

    }
}