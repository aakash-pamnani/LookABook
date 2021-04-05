package com.techcolon.lookabook;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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


        checkNetwork();//will check network, if network available then return here else loop on that method
        retriveData(null, 10);
        // set the data in recycler view
        rcv = v.findViewById(R.id.recyclerviewbook);

        mAdapter = new BookAdapter(getContext());


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
                checkNetwork();
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
                loading.setVisibility(View.INVISIBLE);


            }
        });


    }

    private void checkNetwork() {
        //checking network state
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        if (!connected) {
            new AlertDialog.Builder(getContext()).setTitle("Something Went Wrong...")
                    .setMessage("You are not connected to the internet")
                    .setPositiveButton("retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            checkNetwork();
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getActivity().finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        } else
            return;
    }


}