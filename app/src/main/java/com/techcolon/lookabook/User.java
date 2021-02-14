package com.techcolon.lookabook;

class User {

    private static String email;
    private static String password;
    private static String isName;
    private static String contactNumber;

    public User(String email, String password, String isName, String contactNumber) {
        this.email = email;
        this.password = password;
        this.isName = isName;
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getName() {
        return this.isName;
    }

    public String getContact() {
        return this.contactNumber;
    }
}
