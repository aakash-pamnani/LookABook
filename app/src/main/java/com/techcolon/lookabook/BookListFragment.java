package com.techcolon.lookabook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class BookListFragment extends Fragment {


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


        //Retrive data of book from database


        // set the data in recycler view
        rcv = v.findViewById(R.id.recyclerviewbook);
        mAdapter = new BookAdapter();
        Book.getBooksFromDatabase(mAdapter);
        rcv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        rcv.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rcv.getContext(),
                layoutManager.getOrientation());
        rcv.addItemDecoration(dividerItemDecoration);


        rcv.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        Book.getBooksFromDatabase(mAdapter);
    }
}