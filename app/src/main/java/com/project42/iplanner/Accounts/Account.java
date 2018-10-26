package com.project42.iplanner.Accounts;

import com.project42.iplanner.DBConn.DBController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;


public class Account {

    private String username;
    private String password;
    private String email;
    private String phoneNumber;


    Account(String username, String password){
        //Maybe we make this class purely static, there doesn't have to be permanent account objects.
        //They can just be created on demand from the AccountController and deleted after passing information.
        this.username = username;
        this.password = password;

        //IMPLEMENT PUTTING THIS INTO SQL

    }

    static void deleteAccount(String username){
        //remove account from database
    }

    static Account getAccount(String username){
        //getAccount is called by account controller to retrieve the account object from database
        Account account = null;

        //do some sql shit here

        return account;
    }


    String getPassword(String username){
        String password = null;
        //sql implementation
        return password;
    }

    void setEmail(String username, String email){

    }

    void setPhoneNumber(String username, String phoneNumber){

    }
}
