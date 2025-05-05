package utils;

import java.util.regex.Pattern;

public class Validator {
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{8}$";
    private static final String PHONE_REGEX = "^(\\+\\d{2}[\\s-]?)?\\d{3}[\\s-]?\\d{3}[\\s-]?\\d{3}$";

    public static boolean isEmail(String input) {
        return validate(EMAIL_REGEX, input);
    }

    public static boolean isSecurePassword(String input) {
        return validate(PASSWORD_REGEX, input);
    }

    public static boolean isPhoneNumber(String input) {
        return validate(PHONE_REGEX, input);
    }

    private static boolean validate(String pattern, String input) {
        return Pattern.compile(pattern).matcher(input).matches();
    }
}
