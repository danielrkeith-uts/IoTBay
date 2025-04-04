package model;

import org.junit.Test;
import org.junit.Assert;

public class UserTests {
    private User greg;
    private User james;
    private User david;

    public UserTests() {
        greg = new User("Gregory", null, null, null, "1234");
        james = new User("James", null, null, null, "myPassword");
        david = new User("David", null, null, null, "abcdefg");
    }

    @Test
    public void testCheckPassword() {
        Assert.assertTrue(greg.checkPassword("1234"));
        Assert.assertFalse(greg.checkPassword("5678"));
        Assert.assertTrue(james.checkPassword("myPassword"));
        Assert.assertFalse(james.checkPassword("mypassword"));
    }
}
