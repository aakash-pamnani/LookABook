package com.techcolon.lookabook;

import java.util.ArrayList;

class Book {

    private String bookID;
    private ArrayList<String> imageLinks;
    private String titleOfBook;
    private String ISBN;
    private String department;
    private int semester;
    private String price;
    private String descriptionOfBook;

    public Book(String titleOfBook, String ISBN, String department, int semester, String price, String descriptionOfBook) {
        this.titleOfBook = titleOfBook;
        this.department = department;
        this.semester = semester;
        this.price = price;
        this.descriptionOfBook = descriptionOfBook;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public ArrayList<String> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(ArrayList<String> imageLinks) {
        this.imageLinks = imageLinks;
    }

    public String getTitleOfBook() {
        return titleOfBook;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getDepartment() {
        return department;
    }

    public int getSemester() {
        return semester;
    }

    public String getPrice() {
        return price;
    }

    public String getDescriptionOfBook() {
        return descriptionOfBook;
    }
}
