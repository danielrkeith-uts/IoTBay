package model.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.Customer;
import model.Staff;
import model.User;

public class UserDBManagerTests {
    private static final Customer johnSmith =
        new Customer(1, "John", "Smith", "john.smith@gmail.com", "+61412345678", "johnsPassword");
    private static final Staff gregoryStafferson =
        new Staff(21,
                  "Gregory",
                  "Stafferson",
                  "gregory.stafferson@iotbay.com",
                  "+61487654321",
                  "!@#$%^&*()",
                  1001,
                  true,
                  "STAFF");

    private static final Customer michaelJackson =
        new Customer(999, "Michael", "Jackson", "michael.jackson@bad.com", "+61 111 111 111", "smooth-criminal");

    private Connection conn;
    private UserDBManager mgr;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        conn = new DBConnector().openConnection();
        conn.setAutoCommit(false);
        mgr = new UserDBManager(conn);
    }

    private void assertCustomerEquals(Customer expected, Customer actual) {
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.getUserId(),   actual.getUserId());
        Assert.assertEquals(expected.getFirstName(),actual.getFirstName());
        Assert.assertEquals(expected.getLastName(), actual.getLastName());
        Assert.assertEquals(expected.getEmail(),    actual.getEmail());
        Assert.assertEquals(expected.getPhone(),    actual.getPhone());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
    }

    private void assertStaffEquals(Staff expected, Staff actual) {
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.getUserId(),     actual.getUserId());
        Assert.assertEquals(expected.getFirstName(),  actual.getFirstName());
        Assert.assertEquals(expected.getLastName(),   actual.getLastName());
        Assert.assertEquals(expected.getEmail(),      actual.getEmail());
        Assert.assertEquals(expected.getPhone(),      actual.getPhone());
        Assert.assertEquals(expected.getPassword(),   actual.getPassword());
        Assert.assertEquals(expected.getStaffCardId(),actual.getStaffCardId());
        Assert.assertEquals(expected.isAdmin(),       actual.isAdmin());
        Assert.assertEquals(expected.getPosition(),   actual.getPosition());
    }

    @Test
    public void testAddCustomer() throws SQLException {
        mgr.addCustomer(michaelJackson);
        Customer result = (Customer) mgr.getUser(michaelJackson.getUserId());
        assertCustomerEquals(michaelJackson, result);
        conn.rollback();
    }

    @Test
    public void testGetCustomerByCred() throws SQLException {
        Customer result = (Customer) mgr.getUser(johnSmith.getEmail(), johnSmith.getPassword());
        assertCustomerEquals(johnSmith, result);
    }

    @Test
    public void testGetCustomerById() throws SQLException {
        Customer result = (Customer) mgr.getUser(johnSmith.getUserId());
        assertCustomerEquals(johnSmith, result);
    }

    @Test
    public void testGetStaffByCred() throws SQLException {
        Staff result = (Staff) mgr.getUser(gregoryStafferson.getEmail(), gregoryStafferson.getPassword());
        assertStaffEquals(gregoryStafferson, result);
    }

    @Test
    public void testGetStaffById() throws SQLException {
        User u = mgr.getUser(gregoryStafferson.getUserId());
        Assert.assertTrue(u instanceof Staff);
        assertStaffEquals(gregoryStafferson, (Staff)u);
    }

    @Test
    public void testUpdateCustomer() throws SQLException {
        Customer modified = new Customer(
            johnSmith.getUserId(),
            johnSmith.getFirstName() + "X",
            johnSmith.getLastName()  + "Y",
            johnSmith.getEmail()     + "Z",
            johnSmith.getPhone()     + "0",
            johnSmith.getPassword()  + "!"
        );
        mgr.updateCustomer(modified);
        Customer result = (Customer) mgr.getUser(johnSmith.getUserId());
        assertCustomerEquals(modified, result);
        conn.rollback();
    }

    @Test
    public void testUpdateStaff() throws SQLException {
        Staff modified = new Staff(
            gregoryStafferson.getUserId(),
            gregoryStafferson.getFirstName() + "X",
            gregoryStafferson.getLastName()  + "Y",
            gregoryStafferson.getEmail()     + "Z",
            gregoryStafferson.getPhone()     + "0",
            gregoryStafferson.getPassword()  + "!",
            gregoryStafferson.getStaffCardId() + 1,
            gregoryStafferson.isAdmin(),
            gregoryStafferson.getPosition()
        );
        mgr.updateStaff(modified);
        User u = mgr.getUser(modified.getUserId());
        Assert.assertTrue(u instanceof Staff);
        assertStaffEquals(modified, (Staff)u);
        conn.rollback();
    }

    @Test
    public void testDeleteUser() throws SQLException {
        mgr.deleteUser(johnSmith.getUserId());
        User u = mgr.getUser(johnSmith.getUserId());
        Assert.assertNull(u);
        conn.rollback();
    }
}
