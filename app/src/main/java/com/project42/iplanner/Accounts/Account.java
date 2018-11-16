package com.project42.iplanner.Accounts;

/** Represents a user.
 * @author Team42
 * @version 1.0
 */
public class Account {

    private String username;
    private String email;
    private String phoneNumber;

    /** Creates a user with the specified username and email address.
     * @param username The user's username.
     * @param email The user's email address.
     */
    Account(String username, String email) {
        this.username = username;
        this.email = email;
    }

    /**
     * Get the current user's username property.
     * The username property is essential to store user's information and control the displayed data.
     * @return The current user's username in string.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Set the current user's username property.
     * The username property is essential to store user's information and control the displayed data.
     * @param username the username of the current User.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the current user's email address.
     * The email property is an optional field that may be used to implement
     * more features in the future.
     * @return The current user's email address in string.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Set the current user's email address.
     * The email property is an optional field that may be used to implement
     * more features in the future.
     * @param email the email address of the current User.
     */
    public void setEmail(String email) {
        this.email = email;
    }

}