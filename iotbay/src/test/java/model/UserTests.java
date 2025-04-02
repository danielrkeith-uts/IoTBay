package model;

import org.junit.Test;
import org.junit.Assert;

public class UserTests {
    private User greg;
    private User james;
    private User david;

    public UserTests() {
        greg = new User("Gregory", null, null, null, null);
        james = new User("James", null, null, null, null);
        david = new User("David", null, null, null, null);
    }

    @Test
    public void testGreeting() {
        Assert.assertEquals(greg.getGreeting(), "Hello, Gregory!");
        Assert.assertEquals(james.getGreeting(), "Hello, James!");
        Assert.assertEquals(david.getGreeting(), "Hello, David!");
    }
}
