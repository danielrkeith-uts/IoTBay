package model.dao;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import model.Customer;

public class UserDBManagerTests {
    private UserDBManager userDBManager;

    public UserDBManagerTests() throws ClassNotFoundException, SQLException {
        this.userDBManager = new UserDBManager(new DBConnector().openConnection());
    }

    @Test
    public void testGetCustomer() {
        final String email = "john.smith@gmail.com";
        final String password = "johnsPassword";

        Customer johnSmith;
        try {
            johnSmith = userDBManager.getCustomer(email, password);
        } catch (SQLException e) {
            Assert.fail();
            return;
        }

        Assert.assertEquals("John", johnSmith.getFirstName());
        Assert.assertEquals("Smith", johnSmith.getLastName());
        Assert.assertEquals("+61 412 345 678", johnSmith.getPhone());
        Assert.assertEquals(email, johnSmith.getEmail());
        Assert.assertTrue(johnSmith.checkPassword(password));
    }
}
