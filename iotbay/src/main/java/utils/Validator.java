package utils;

import java.util.regex.Pattern;

public class Validator {
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String[] PASSWORD_REGEXES = {"^.*[a-z].*$", "^.*[A-Z].*$", "^.*[0-9].*$", "^.*[^a-zA-Z0-9].*"};
    private static final String PHONE_REGEX = "^\\+?[0-9]{3,17}$";

    public static boolean isEmail(String input) {
        return validate(EMAIL_REGEX, input);
    }

    public static boolean isSecurePassword(String input) {
        for (String regex : PASSWORD_REGEXES) {
            if (!validate(regex, input)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPhoneNumber(String input) {
        return validate(PHONE_REGEX, input);
    }

    private static boolean validate(String pattern, String input) {
        return Pattern.compile(pattern).matcher(input).matches();
    }
}
