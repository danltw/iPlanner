package com.project42.iplanner.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import static android.content.Context.MODE_PRIVATE;

public class SharedManager {

    private static SharedManager mInstance;

    private static Context mCtx;

    private SharedManager() {
    }

    public void initialize(Context context) {
        if (context !=null)
            mCtx = context;
    }

    public static SharedManager getInstance() {
        if (mInstance == null)
            mInstance = new SharedManager();
        return mInstance;
    }

    public void setUser(String username) {
        SharedPreferences.Editor sp = mCtx.getSharedPreferences("username", MODE_PRIVATE).edit();
        sp.putString("username", username);
        sp.apply();
    }

    public String getUser() {
        SharedPreferences sp = mCtx.getSharedPreferences("username", MODE_PRIVATE);
        return sp.getString("username", null);
    }

    public void setEmail(String email) {
        SharedPreferences.Editor sp = mCtx.getSharedPreferences("email", MODE_PRIVATE).edit();
        sp.putString("email", email);
        sp.apply();
    }

    public String getEmail() {
        SharedPreferences sp = mCtx.getSharedPreferences("email", MODE_PRIVATE);
        return sp.getString("email", null);
    }

    public void clearUser() {
        SharedPreferences.Editor sp = mCtx.getSharedPreferences("username", MODE_PRIVATE).edit();
        sp.remove("username");
    }
}
