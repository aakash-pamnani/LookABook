package com.techcolon.lookabook;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class BookListFragment extends Fragment {


    FloatingActionButton addNewBookFabBtn;


    public BookListFragment() {
        // Required empty public constructor
    }


    public static BookListFragment newInstance(String param1, String param2) {
        BookListFragment fragment = new BookListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book_list, container, false);
        addNewBookFabBtn = v.findViewById(R.id.addbookfabbtn);
        addNewBookFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AddBookActivity.class);
                startActivity(i);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}