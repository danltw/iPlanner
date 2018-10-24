package com.project42.iplanner.Accounts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.project42.iplanner.Home.HomeActivity;
import com.project42.iplanner.R;

public class LoginActivity extends AppCompatActivity {
    private static final int SUCCESS = 0;
    private static final int USERNAME_OCCUPIED = 1;
    private static final int USERNAME_INVALID = 2;
    private static final int PASSWORD_INCORRECT = 3;

    public LoginActivity(){

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login_ui);

        final Button login_button = findViewById(R.id.login_button);

        if (savedInstanceState == null) {
            Fragment fragment = new LoginUI();

            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack();

            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickLogin(view);
                }
            });
        }
    }

    public void onClickLogin(View view){
        int status;

        final String username = ((EditText)findViewById(R.id.login_username)).getText().toString();
        final String password = ((EditText)findViewById(R.id.login_password)).getText().toString();

        status = AccountController.login(username, password);
        status=0; //placeholder to test transition to home page

        if (status == SUCCESS){
            Toast.makeText(this, "Logging in...", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
        else if (status == USERNAME_INVALID){
            Toast.makeText(this, "Wrong username!", Toast.LENGTH_LONG).show();
        }
        else if (status == PASSWORD_INCORRECT){
            Toast.makeText(this, "Wrong password!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "(DEBUG) Uncaught error!", Toast.LENGTH_LONG).show();
        }
    }
}
