package com.project42.iplanner.Utilities;

public class TextUtils {

    public static boolean isEmpty (String text) {
        try {
            if (text.isEmpty() || text.equals("") || text.length() < 3 || text == null)
                return true;

            else
                return false;
        } catch (NullPointerException e) {
            return true;
        }
    }
}
