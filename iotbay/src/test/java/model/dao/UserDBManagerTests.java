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
    private static final Customer johnSmith =
        new Customer(0,
                     "John",
                     "Smith",
                     "john.smith@gmail.com",
                     "+61412345678",
                     "johnsPassword");

    // ← Added "ADMIN" as the position for our single built-in admin staff
    private static final Staff gregoryStafferson =
        new Staff(1,
                  "Gregory",
                  "Stafferson",
                  "gregory.stafferson@iotbay.com",
                  "+61487654321",
                  "!@#$%^&*()",
                  1001,
                  true,
                  "STAFF");

    // Not in database
    private static final Customer michaelJackson =
        new Customer(999,
                     "Michael",
                     "Jackson",
                     "michael.jackson@bad.com",
                     "+61 111 111 111",
                     "smooth-criminal");

    private final Connection conn;
    private final UserDBManager userDBManager;

    public UserDBManagerTests() throws ClassNotFoundException, SQLException {
        this.conn           = new DBConnector().openConnection();
        this.conn.setAutoCommit(false);
        this.userDBManager  = new UserDBManager(conn);
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
            try { conn.rollback(); }
            catch (SQLException ignore) {}
        }
    }

    @Test
    public void testGetCustomerA() {
        try {
            Customer jsResult = (Customer) userDBManager.getUser(
                johnSmith.getEmail(),
                johnSmith.getPassword()
            );
            Assert.assertEquals(johnSmith, jsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetCustomerB() {
        try {
            Customer jsResult = (Customer) userDBManager.getUser(johnSmith.getUserId());
            Assert.assertEquals(johnSmith, jsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetStaffA() {
        try {
            Staff gsResult = (Staff) userDBManager.getUser(
                gregoryStafferson.getEmail(),
                gregoryStafferson.getPassword()
            );
            Assert.assertEquals(gregoryStafferson, gsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetStaffB() {
        try {
            Staff gsResult = (Staff) userDBManager.getUser(gregoryStafferson.getUserId());
            Assert.assertEquals(gregoryStafferson, gsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateCustomer() {
        Customer newJohnSmith = new Customer(
            johnSmith.getUserId(),
            johnSmith.getFirstName() + "1",
            johnSmith.getLastName()  + "2",
            johnSmith.getEmail()     + "3",
            johnSmith.getPhone()     + "4",
            johnSmith.getPassword()  + "5"
        );

        try {
            userDBManager.updateCustomer(newJohnSmith);
            Customer newJsResult = (Customer) userDBManager.getUser(johnSmith.getUserId());
            Assert.assertEquals(newJohnSmith, newJsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try { conn.rollback(); }
            catch (SQLException ignore) {}
        }
    }

    @Test
    public void testUpdateStaff() {
        // include same position, only staffCardId and names/etc change
        Staff newGregoryStafferson = new Staff(
            gregoryStafferson.getUserId(),
            gregoryStafferson.getFirstName() + "1",
            gregoryStafferson.getLastName()  + "2",
            gregoryStafferson.getEmail()     + "3",
            gregoryStafferson.getPhone()     + "4",
            gregoryStafferson.getPassword()  + "5",
            gregoryStafferson.getStaffCardId() + 6,
            gregoryStafferson.isAdmin(),
            gregoryStafferson.getPosition()
        );

        try {
            userDBManager.updateStaff(newGregoryStafferson);
            Staff newGsResult = (Staff) userDBManager.getUser(gregoryStafferson.getUserId());
            Assert.assertEquals(newGregoryStafferson, newGsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try { conn.rollback(); }
            catch (SQLException ignore) {}
        }
    }

    @Test
    public void testDeleteUser() {
        try {
            userDBManager.deleteUser(0);
            User deletedUser = userDBManager.getUser(0);
            Assert.assertNull(deletedUser);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try { conn.rollback(); }
            catch (SQLException ignore) {}
        }
    }
}





