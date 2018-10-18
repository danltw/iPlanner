package com.project42.iplanner.Accounts;

import java.util.ArrayList;

public class AccountController {
    private static ArrayList<String> accountList = new ArrayList<String>();
    private static final int SUCCESS = 0;
    private static final int USERNAME_OCCUPIED = 1;
    private static final int USERNAME_INVALID = 2;
    private static final int PASSWORD_INCORRECT = 3;


    private String username;
    private String password;
    private ArrayList<String> profileSettings = new ArrayList<String>();

    private AccountController(String username, String password){
        this.username = username;
        this.password = password;
    }

    public static int createAccount(String username, String password){
        if (accountExists(username)){
            return USERNAME_OCCUPIED;                                       //Username already exists
        }
        if (username.length()>45 || password.length()>45){
            return USERNAME_INVALID;                                       //Password or username too long
        }

        new Account(username, password);
        new AccountController(username, password);
        accountList.add(username);

        return SUCCESS;
    }

    public static int deleteAccount(String username, String password){
        if (!accountExists(username)) return USERNAME_INVALID;
        if (!verifyUser(username, password)) return PASSWORD_INCORRECT;

        //pass all checks, then:
        Account.deleteAccount(username);
        accountList.remove(username);
        return SUCCESS;
    }

    public static int login(String username, String password){
        if (!accountExists(username)) return USERNAME_INVALID;
        if (!verifyUser(username, password)) return PASSWORD_INCORRECT;

        new AccountController(username, password);
        return SUCCESS;
    }

    private static boolean accountExists(String username){
        return (accountList.contains(username));
    }

    public static boolean verifyUser(String username, String password){
        String realPassword;

        Account account = Account.getAccount(username);
        realPassword = account.getPassword(username);

        return (password.equals(realPassword));
    }

    public Account getAccountDetails(){
        //return both email and phone number by returning entire account object
        if (accountExists(this.username) && verifyUser(this.username, this.password)){
            return Account.getAccount(username);
        }

        return null;
    }
    public int updateAccount(String email, String phoneNumber){
        if (!accountExists(this.username)) return USERNAME_INVALID;
        if (!verifyUser(this.username, this.password)) return PASSWORD_INCORRECT;

        Account account = Account.getAccount(this.username);
        account.setEmail(this.username, email);
        account.setPhoneNumber(this.username, phoneNumber);

        return SUCCESS;
    }
}
