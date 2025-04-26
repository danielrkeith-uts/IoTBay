package model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import org.junit.Assert;
import org.junit.Test;

import model.Customer;
import model.Staff;
import model.User;

public class UserDBManagerTests {
    private final Connection conn;
    private final UserDBManager userDBManager;

    public UserDBManagerTests() throws ClassNotFoundException, SQLException {
        this.conn = new DBConnector().openConnection();
        this.userDBManager = new UserDBManager(conn);
    }

    @Test
    public void testAddCustomer() {
        try {
            Savepoint savepoint = conn.setSavepoint();

            Customer michaelJackson = new Customer(123456, "Michael", "Jackson", "michael.jackson@bad.com", "+61 111 111 111", "smooth-criminal");
            userDBManager.addCustomer(michaelJackson);

            Customer mjResult = (Customer) userDBManager.getUser(michaelJackson.getUserId());
            assertMichaelJackson(mjResult);

            conn.rollback(savepoint);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
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
            johnSmith = (Customer) userDBManager.getUser(0);
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
            gregoryStafferson = (Staff) userDBManager.getUser(1);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
            return;
        }

        assertGregoryStafferson(gregoryStafferson);
    }

    @Test
    public void testDeleteUser() {
        try {
            Savepoint savepoint = conn.setSavepoint();

            userDBManager.deleteUser(0);
            User deletedUser = userDBManager.getUser(0);

            Assert.assertNull(deletedUser);

            conn.rollback(savepoint);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    private void assertJohnSmith(Customer johnSmith) {
        Assert.assertEquals(0, johnSmith.getUserId());
        Assert.assertEquals("John", johnSmith.getFirstName());
        Assert.assertEquals("Smith", johnSmith.getLastName());
        Assert.assertEquals("+61 412 345 678", johnSmith.getPhone());
        Assert.assertEquals("john.smith@gmail.com", johnSmith.getEmail());
        Assert.assertTrue(johnSmith.checkPassword("johnsPassword"));
    }

    private void assertGregoryStafferson(Staff gregoryStafferson) {
        Assert.assertEquals(1, gregoryStafferson.getUserId());
        Assert.assertEquals("Gregory", gregoryStafferson.getFirstName());
        Assert.assertEquals("Stafferson", gregoryStafferson.getLastName());
        Assert.assertEquals("+61 487 654 321", gregoryStafferson.getPhone());
        Assert.assertEquals(1001, gregoryStafferson.getStaffCardId());
        Assert.assertEquals("gregory.stafferson@iotbay.com", gregoryStafferson.getEmail());
        Assert.assertTrue(gregoryStafferson.checkPassword("!@#$%^&*()"));
    }

    private void assertMichaelJackson(Customer michaelJackson) {
        Assert.assertEquals("Michael", michaelJackson.getFirstName());
        Assert.assertEquals("Jackson", michaelJackson.getLastName());
        Assert.assertEquals("+61 111 111 111", michaelJackson.getPhone());
        Assert.assertEquals("michael.jackson@bad.com", michaelJackson.getEmail());
        Assert.assertTrue(michaelJackson.checkPassword("smooth-criminal"));
    }
}
