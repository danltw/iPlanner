package com.project42.iplanner.Accounts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.project42.iplanner.R;

public class LoginActivity extends AppCompatActivity {
    private static final int SUCCESS = 0;
    private static final int USERNAME_OCCUPIED = 1;
    private static final int USERNAME_INVALID = 2;
    private static final int PASSWORD_INCORRECT = 3;

    private String username = ((EditText)findViewById(R.id.login_username)).getText().toString();
    private String password = ((EditText)findViewById(R.id.login_password)).getText().toString();

    public LoginActivity(){

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            // Load list of Group Channels
            Fragment fragment = new LoginUI();

            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack();

            manager.beginTransaction()
                    .replace(R.id.container_group_channel, fragment)
                    .commit();
        }
    }

    public void onClickLogin(View view){
        int status;
        status = AccountController.login(username, password);

        if (status == SUCCESS){
            Toast.makeText(this, "Logging in...", Toast.LENGTH_LONG).show();
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
