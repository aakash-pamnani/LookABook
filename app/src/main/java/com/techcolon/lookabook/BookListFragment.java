package com.techcolon.lookabook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BookListFragment extends Fragment implements InfiniteScrollListener.OnLoadMoreListener {


    private RecyclerView rcv;
    private BookAdapter mAdapter;
    LinearLayout loading;
    private InfiniteScrollListener listener;


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
        loading = v.findViewById(R.id.loading);

        // set the data in recycler view
        rcv = v.findViewById(R.id.recyclerviewbook);

        mAdapter = new BookAdapter(getContext());


        retriveData(null, 10);
        rcv.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        rcv.setLayoutManager(layoutManager);
        listener = new InfiniteScrollListener(layoutManager, this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rcv.getContext(),
                layoutManager.getOrientation());
        rcv.addItemDecoration(dividerItemDecoration);

        listener.setLoaded();
        rcv.setAdapter(mAdapter);
        rcv.addOnScrollListener(listener);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onLoadMore() {

        rcv.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.addNullData();
                retriveData(mAdapter.getLastKey(), 10);
            }
        });


    }

    public void retriveData(String bookId, int size) {


        Query query;

        if (bookId == null) {
            loading.setVisibility(View.VISIBLE);
            query = User.getDatabase().getReference()
                    .child("Books")
                    .orderByKey()
                    .limitToFirst(size);
        } else {

            query = User.getDatabase().getReference()
                    .child("Books")
                    .orderByKey()
                    .startAt(bookId)
                    .limitToFirst(size);
        }


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Book> books = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Book book = snapshot.getValue(Book.class);

                    if (book.getBookID().equals(bookId)) {

                    } else
                        books.add(book);
                }
                if (bookId != null) //retriveing data more than 1st time
                    mAdapter.removeNull();


                mAdapter.addData(books);
                listener.setLoaded();
                loading.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.setLoaded();


            }
        });


    }


}