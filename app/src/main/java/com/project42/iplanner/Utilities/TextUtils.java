package com.project42.iplanner.Utilities;

public class TextUtils {

    public static boolean isEmpty (String text) {
        if (text.isEmpty() || text.equals("") || text.length() < 3)
            return true;

        else
            return false;
    }
}
