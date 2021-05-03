package com.techcolon.lookabook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class SingleBookActivity extends AppCompatActivity {
    private TextView titleTv, fieldtv, departmenttv, semestertv, pricetv, isbntv, descriptiontv, listedbytv;
    private Button callUser;
    int imagescount = 0;
    private MaterialToolbar toolbar;
    private ProgressDialog progressDialog;
    private ImageView[] images = new ImageView[5];
    private Bitmap imageBitmap;
    private String firstName = null, lastName = null, phoneNumber = null;

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

        setContentView(R.layout.activity_single_book);

        checkNetwork();

        Intent i = getIntent();
        Book book = (Book) i.getSerializableExtra("SingleBook");
        Boolean isUserBook = i.getBooleanExtra("isUserBook", false);
        toolbar = findViewById(R.id.topAppBar);
        toolbar.setTitle(book.getTitleOfBook());

        if (isUserBook) {
            toolbar.inflateMenu(R.menu.singlebooklist_menu);

            toolbar.getMenu().findItem(R.id.editbook).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    return false;
                }
            });

            toolbar.getMenu().findItem(R.id.deletebook).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (Book.deleteBook(book)) {
                        Toast.makeText(SingleBookActivity.this, "Book Deleted Succesfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SingleBookActivity.this, "Unable to delete book", Toast.LENGTH_SHORT).show();

                    }
                    finish();
                    return false;
                }
            });
        } else {
            toolbar.getMenu().clear();
        }
        showProgressDialog(true);
        images[0] = findViewById(R.id.imageview0);
        images[1] = findViewById(R.id.imageview1);
        images[2] = findViewById(R.id.imageview2);
        images[3] = findViewById(R.id.imageview3);
        images[4] = findViewById(R.id.imageview4);
        toolbar = findViewById(R.id.topAppBar);
        titleTv = findViewById(R.id.titletv);
        fieldtv = findViewById(R.id.fieldtv);
        departmenttv = findViewById(R.id.departmenttv);
        semestertv = findViewById(R.id.semestertv);
        pricetv = findViewById(R.id.pricetv);
        isbntv = findViewById(R.id.isbntv);
        descriptiontv = findViewById(R.id.descriptiontv);
        listedbytv = findViewById(R.id.listedbytv);
        callUser = findViewById(R.id.callUser);

        titleTv.setText(book.getTitleOfBook());
        fieldtv.setText(book.getField());
        departmenttv.setText(book.getDepartment());
        semestertv.setText(book.getSemester());
        pricetv.setText(book.getPrice() + "");
        isbntv.setText(book.getISBN());
        descriptiontv.setText(book.getDescriptionOfBook());

        getCurrData(book);
        getImages(book);


        callUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.getmAuth().getCurrentUser() != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:+91" + phoneNumber));
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "You must be Logged In first.", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void getImages(Book book) {

        DatabaseReference ref = User.getDatabase().getReference().child("Books").child(book.getBookID()).child("Images");
        if (!(ref == null)) {

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot snapshotimages : snapshot.getChildren()) {
                        String imageUrl = snapshotimages.getValue(String.class);
                        Glide.with(getApplicationContext()).load(imageUrl).into(images[imagescount]);
                        imagescount++;


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {

            return;
        }
    }

    private void getCurrData(Book book) {
        User.getDatabase().getReference().child("Users").child(book.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firstName = snapshot.child("FirstName").getValue(String.class);
                lastName = snapshot.child("LastName").getValue(String.class);
                phoneNumber = snapshot.child("PhoneNumber").getValue(String.class);
                listedbytv.setText(firstName + " " + lastName);
                callUser.setText("Contact " + firstName);
                showProgressDialog(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showProgressDialog(false);
                Toast.makeText(getApplicationContext(), "There was some error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog(Boolean show) {
        if (show) {
            progressDialog = new ProgressDialog(SingleBookActivity.this);
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
        ConnectivityManager connectivityManager = (ConnectivityManager) SingleBookActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        if (!connected) {
            new AlertDialog.Builder(SingleBookActivity.this).setTitle("Something Went Wrong...")
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