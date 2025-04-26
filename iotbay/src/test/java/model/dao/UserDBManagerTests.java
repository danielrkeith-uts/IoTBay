package model.dao;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import model.Customer;
import model.Staff;

public class UserDBManagerTests {
    private UserDBManager userDBManager;

    public UserDBManagerTests() throws ClassNotFoundException, SQLException {
        this.userDBManager = new UserDBManager(new DBConnector().openConnection());
    }

    @Test
    public void testGetCustomerA() {
        Customer johnSmith;
        try {
            johnSmith = (Customer) userDBManager.getUser("john.smith@gmail.com", "johnsPassword");
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
            return;
        }

        assertJohnSmith(johnSmith);
    }

    @Test
    public void testGetCustomerB() {
        Customer johnSmith;
        try {
            johnSmith = (Customer) userDBManager.getUser(1);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
            return;
        }

        assertJohnSmith(johnSmith);
    }

    @Test
    public void testGetStaffA() {
        Staff gregoryStafferson;
        try {
            gregoryStafferson = (Staff) userDBManager.getUser("gregory.stafferson@iotbay.com", "!@#$%^&*()");
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
            return;
        }

        assertGregoryStafferson(gregoryStafferson);
    }

    @Test
    public void testGetStaffB() {
        Staff gregoryStafferson;
        try {
            gregoryStafferson = (Staff) userDBManager.getUser(100);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
            return;
        }

        assertGregoryStafferson(gregoryStafferson);
    }

    private void assertJohnSmith(Customer johnSmith) {
        Assert.assertEquals(1, johnSmith.getUserId());
        Assert.assertEquals("John", johnSmith.getFirstName());
        Assert.assertEquals("Smith", johnSmith.getLastName());
        Assert.assertEquals("+61 412 345 678", johnSmith.getPhone());
        Assert.assertEquals("john.smith@gmail.com", johnSmith.getEmail());
        Assert.assertTrue(johnSmith.checkPassword("johnsPassword"));
    }

    private void assertGregoryStafferson(Staff gregoryStafferson) {
        Assert.assertEquals(100, gregoryStafferson.getUserId());
        Assert.assertEquals("Gregory", gregoryStafferson.getFirstName());
        Assert.assertEquals("Stafferson", gregoryStafferson.getLastName());
        Assert.assertEquals("+61 487 654 321", gregoryStafferson.getPhone());
        Assert.assertEquals(1001, gregoryStafferson.getStaffCardId());
        Assert.assertEquals("gregory.stafferson@iotbay.com", gregoryStafferson.getEmail());
        Assert.assertTrue(gregoryStafferson.checkPassword("!@#$%^&*()"));
    }
}
