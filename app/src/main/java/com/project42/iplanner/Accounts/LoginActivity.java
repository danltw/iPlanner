package com.project42.iplanner.Accounts;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.project42.iplanner.Home.HomeActivity;
import com.project42.iplanner.R;
import com.project42.iplanner.Utilities.SharedManager;

public class LoginActivity extends AppCompatActivity implements AccountController.OnCallbackResponse{

    public static final String LOGIN_STATE = "LOGIN_STATE";
    public static final String REG_STATE = "REG_STATE";
    public static String currentState = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initialize SharedManager
        SharedManager.getInstance().initialize(this.getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (isUserLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.login_container, new LoginFragment());
            currentState = LOGIN_STATE;
            ft.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    private boolean isUserLoggedIn() {
        if (SharedManager.getInstance().getUser() != null)
            return true;
        return false;
    }

    @Override
    public void onBackPressed() {
        if (currentState.equals(LOGIN_STATE)) {
            finish();
        }
        super.onBackPressed();
    }

    @Override
    public void onRegResponse(String response) {
        if (response != null) {
            Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(LoginActivity.this, "Successfully Registered!", Toast.LENGTH_LONG).show();
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onLoginResponse(Account a) {
        if (a == null)
        {
            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
        }
        else {
            SharedManager.getInstance().setUser(a.getUsername()); // save user into SP
            SharedManager.getInstance().setEmail(a.getEmail()); // save email into SP
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onUpdateResponse(String response) {
        if (response != null) {
            Toast.makeText(LoginActivity.this, "Updated Successfully", Toast.LENGTH_LONG).show();
        }
    }
}
