package com.project42.iplanner.Accounts;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.project42.iplanner.Home.HomeActivity;
import com.project42.iplanner.R;

public class RegisterActivity extends Activity {
    private static final int SUCCESS = 0;
    private static final int USERNAME_OCCUPIED = 1;
    private static final int USERNAME_INVALID = 2;
    private static final int PASSWORD_INCORRECT = 3;

    public RegisterActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);

        final Button register = findViewById(R.id.register_button);
        final Button switch_mode = findViewById(R.id.switch_mode_tologin);

        if (savedInstanceState == null) {
            Fragment fragment = new RegisterUI();

//            FragmentManager manager = getSupportFragmentManager();
//            manager.popBackStack();

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickRegister(view);
                }
            });
            switch_mode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickSwitchMode(view);
                }
            });
        }
    }

    private void onClickRegister(View view){
        int status;

        final String username = ((EditText)findViewById(R.id.login_username)).getText().toString();
        final String password = ((EditText)findViewById(R.id.login_password)).getText().toString();

        status = AccountController.createAccount(username, password);
        status=0; //placeholder to test transition to home page

        if (status == SUCCESS){
            Toast.makeText(this, "Logging in...", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
        else if (status == USERNAME_INVALID){
            Toast.makeText(this, "Invalid username!", Toast.LENGTH_LONG).show();
        }
        else if (status == USERNAME_OCCUPIED){
            Toast.makeText(this, "Username unavailable!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "(DEBUG) Uncaught error!", Toast.LENGTH_LONG).show();
        }
    }

    private void onClickSwitchMode(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
