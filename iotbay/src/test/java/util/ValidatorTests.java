package util;

import org.junit.Assert;
import org.junit.Test;

import model.exceptions.InvalidInputException;
import utils.Validator;

public class ValidatorTests {
    @Test
    public void testValidateStaffCard() {
        Assert.assertTrue(testValidateStaffCardParameterised("", "Must provide Staff Card ID"));
        Assert.assertTrue(testValidateStaffCardParameterised("186G*^", "Invalid Staff Card ID"));
        Assert.assertTrue(testValidateStaffCardParameterised("1234", null));
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
}
