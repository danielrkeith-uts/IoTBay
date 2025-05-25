package util;

import org.junit.Assert;
import org.junit.Test;

import model.Customer;
import model.Staff;
import model.User;
import model.exceptions.InvalidInputException;
import utils.Validator;

public class ValidatorTests {
    //Customer validation tests 
    private User userInvalidEmail= new Customer(-1, null, null, "invalidEmail",    null, "validPassword1!", Customer.Type.INDIVIDUAL);
    private User userInvalidPassword  = new Customer(-1, null, null, "valid@email.com", null, "invalidPassword", Customer.Type.INDIVIDUAL);
    private User userInvalidPhone     = new Customer(-1, null, null, "valid@email.com", "abc", "validPassword1!", Customer.Type.INDIVIDUAL);
    private User userValid1           = new Customer(-1, null, null, "valid@email.com", "", "validPassword1!", Customer.Type.INDIVIDUAL);

    // Staff validation tests
    private User staffInvalidEmail= new Staff(-1, null, null, "invalidEmail",    null, "validPassword1!", 0, false, "STAFF");
    private User staffInvalidPassword= new Staff(-1, null, null, "valid@staff.com", null, "short",           0, false, "STAFF");
    private User staffInvalidPhone = new Staff(-1, null, null, "valid@staff.com", "abc", "ValidPass1!",  0, false, "STAFF");
    private User staffValid1= new Staff(-1, null, null, "valid@staff.com", "",    "ValidPass1!",  0, false, "STAFF");

    @Test
    public void testValidateUser() {
        // Customer tests
        Assert.assertTrue(testValidateUserParameterised(userInvalidEmail,    "Invalid email"));
        Assert.assertTrue(testValidateUserParameterised(userInvalidPassword, "Invalid password"));
        Assert.assertTrue(testValidateUserParameterised(userInvalidPhone,    "Invalid phone number"));
        Assert.assertTrue(testValidateUserParameterised(userValid1,          null));
        
        // Staff tests
        Assert.assertTrue(testValidateUserParameterised(staffInvalidEmail,    "Invalid email"));
        Assert.assertTrue(testValidateUserParameterised(staffInvalidPassword, "Invalid password"));
        Assert.assertTrue(testValidateUserParameterised(staffInvalidPhone,    "Invalid phone number"));
        Assert.assertTrue(testValidateUserParameterised(staffValid1,          null));
    }

    @Test
    public void testValidateStaffCard() {
        Assert.assertTrue(testValidateStaffCardParameterised("",    "Must provide Staff Card ID"));
        Assert.assertTrue(testValidateStaffCardParameterised("186G*^", "Invalid Staff Card ID"));
        Assert.assertTrue(testValidateStaffCardParameterised("1234", null));
    }

    @Test
    public void testValidatePhoneNumber() {
        Assert.assertTrue(testValidatePhoneNumberParameterised("",                   null));
        Assert.assertTrue(testValidatePhoneNumberParameterised("12345678876543456787654", "Invalid phone number"));
        Assert.assertTrue(testValidatePhoneNumberParameterised("1234rwe&",           "Invalid phone number"));
        Assert.assertTrue(testValidatePhoneNumberParameterised("0412345678",          null));
        Assert.assertTrue(testValidatePhoneNumberParameterised("+61412345678",        null));
    }

    private boolean testValidateUserParameterised(User user, String expectedError) {
        String error = null;

        try {
            Validator.validateUser(user);
        } catch (InvalidInputException e) {
            error = e.getMessage();
        }

        if (error == null) {
            return expectedError == null;
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
            return expectedError == null;
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
            return expectedError == null;
        }

        return error.equals(expectedError);
    }
}


