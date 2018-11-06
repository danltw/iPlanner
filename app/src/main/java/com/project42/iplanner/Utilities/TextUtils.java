package com.project42.iplanner.Utilities;

public class TextUtils {

    private final static String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^A-Za-z0-9])(?=\\S+$).{8,}";

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

    public static boolean passReqValidator (String password) {
        try {
            if (password.matches(pattern))
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
