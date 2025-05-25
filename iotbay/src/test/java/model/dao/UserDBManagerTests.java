package model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.Customer;
import model.Staff;
import model.User;

public class UserDBManagerTests {
    private static final Customer johnSmith =
        new Customer(1, "John", "Smith", "john.smith@gmail.com", "+61412345678", "johnsPassword", Customer.Type.INDIVIDUAL);
    private static final Staff gregoryStafferson =
        new Staff(21,
                  "Gregory",
                  "Stafferson",
                  "gregory.stafferson@iotbay.com",
                  "+61487654321",
                  "!@#$%^&*()",
                  1001, true,
                  "STAFF");
    private static final Customer michaelJackson =
        new Customer(999, "Michael", "Jackson", "michael.jackson@bad.com", "+61 111 111 111", "smooth-criminal", Customer.Type.INDIVIDUAL);

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
        Assert.assertEquals(expected.getType(),     actual.getType());
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
    public void testAddCustomer() {
        try {
            mgr.addCustomer(michaelJackson);
            Customer mjResult = (Customer) mgr.getUser(michaelJackson.getUserId());
            assertCustomerEquals(michaelJackson, mjResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try { conn.rollback(); } catch (SQLException ignore) {}
        }
    }

    @Test
    public void testGetCustomerA() {
        try {
            Customer jsResult = (Customer) mgr.getUser(johnSmith.getEmail(), johnSmith.getPassword());
            assertCustomerEquals(johnSmith, jsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetCustomerB() {
        try {
            Customer jsResult = (Customer) mgr.getUser(johnSmith.getUserId());
            assertCustomerEquals(johnSmith, jsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetStaffA() {
        try {
            Staff gsResult = (Staff) mgr.getUser(gregoryStafferson.getEmail(), gregoryStafferson.getPassword());
            assertStaffEquals(gregoryStafferson, gsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetStaffB() {
        try {
            User user = mgr.getUser(gregoryStafferson.getUserId());
            Assert.assertTrue("Expected Staff instance", user instanceof Staff);
            assertStaffEquals(gregoryStafferson, (Staff) user);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateCustomer() throws SQLException {
        Customer modified = new Customer(
            johnSmith.getUserId(),
            johnSmith.getFirstName() + "1",
            johnSmith.getLastName()  + "2",
            johnSmith.getEmail()     + "3",
            johnSmith.getPhone()     + "4",
            johnSmith.getPassword()  + "5",
            johnSmith.getType()
        );

        try {
            mgr.updateCustomer(modified);
            Customer newJsResult = (Customer) mgr.getUser(johnSmith.getUserId());
            assertCustomerEquals(modified, newJsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try { conn.rollback(); } catch (SQLException ignore) {}
        }
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
    public void testDeleteUser() {
        try {
            mgr.deleteUser(johnSmith.getUserId());
            User deletedUser = mgr.getUser(johnSmith.getUserId());
            Assert.assertNull(deletedUser);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try { conn.rollback(); } catch (SQLException ignore) {}
        }
    }

    //customer specific tests 
    @Test
    public void testGetAllCustomers() {
        try {
            List<Customer> customers = mgr.getAllCustomers();
            Assert.assertNotNull(customers);
            Assert.assertTrue(customers.size() > 0);

            boolean foundJohnSmith = customers.stream()
                .anyMatch(c -> c.getUserId() == johnSmith.getUserId());
            Assert.assertTrue(foundJohnSmith);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try { conn.rollback(); } catch (SQLException ignore) {}
        }
    }

    @Test
    public void testDeactivateAndReactivateCustomer() {
        try {
            int id = johnSmith.getUserId();
            mgr.setCustomerDeactivated(id, true);
            Customer deactivated = (Customer) mgr.getUser(id);
            Assert.assertTrue(deactivated.isDeactivated());

            mgr.reactivateCustomer(id);
            Customer reactivated = (Customer) mgr.getUser(id);
            Assert.assertFalse(reactivated.isDeactivated());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try { conn.rollback(); } catch (SQLException ignore) {}
        }
    }

    @Test
    public void testGetCustomersByType() {
        try {
            List<Customer> all = mgr.getAllCustomers();
            List<Customer> individuals = all.stream()
                .filter(c -> c.getType() == Customer.Type.INDIVIDUAL)
                .collect(Collectors.toList());
            individuals.forEach(c -> Assert.assertEquals(Customer.Type.INDIVIDUAL, c.getType()));

            List<Customer> companies = all.stream()
                .filter(c -> c.getType() == Customer.Type.COMPANY)
                .collect(Collectors.toList());
            companies.forEach(c -> Assert.assertEquals(Customer.Type.COMPANY, c.getType()));

            Assert.assertEquals(all.size(), individuals.size() + companies.size());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    //staff specific tests

    @Test
    public void testDeactivateAndReactivateStaff() {
        try {
            int id = gregoryStafferson.getUserId();
            mgr.deactivateStaff(id);
            Staff deactivated = (Staff) mgr.getUser(id);
            Assert.assertTrue(deactivated.isDeactivated());

            mgr.reactivateStaff(id);
            Staff reactivated = (Staff) mgr.getUser(id);
            Assert.assertFalse(reactivated.isDeactivated());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try { conn.rollback(); } catch (SQLException ignore) {}
        }
    }

    @Test
    public void testGetStaffByPosition() {
        try {
            List<Staff> staffList = mgr.searchStaff("", "STAFF");
            Assert.assertNotNull(staffList);
            Assert.assertTrue(staffList.size() > 0);
            staffList.forEach(s -> Assert.assertEquals("STAFF", s.getPosition()));

            List<Staff> allStaff = mgr.getAllStaff();
            Assert.assertEquals(allStaff.size(), mgr.searchStaff("", "ALL").size());
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetAllStaff() {
        try {
            List<Staff> staffList = mgr.getAllStaff();
            Assert.assertNotNull(staffList);
            Assert.assertTrue(staffList.size() > 0);

            boolean foundGregory = staffList.stream()
                .anyMatch(s -> s.getUserId() == gregoryStafferson.getUserId());
            Assert.assertTrue(foundGregory);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testSearchStaffByName() {
        try {
            List<Staff> results = mgr.searchStaff("Gregory", "ALL");
            Assert.assertNotNull(results);
            Assert.assertTrue(results.stream()
                .anyMatch(s -> s.getUserId() == gregoryStafferson.getUserId()));
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }
}
