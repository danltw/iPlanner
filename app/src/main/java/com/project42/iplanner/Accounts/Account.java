package com.project42.iplanner.Accounts;

public class Account {

    private String username;
    private String email;
    private String phoneNumber;

    Account(String username){
        this.username = username;
    }

    Account(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}