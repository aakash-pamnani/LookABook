package com.techcolon.lookabook;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;

public class AddBookActivity extends AppCompatActivity {

    // Variables
    String bookId;
    String bookTitle;
    String semesterOfBook;
    String isbn;
    String price;
    String fieldOfBook;
    String departmentOfField;
    String descriptionOfBook;

    // Resources
    private TextInputLayout bookTitleTextView, descriptionTextView, priceTextView, isbnTextView;
    private AutoCompleteTextView departmentAutoComplete, fieldAutoComplete, semesterAutoComplete;
    private Button addBookBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        // remove bottom this after flash is created

        fieldAutoComplete = findViewById(R.id.fieldautocomplete);
        departmentAutoComplete = findViewById(R.id.departmentautocomplete);
        semesterAutoComplete = findViewById(R.id.semesterautocomplete);
        bookTitleTextView = findViewById(R.id.booktitle);
        descriptionTextView = findViewById(R.id.descriptiontextview);
        priceTextView = findViewById(R.id.pricetextview);
        isbnTextView = findViewById(R.id.isbntextview);
        addBookBtn = findViewById(R.id.addbookbtn);


        //make this 3 fields online on database
        String[] fields = {"Engineering"};
        String[] dept = {"Computer", "Mechanical", "Electrical"};
        String[] sem = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        ArrayAdapter arrayAdapterFields = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, fields);
        ArrayAdapter arrayAdapterDepts = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, dept);
        ArrayAdapter arrayAdapterSem = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, sem);

        fieldAutoComplete.setAdapter(arrayAdapterFields);
        departmentAutoComplete.setAdapter(arrayAdapterDepts);
        semesterAutoComplete.setAdapter(arrayAdapterSem);


        addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookTitle = bookTitleTextView.getEditText().getText().toString();
                isbn = isbnTextView.getEditText().getText().toString();
                price = priceTextView.getEditText().getText().toString();
                descriptionOfBook = descriptionTextView.getEditText().getText().toString();


                fieldAutoComplete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        fieldOfBook = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(AddBookActivity.this, "Select Your Field", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

                departmentAutoComplete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        departmentOfField = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(parent.getContext(), "Select Your Department", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

                semesterAutoComplete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        semesterOfBook = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(parent.getContext(), "Select Your Semester", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });


                if (!checkAllFields(bookTitle, isbn, descriptionOfBook)) {
                    return;
                }

                User.initializeFirebaseDatabase(); //splach screen
                User.intializemAuth();  // /splash screen//

                DatabaseReference databaseReference = User.getFirebaseDatabase().getReference();

                bookId = databaseReference.push().getKey();
                String userId = User.getmAuth().getCurrentUser().getUid();

                Book currBook = new Book(bookId, userId, bookTitle, isbn, departmentOfField, fieldOfBook, semesterOfBook, price, descriptionOfBook);
                databaseReference.child("Books").child(bookId).setValue(currBook);

                User.addBooks(currBook);

                //adding bookid to user child
                databaseReference.child("Users").child(userId).child("Books Added by User").child("Book" + (User.getNoOfBooks() + 1)).setValue(bookId);
                User.setNoOfBooks(User.getNoOfBooks() + 1);
                databaseReference.child("Users").child(userId).child("noOfBooks").setValue(User.getNoOfBooks());


                Toast.makeText(AddBookActivity.this, "Book Added Successfully", Toast.LENGTH_SHORT).show();
                finish();

            }


        });


    }


    private boolean checkAllFields(String bookTitle, String isbn, String descriptionOfBook) {

        bookTitleTextView.setError(null);
        isbnTextView.setError(null);
        descriptionTextView.setError(null);


        if (!checkString(bookTitle)) {
            bookTitleTextView.setError("Book title should not be less than 4");
            return false;
        } else {
            bookTitleTextView.setError(null);

        }

        if (!checkIsbn(isbn)) {
            isbnTextView.setError("ISBN should be 10 or 13");
            return false;

        } else {

            isbnTextView.setError(null);
        }

        if (!checkDescription(descriptionOfBook)) {
            descriptionTextView.setError("Description should at least have 10 characters");
            return false;
        } else {
            descriptionTextView.setError(null);
        }

        return true;
    }

    private boolean checkIsbn(String isbn) {
        if (isbn.length() == 0 || isbn.length() == 10 || isbn.length() == 13) {
            return true;
        }
        return false;

    }

    private boolean checkString(String name) {
        if (name.length() <= 3) {
            return false;
        }
        return true;
    }

    private boolean checkDescription(String desc) {
        if (desc.length() <= 10) {
            return false;
        }
        return true;
    }
}