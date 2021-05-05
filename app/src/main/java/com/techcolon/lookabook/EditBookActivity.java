package com.techcolon.lookabook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class EditBookActivity extends AppCompatActivity {

    // Variables
    String bookTitle;
    String semesterOfBook;
    Book book;
    String isbn;
    int price;
    int imagescount = 0;
    Book currBook;
    String fieldOfBook;
    String departmentOfField;
    String descriptionOfBook;
    private ImageView[] images = new ImageView[5];
    ArrayList<String> fieldsArray;
    Uri[] imageuri = new Uri[5];
    int j;
    private ProgressDialog progressDialog;

    // Resources
    private TextInputLayout bookTitleTextView, descriptionTextView, priceTextView, isbnTextView, departmentLayout, fieldLayout, semseterLayout;
    private AutoCompleteTextView departmentAutoComplete, fieldAutoComplete, semesterAutoComplete;
    private Button updateDetails;

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

        setContentView(R.layout.activity_edit_book);
        checkNetwork();

        Intent i = getIntent();
        book = (Book) i.getSerializableExtra("BookToUpdate");

        fieldsArray = new ArrayList<>();




        fieldAutoComplete = findViewById(R.id.fieldautocomplete);
        departmentAutoComplete = findViewById(R.id.departmentautocomplete);
        semesterAutoComplete = findViewById(R.id.semesterautocomplete);
        fieldLayout = findViewById(R.id.fieldlayout);
        departmentLayout = findViewById(R.id.departmentlayout);
        semseterLayout = findViewById(R.id.semseterlayout);
        bookTitleTextView = findViewById(R.id.booktitle);
        descriptionTextView = findViewById(R.id.descriptiontextview);
        priceTextView = findViewById(R.id.pricetextview);
        isbnTextView = findViewById(R.id.isbntextview);
        updateDetails = findViewById(R.id.updateDetails);

        bookTitleTextView.getEditText().setText(book.getTitleOfBook());
        isbnTextView.getEditText().setText(book.getISBN());
        priceTextView.getEditText().setText(Integer.toString(book.getPrice()));
        descriptionTextView.getEditText().setText(book.getDescriptionOfBook());



        images[0] = findViewById(R.id.imageview0);
        images[1] = findViewById(R.id.imageview1);
        images[2] = findViewById(R.id.imageview2);
        images[3] = findViewById(R.id.imageview3);
        images[4] = findViewById(R.id.imageview4);

        images[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        images[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
        images[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });
        images[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });
        images[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 4);
            }
        });

        //         make this 3 fields online on database
        ArrayList<String> fields = new ArrayList<>();
        ArrayList<String> dept = new ArrayList<>();
        String[] sem = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        departmentLayout.setEnabled(false);

        showProgressDialog(true);
        User.getDatabase().getReference().child("Fields").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshotFields : snapshot.getChildren()) {

                    String field = snapshotFields.getKey();
                    fields.add(field);

                }
                showProgressDialog(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ArrayAdapter arrayAdapterFields = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, fields);
        ArrayAdapter arrayAdapterDepts = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, dept);
        ArrayAdapter arrayAdapterSem = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, sem);

        fieldAutoComplete.setText(book.getField(),false);
        fieldAutoComplete.setAdapter(arrayAdapterFields);

        departmentAutoComplete.setText(book.getDepartment(), false);
        departmentAutoComplete.setAdapter(arrayAdapterDepts);

        semesterAutoComplete.setText(book.getSemester(),false);
        semesterAutoComplete.setAdapter(arrayAdapterSem);


        fieldAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                fieldOfBook = adapterView.getItemAtPosition(i).toString();

                showProgressDialog(true);
                dept.clear();
                User.getDatabase().getReference().child("Fields").child(fieldOfBook).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshotFields : snapshot.getChildren()) {

                            String field = snapshotFields.getValue(String.class);
                            dept.add(field);

                        }
                        showProgressDialog(false);
                        departmentLayout.setEnabled(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        departmentAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                departmentOfField = adapterView.getItemAtPosition(i).toString();
            }
        });

        semesterAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                semesterOfBook = adapterView.getItemAtPosition(i).toString();
            }

        });

        getImages(book);

        updateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(true);
                bookTitle = bookTitleTextView.getEditText().getText().toString();
                isbn = isbnTextView.getEditText().getText().toString();
                price = Integer.parseInt(priceTextView.getEditText().getText().toString().length() == 0 ? "0" : priceTextView.getEditText().getText().toString());
                descriptionOfBook = descriptionTextView.getEditText().getText().toString();

//                semesterOfBook = semesterAutoComplete.getText().toString();
//                departmentOfField = departmentAutoComplete.getText().toString();
//                fieldOfBook = fieldAutoComplete.getText().toString();


                if (!checkAllFields(bookTitle, isbn, descriptionOfBook, fieldOfBook, departmentOfField, semesterOfBook, imageuri)) {
                    showProgressDialog(false);
                    return;
                }

                DatabaseReference databaseReference = User.getDatabase().getReference();
                String userId = User.getmAuth().getCurrentUser().getUid();
                uploadImages();
                currBook = new Book(book.getBookID(), userId, bookTitle, isbn, departmentOfField, fieldOfBook, semesterOfBook, price, descriptionOfBook);
                databaseReference.child("Books").child(book.getBookID()).setValue(currBook);

                for (int i = 0; i < User.getUserBooks().size(); i++) {
                    if (User.getUserBooks().get(i).getBookID().equals(book.getBookID())) {
                        User.getUserBooks().get(i).setDepartment(currBook.getDepartment());
                        User.getUserBooks().get(i).setField(currBook.getField());
                        User.getUserBooks().get(i).setSemester(currBook.getSemester());
                        User.getUserBooks().get(i).setTitleOfBook(currBook.getTitleOfBook());
                        User.getUserBooks().get(i).setIsbn(currBook.getISBN());
                        User.getUserBooks().get(i).setDescriptionOfBook(currBook.getDescriptionOfBook());
                        User.getUserBooks().get(i).setPrice(currBook.getPrice());
                        break;
                    }
                }

                showProgressDialog(false);
                Toast.makeText(EditBookActivity.this, "Book Details Updated Successfully", Toast.LENGTH_SHORT).show();
                finish();

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
    private void checkNetwork() {
        //checking network state
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) EditBookActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        if (!connected) {
            new AlertDialog.Builder(EditBookActivity.this).setTitle("Something Went Wrong...")
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                imageuri[0] = data.getData();
                images[0].setImageURI(imageuri[0]);
            }
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                imageuri[1] = data.getData();
                images[1].setImageURI(imageuri[1]);
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                imageuri[2] = data.getData();
                images[2].setImageURI(imageuri[2]);
            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                imageuri[3] = data.getData();
                images[3].setImageURI(imageuri[3]);
            }
        } else {
            if (resultCode == RESULT_OK) {
                imageuri[4] = data.getData();
                images[4].setImageURI(imageuri[4]);
            }
        }
    }

    private void showProgressDialog(Boolean show) {
        if (show) {
            progressDialog = new ProgressDialog(EditBookActivity.this);
            progressDialog.setTitle("Loading...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

        } else {
            progressDialog.cancel();
        }
    }

    private void uploadImages() {

        for (int i = 0; i < 5; i++) {
            j = i;
            if (imageuri[i] == null)
                continue;

            else {
                String randomKey = User.getDatabase().getReference().child("Books").child(book.getBookID()).child("Images").push().getKey();
                User.getStorageReference().child("Books").child(book.getBookID()).child(randomKey).putFile(imageuri[i]).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//

                        User.getStorageReference().child("Books").child(book.getBookID()).child(randomKey).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {

                                URL imageUrl = null;
                                try {
                                    imageUrl = new URL(task.getResult().toString());
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                                User.getDatabase().getReference().child("Books").child(book.getBookID()).child("Images").child(randomKey).setValue(imageUrl.toString());

                            }
                        });


                        if (j == 4) {
                            showProgressDialog(false);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Some error occurred while uploading Images", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }


    }

    private boolean checkAllFields(String bookTitle, String isbn, String descriptionOfBook, String fieldOfBook, String departmentOfField, String semesterOfBook, Uri[] imageuri) {

        bookTitleTextView.setError(null);
        isbnTextView.setError(null);
        descriptionTextView.setError(null);
        fieldLayout.setError(null);
        departmentLayout.setError(null);
        semseterLayout.setError(null);


        //check for at least one image
//        if(!checkAtLeastOneImage(imageuri)){
//            Toast.makeText(this, "Select At least One Image", Toast.LENGTH_SHORT).show();
//            return false;
//        }


        if (!checkString(bookTitle)) {
            bookTitleTextView.setError("Book title should not be less than 4");
            return false;
        } else {
            bookTitleTextView.setError(null);

        }

        if (fieldOfBook == null) {
            fieldLayout.setError("This field cannot be empty");

            return false;
        } else {
            fieldLayout.setError(null);
        }

        if (departmentOfField == null) {
            departmentLayout.setError("This field cannot be empty");
            return false;
        } else {
            departmentLayout.setError(null);
        }

        if (semesterOfBook == null) {
            semseterLayout.setError("This field cannot be empty");
            return false;
        } else {
            semseterLayout.setError(null);
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

    private boolean checkAtLeastOneImage(Uri[] imageuri) {

        if (imageuri[0] == null && imageuri[1] == null && imageuri[2] == null && imageuri[3] == null && imageuri[4] == null) {
            return false;
        } else {
            return true;
        }

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