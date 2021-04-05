package com.techcolon.lookabook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;

public class SingleBookActivity extends AppCompatActivity {
    private TextView titleTv,fieldtv, departmenttv,semestertv,pricetv,isbntv,descriptiontv,listedbytv;
    private Button callUser;
    private String firstName=null,lastName=null,phoneNumber=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_book);
        Intent i = getIntent();
        Book book = (Book)i.getSerializableExtra("SingleBook");
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
        pricetv.setText(book.getPrice());
        isbntv.setText(book.getISBN());
        descriptiontv.setText(book.getDescriptionOfBook());

        getCurrData(book);



        callUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(User.getmAuth().getCurrentUser()!=null){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:+91"+phoneNumber));
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"You must be Logged In first.",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void getCurrData(Book book) {
        User.getDatabase().getReference().child("Users").child(book.getUserID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firstName = snapshot.child("FirstName").getValue(String.class);
                lastName = snapshot.child("LastName").getValue(String.class);
                phoneNumber = snapshot.child("PhoneNumber").getValue(String.class);
                listedbytv.setText(firstName +" "+lastName);
                callUser.setText("Contact "+firstName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"There was some error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}