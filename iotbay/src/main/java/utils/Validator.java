package utils;

import java.util.regex.Pattern;

import model.User;
import model.exceptions.InvalidInputException;

public class Validator {
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String[] PASSWORD_REGEXES = {"^.*[a-z].*$", "^.*[A-Z].*$", "^.*[0-9].*$", "^.*[^a-zA-Z0-9].*"};
    private static final String PHONE_REGEX = "^\\+?[0-9]{3,17}$";

    public static void validateUser(User user) throws InvalidInputException {
        if (!isEmail(user.getEmail())) {
            throw new InvalidInputException("Invalid email");
        }

        if (!isSecurePassword(user.getPassword())) {
            throw new InvalidInputException("Invalid password");
        }

        validatePhoneNumber(user.getPhone());
    }

    public static int validateStaffCardId(String staffCardId) throws InvalidInputException {
        if (staffCardId.isEmpty()) {
            throw new InvalidInputException("Must provide Staff Card ID");
        }

        int staffCardIdInt;
        try {
            staffCardIdInt = Integer.parseInt(staffCardId);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Invalid Staff Card ID");
        }

        return staffCardIdInt;
    }

    public static void validatePhoneNumber(String phone) throws InvalidInputException {
        if (phone != null && !phone.isEmpty() && !isPhoneNumber(phone)) {
            throw new InvalidInputException("Invalid phone number");
        }
    }

    public static boolean isSecurePassword(String input) {
        for (String regex : PASSWORD_REGEXES) {
            if (!validate(regex, input)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPhoneNumber(String input) {
        return validate(PHONE_REGEX, input);
    }

    private static boolean isEmail(String input) {
        return validate(EMAIL_REGEX, input);
    }

    private static boolean validate(String pattern, String input) {
        return Pattern.compile(pattern).matcher(input).matches();
    }

    public static void validateName(String name, String fieldName) throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " cannot be empty");
        }
        if (!name.matches("^[a-zA-Z\\s'-]+$")) {
            throw new InvalidInputException("Invalid " + fieldName);
        }
    }
}
