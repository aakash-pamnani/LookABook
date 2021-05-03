package com.techcolon.lookabook;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

class Book implements Serializable {

    private String bookID;
    private String titleOfBook;
    private String isbn;
    private String field;
    private String department;
    private String semester;
    private int price;
    private String descriptionOfBook;
    private String userID;

    public Book(String bookID, String userID, String titleOfBook, String isbn, String department, String field, String semester, int price, String descriptionOfBook) {
        this.titleOfBook = titleOfBook;
        this.department = department;
        this.semester = semester;
        this.price = price;
        this.descriptionOfBook = descriptionOfBook;
        this.isbn = isbn;
        this.bookID = bookID;
        this.userID = userID;
        this.field = field;
    }

    public Book() {
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public String getTitleOfBook() {
        return titleOfBook;
    }

    public void setTitleOfBook(String titleOfBook) {
        this.titleOfBook = titleOfBook;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDescriptionOfBook(String descriptionOfBook) {
        this.descriptionOfBook = descriptionOfBook;
    }

    public String getISBN() {
        return isbn;
    }

    public String getDepartment() {
        return department;
    }

    public String getSemester() {
        return semester;
    }

    public int getPrice() {
        return price;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getDescriptionOfBook() {
        return descriptionOfBook;
    }


    public static boolean deleteBook(Book bookToDelete) {

        //remove from user child
        User.getDatabase().getReference().child("Users").child(User.getmAuth().getUid()).child("Books Added by User").child(bookToDelete.getBookID()).removeValue().isSuccessful();

        //remove from books child
        User.getDatabase().getReference().child("Books").child(bookToDelete.getBookID()).removeValue().isSuccessful();

        //decrement no of books counter
        User.getDatabase().getReference().child("Users").child(User.getmAuth().getUid()).child("noOfBooks").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int currentCountOfBooks = snapshot.getValue(Integer.class);
                User.getDatabase().getReference().child("Users").child(User.getmAuth().getUid()).child("noOfBooks").setValue(currentCountOfBooks - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //update new data at local objects
        User.setNoOfBooks(User.getNoOfBooks() - 1);


        for (int i = 0; i < User.getUserBooks().size(); i++) {
            if (User.getUserBooks().get(i).getBookID().equals(bookToDelete.getBookID())) {
                User.getUserBooks().remove(i);
                break;
            }
        }

        return true;

    }


}
