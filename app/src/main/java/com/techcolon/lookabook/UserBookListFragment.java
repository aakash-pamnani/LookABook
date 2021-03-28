package com.techcolon.lookabook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


public class UserBookListFragment extends Fragment {


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


        } else {
            //if user null(not logged in)
            v = inflater.inflate(R.layout.fragment_user_book_list, container, false);
        }


        return v;

    }
}

