package util;

import org.junit.Assert;
import org.junit.Test;

import model.Customer;
import model.User;
import model.exceptions.InvalidInputException;
import utils.Validator;

public class ValidatorTests {
    private User userInvalidEmail = new Customer(-1, null, null, "invalidEmail", null, "validPassword1!");
    private User userInvalidPassword = new Customer(-1, null, null, "valid@email.com", null, "invalidPassword");
    private User userInvalidPhone = new Customer(-1, null, null, "valid@email.com", "abc", "validPassword1!");
    private User userValid1 = new Customer(-1, null, null, "valid@email.com", "", "validPassword1!");

    @Test
    public void testValidateUser() {
        Assert.assertTrue(testValidateUserParameterised(userInvalidEmail, "Invalid email"));
        Assert.assertTrue(testValidateUserParameterised(userInvalidPassword, "Invalid password"));
        Assert.assertTrue(testValidateUserParameterised(userInvalidPhone, "Invalid phone number"));
        Assert.assertTrue(testValidateUserParameterised(userValid1, null));
    }

    @Test
    public void testValidateStaffCard() {
        Assert.assertTrue(testValidateStaffCardParameterised("", "Must provide Staff Card ID"));
        Assert.assertTrue(testValidateStaffCardParameterised("186G*^", "Invalid Staff Card ID"));
        Assert.assertTrue(testValidateStaffCardParameterised("1234", null));
    }

    @Test
    public void testValidatePhoneNumber() {
        Assert.assertTrue(testValidatePhoneNumberParameterised("", null));
        Assert.assertTrue(testValidatePhoneNumberParameterised("12345678876543456787654", "Invalid phone number"));
        Assert.assertTrue(testValidatePhoneNumberParameterised("1234rwe&", "Invalid phone number"));
        Assert.assertTrue(testValidatePhoneNumberParameterised("0412345678", null));
        Assert.assertTrue(testValidatePhoneNumberParameterised("+61412345678", null));
    }

    private boolean testValidateUserParameterised(User user, String expectedError) {
        String error = null;

        try {
            Validator.validateUser(user);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        if (error == null) {
            if (expectedError == null) {
                return true;
            }
            return false;
        }

        return error.equals(expectedError);
    }

    private boolean testValidateStaffCardParameterised(String staffCardID, String expectedError) {
        String error = null;

        try {
            Validator.validateStaffCardId(staffCardID);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        if (error == null) {
            if (expectedError == null) {
                return true;
            }
            return false;
        }

        return error.equals(expectedError);
    }

    private boolean testValidatePhoneNumberParameterised(String phone, String expectedError) {
        String error = null;

        try {
            Validator.validatePhoneNumber(phone);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        if (error == null) {
            if (expectedError == null) {
                return true;
            }
            return false;
        }

        return error.equals(expectedError);
    }
}
