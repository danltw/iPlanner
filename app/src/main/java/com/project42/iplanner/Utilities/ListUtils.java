package com.project42.iplanner.Utilities;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ListUtils {

    public static ArrayList<Integer> parseString(String integerLst) {
        if (integerLst.isEmpty() || integerLst == null)
            return null;
        ArrayList<Integer> result = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(integerLst, ",");
        while (tokenizer.hasMoreTokens()) {
            String id = tokenizer.nextToken();
            int number = Integer.valueOf(id);
            result.add(number);
        }
        return result;
    }

    public static String parseIntList(ArrayList<Integer> integerLst) {
        if (integerLst.isEmpty() || integerLst == null)
            return null;
        String result = "";
        for (int i=0;i<integerLst.size()-1;i++) {
            result+=String.valueOf(integerLst.get(i));
            result+=", ";
        }
        result+=integerLst.get(integerLst.size()-1);
        return result;
    }


    // Concatenate a list of string into a single string
    public static String getStrNames(ArrayList<String> usernames) {
        if (usernames.isEmpty() || usernames == null)
            return null;
        String result = "";

        if (usernames.size() > 1) {
            for (int i = 0; i < usernames.size() - 1; i++) {
                result += "\"";
                result += usernames.get(i);
                result += "\",";
            }
        }

        result+="\"";
        result+=usernames.get(usernames.size()-1);
        result+="\"";
        return result;
    }
}
