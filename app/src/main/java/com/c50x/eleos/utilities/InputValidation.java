package com.c50x.eleos.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ugin on 3/23/18.
 */

public final class InputValidation {
    private InputValidation() {

    }


    public static boolean handle(String handle) {

        int minLen = 4;
        int maxLen = 10;

        if (handle.isEmpty() || !(handle.length() >= minLen && handle.length() <= maxLen)) {
            return false;
        } else
            return true;
    }

    public static boolean name(String name) {

        int minLen = 4;
        int maxLen = 10;

        if (name.isEmpty() || !(name.length() >= minLen && name.length() <= maxLen)) {
            return false;
        } else
            return true;
    }
    public static boolean venueName(String name) {

        int minLen = 4;
        int maxLen = 20;

        if (name.isEmpty() || !(name.length() >= minLen && name.length() <= maxLen)) {
            return false;
        } else
            return true;
    }
    public static boolean location(String location) {

        int minLen = 4;
        int maxLen = 20;

        if (location.isEmpty() || !(location.length() >= minLen && location.length() <= maxLen)) {
            return false;
        } else
            return true;
    }

    public static boolean email(String email) {
        String emailPattern = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + // android default pattern

                "\\@" +

                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +

                "(" +

                "\\." +

                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +

                ")+";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean password(String password) {

        int minLen = 6;
        int maxLen = 16;

        if (password.isEmpty() || !(password.length() >= minLen && password.length() <= maxLen)) {
            return false;
        } else
            return true;
    }
}
