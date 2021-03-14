package com.techcolon.lookabook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;


public class UserBookListFragment extends Fragment {

    MaterialToolbar toolbar;

    public UserBookListFragment() {
        // Required empty public constructor
    }


    public static UserBookListFragment newInstance(String param1, String param2) {
        UserBookListFragment fragment = new UserBookListFragment();
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
            v = inflater.inflate(R.layout.fragment_user_book_list, container, false);
            toolbar = v.findViewById(R.id.topAppBar);
            toolbar.setTitle("My Books");

        } else {
            //if user null(not logged in)
            v = inflater.inflate(R.layout.fragment_user_book_list, container, false);
        }


        return v;

    }
}

