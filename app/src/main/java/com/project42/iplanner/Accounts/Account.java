package com.project42.iplanner.Accounts;

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


    public Account(String username, String password){
        this.username = username;
        this.password = password;

        //IMPLEMENT PUTTING THIS INTO SQL

    }

    public String getPassword(String username){
        String password = null;

        return password;
    }
}
