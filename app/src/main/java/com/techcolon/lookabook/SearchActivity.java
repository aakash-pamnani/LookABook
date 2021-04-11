package com.techcolon.lookabook;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements InfiniteScrollListener.OnLoadMoreListener {

    SearchView search;
    RecyclerView rcv;
    //    LinearLayout loading;
    private BookAdapter mAdapter;
    private InfiniteScrollListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search = findViewById(R.id.search_view);
        rcv = findViewById(R.id.recyclerview);
        mAdapter = new BookAdapter(this);

        retriveData(null, 10);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcv.setLayoutManager(layoutManager);
        listener = new InfiniteScrollListener(layoutManager, this);
        InfiniteScrollListener listener = new InfiniteScrollListener(layoutManager, this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rcv.getContext(),
                layoutManager.getOrientation());
        rcv.addItemDecoration(dividerItemDecoration);

        listener.setLoaded();
        rcv.setAdapter(mAdapter);
        rcv.addOnScrollListener(listener);


    }


    public void retriveData(String bookId, int size) {


        Query query;

        if (bookId == null) {
//            loading.setVisibility(View.VISIBLE);
            query = User.getDatabase().getReference()
                    .child("Books");
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
                // loading.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.setLoaded();
                // loading.setVisibility(View.INVISIBLE);


            }
        });


    }

    @Override
    public void onLoadMore() {

    }
}