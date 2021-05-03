package com.techcolon.lookabook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    private BookAdapter mAdapter;
    private InfiniteScrollListener listener;
    ArrayList<Book> books, booksSearch;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting Theme
        SharedPreferences prefs = getSharedPreferences("LookABook_Storage", Context.MODE_PRIVATE);
        int theme = prefs.getInt("Theme", 0);


        if (theme == 1) {
            setTheme(R.style.ThemeLight_LookABook);
        } else if (theme == 2) {
            setTheme(R.style.ThemeDark_LookABook);
        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    setTheme(R.style.ThemeDark_LookABook);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    setTheme(R.style.ThemeLight_LookABook);
                    break;
            }
        }

        setContentView(R.layout.activity_search);


        search = findViewById(R.id.search_view);
        rcv = findViewById(R.id.recyclerview);
        mAdapter = new BookAdapter(this, 0);

        checkNetwork();
        showProgressDialog(true);
        retriveData(null, 10);
        search.requestFocus();
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

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });


    }


    public void retriveData(String bookId, int size) {


        Query query;


            query = User.getDatabase().getReference()
                    .child("Books");


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                books = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Book book = snapshot.getValue(Book.class);

                    if (book.getBookID().equals(bookId)) {

                    } else
                        books.add(book);
                }
                showProgressDialog(false);
                search(null);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.setLoaded();


            }
        });


    }

    @Override
    public void onLoadMore() {

    }

    public void search(String text) {
        booksSearch = new ArrayList<>();
        if (text == null) {
            booksSearch.addAll(books);
        } else {
            for (Book item : books) {
                if (item.getTitleOfBook().toLowerCase().contains(text.toLowerCase())
                        || item.getDepartment().toLowerCase().contains(text.toLowerCase())
                        || item.getField().toLowerCase().contains(text.toLowerCase())) {
                    booksSearch.add(item);
                }
            }
        }
        mAdapter.addNewData(booksSearch);
        listener.setLoaded();


    }

    private void showProgressDialog(Boolean show) {
        if (show) {
            progressDialog = new ProgressDialog(SearchActivity.this);
            progressDialog.setTitle("Loading...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

        } else {
            progressDialog.cancel();
        }
    }

    private void checkNetwork() {
        //checking network state
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        if (!connected) {
            new AlertDialog.Builder(this).setTitle("Something Went Wrong...")
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
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        } else
            return;
    }

}