package com.techcolon.lookabook;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class BookListFragment extends Fragment {


    FloatingActionButton addNewBookFabBtn;
    private RecyclerView rcv;
    private BookAdapter mAdapter;

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

        //Retrive data of book from database
        ArrayList<Book> bookList = new ArrayList<>();

        bookList.add(new Book("test1","121212","abcdef","12121212","comp","","5","2200",""));
        bookList.add(new Book("test1","121212","abcdef","12121212","comp","","5","2200",""));

        bookList.add(new Book("test1","121212","abcdef","12121212","comp","","5","2200",""));

        bookList.add(new Book("test1","121212","abcdef","12121212","comp","","5","2200",""));

        bookList.add(new Book("test1","121212","abcdef","12121212","comp","","5","2200",""));
        bookList.add(new Book("test1","121212","abcdef","12121212","comp","","5","2200",""));
        bookList.add(new Book("test1","121212","abcdef","12121212","comp","","5","2200",""));
        bookList.add(new Book("test1","121212","abcdef","12121212","comp","","5","2200",""));



        // set the data in recycler view
        rcv = v.findViewById(R.id.recyclerviewbook);
        mAdapter = new BookAdapter(bookList);
        rcv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        rcv.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rcv.getContext(),
                layoutManager.getOrientation());
        rcv.addItemDecoration(dividerItemDecoration);


        rcv.setAdapter(mAdapter) ;


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