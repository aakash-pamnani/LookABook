package com.techcolon.lookabook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;


public class SettingsFragment extends Fragment {


    MaterialToolbar toolbar;

    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();

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
        v = inflater.inflate(R.layout.fragment_user_book_list, container, false);
        toolbar = v.findViewById(R.id.topAppBar);
        toolbar.setTitle("Settings");


        return v;

    }
}