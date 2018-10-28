package com.project42.iplanner.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedManager {

    private static SharedManager mInstance;

    private static Context mCtx;

    private SharedManager() {
    }

    public static SharedManager getInstance(Context context) {
        if (mInstance == null)
            mInstance = new SharedManager();
        mCtx = context;
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

    public void clearUser() {
        SharedPreferences.Editor sp = mCtx.getSharedPreferences("username", MODE_PRIVATE).edit();
        sp.remove("username");
    }
}
