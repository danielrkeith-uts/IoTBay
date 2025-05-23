package model.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Test;

import model.Customer;
import model.Staff;
import model.User;

public class UserDBManagerTests {
    // In database
    private static final Customer johnSmith = new Customer(1, "John", "Smith", "john.smith@gmail.com", "+61412345678", "johnsPassword");
    private static final Staff gregoryStafferson = new Staff(21, "Gregory", "Stafferson", "gregory.stafferson@iotbay.com", "+61487654321", "!@#$%^&*()", 1001);

    // Not in database
    private static final Customer michaelJackson = new Customer(999, "Michael", "Jackson", "michael.jackson@bad.com", "+61 111 111 111", "smooth-criminal");

    private final Connection conn;
    private final UserDBManager userDBManager;

    public UserDBManagerTests() throws ClassNotFoundException, SQLException {
        this.conn = new DBConnector().openConnection();
        conn.setAutoCommit(false);
        this.userDBManager = new UserDBManager(conn);
    }

    @Test
    public void testAddCustomer() {
        try {
            userDBManager.addCustomer(michaelJackson);

            Customer mjResult = (Customer) userDBManager.getUser(michaelJackson.getUserId());

            Assert.assertEquals(michaelJackson, mjResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    @Test
    public void testGetCustomerA() {
        Customer jsResult;
        try {
            jsResult = (Customer) userDBManager.getUser(johnSmith.getEmail(), johnSmith.getPassword());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
            return;
        }

        Assert.assertEquals(johnSmith, jsResult);
    }

    @Test
    public void testGetCustomerB() {
        Customer jsResult;
        try {
            jsResult = (Customer) userDBManager.getUser(1);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
            return;
        }

        Assert.assertEquals(johnSmith, jsResult);
    }

    @Test
    public void testGetStaffA() {
        Staff gsResult;
        try {
            gsResult = (Staff) userDBManager.getUser(gregoryStafferson.getEmail(), gregoryStafferson.getPassword());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
            return;
        }

        Assert.assertEquals(gregoryStafferson, gsResult);
    }

    @Test
    public void testGetStaffB() {
        Staff gsResult;
        try {
            gsResult = (Staff) userDBManager.getUser(21);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
            return;
        }

        Assert.assertEquals(gregoryStafferson, gsResult);
    }

    @Test
    public void testUpdateCustomer() {
        Customer newJohnSmith = new Customer(
            johnSmith.getUserId(),
            johnSmith.getFirstName() + "1",
            johnSmith.getLastName() + "2",
            johnSmith.getEmail() + "3",
            johnSmith.getPhone() + "4",
            johnSmith.getPassword() + "5"
        );

        try {
            userDBManager.updateCustomer(newJohnSmith);
        
            Customer newJsResult = (Customer) userDBManager.getUser(johnSmith.getUserId());
    
            Assert.assertEquals(newJohnSmith, newJsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    @Test
    public void testUpdateStaff() {
        Staff newGregoryStafferson = new Staff(
            gregoryStafferson.getUserId(),
            gregoryStafferson.getFirstName() +"1",
            gregoryStafferson.getLastName() + "2",
            gregoryStafferson.getEmail() + "3",
            gregoryStafferson.getPhone() + "4",
            gregoryStafferson.getPassword() + "5",
            gregoryStafferson.getStaffCardId() + 6
        );

        try {
            userDBManager.updateStaff(newGregoryStafferson);

            Staff newGsResult = (Staff) userDBManager.getUser(gregoryStafferson.getUserId());

            Assert.assertEquals(newGregoryStafferson, newGsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    @Test
    public void testDeleteUser() {
        try {
            userDBManager.deleteUser(1);
            User deletedUser = userDBManager.getUser(1);

            Assert.assertNull(deletedUser);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }
}