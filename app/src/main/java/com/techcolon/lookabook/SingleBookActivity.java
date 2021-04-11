package com.techcolon.lookabook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.URL;

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
        setContentView(R.layout.activity_single_book);
        Intent i = getIntent();
        Book book = (Book) i.getSerializableExtra("SingleBook");
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
        showProgressDialog(false);


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
                        Thread getImage = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    URL url = new URL(imageUrl);
                                    imageBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        images[imagescount].setImageBitmap(imageBitmap);
                                        imagescount++;

                                    }
                                });

                            }

                        });
                        getImage.start();


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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
}