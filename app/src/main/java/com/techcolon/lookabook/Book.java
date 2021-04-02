package com.techcolon.lookabook;

import java.io.Serializable;

class Book implements Serializable {

    private String bookID;
    // private ArrayList<String> imageLinks;
    private String titleOfBook;
    private String isbn;
    private String field;
    private String department;
    private String semester;
    private String price;
    private String descriptionOfBook;
    private String userID;

    public Book(String bookID, String userID, String titleOfBook, String isbn, String department, String field, String semester, String price, String descriptionOfBook) {
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
    // public ArrayList<String> getImageLinks() {
    //     return imageLinks;
    // }

    // public void setImageLinks(ArrayList<String> imageLinks) {
    //     this.imageLinks = imageLinks;
    // }

    public String getTitleOfBook() {
        return titleOfBook;
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

    public String getPrice() {
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


}
