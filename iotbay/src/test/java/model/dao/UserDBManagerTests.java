package model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import model.Customer;
import model.Staff;
import model.User;
import java.util.stream.Collectors;


public class UserDBManagerTests {
    // In database
    private static final Customer johnSmith = new Customer(1, "John", "Smith", "john.smith@gmail.com", "+61412345678", "johnsPassword", Customer.Type.INDIVIDUAL);
    private static final Staff gregoryStafferson = new Staff(21, "Gregory", "Stafferson", "gregory.stafferson@iotbay.com", "+61487654321", "!@#$%^&*()", 1001, true);

    // Not in database
    private static final Customer michaelJackson = new Customer(999, "Michael", "Jackson", "michael.jackson@bad.com", "+61 111 111 111", "smooth-criminal", Customer.Type.INDIVIDUAL);

    private final Connection conn;
    private final UserDBManager userDBManager;

    public UserDBManagerTests() throws ClassNotFoundException, SQLException {
        this.conn = new DBConnector().openConnection();
        conn.setAutoCommit(false);
        this.userDBManager = new UserDBManager(conn);
    }

    private void assertCustomerEquals(Customer expected, Customer actual) {
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assert.assertEquals(expected.getLastName(), actual.getLastName());
        Assert.assertEquals(expected.getEmail(), actual.getEmail());
        Assert.assertEquals(expected.getPhone(), actual.getPhone());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
        Assert.assertEquals(expected.getType(), actual.getType());
    }

    private void assertStaffEquals(Staff expected, Staff actual) {
        Assert.assertNotNull(actual);
    
        System.out.println("Expected Staff: " + expected);
        System.out.println("Actual Staff: " + actual);
    
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getFirstName(), actual.getFirstName());
        Assert.assertEquals(expected.getLastName(), actual.getLastName());
        Assert.assertEquals(expected.getEmail(), actual.getEmail());
        Assert.assertEquals(expected.getPhone(), actual.getPhone());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
        Assert.assertEquals(expected.getStaffCardId(), actual.getStaffCardId());
        Assert.assertEquals(expected.isAdmin(), actual.isAdmin());
    }

    @Test
    public void testAddCustomer() {
        try {
            userDBManager.addCustomer(michaelJackson);
            Customer mjResult = (Customer) userDBManager.getUser(michaelJackson.getUserId());
            assertCustomerEquals(michaelJackson, mjResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try { conn.rollback(); } catch (SQLException e) { System.err.println(e); }
        }
    }

    @Test
    public void testGetCustomerA() {
        try {
            Customer jsResult = (Customer) userDBManager.getUser(johnSmith.getEmail(), johnSmith.getPassword());
            assertCustomerEquals(johnSmith, jsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetCustomerB() {
        try {
            Customer jsResult = (Customer) userDBManager.getUser(johnSmith.getUserId());
            assertCustomerEquals(johnSmith, jsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testGetStaffA() {
        try {
            Staff gsResult = (Staff) userDBManager.getUser(gregoryStafferson.getEmail(), gregoryStafferson.getPassword());
            assertStaffEquals(gregoryStafferson, gsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }
    @Test
    public void testGetStaffB() {
        try {
            User user = userDBManager.getUser(gregoryStafferson.getUserId());
            Assert.assertTrue("Expected Staff instance", user instanceof Staff);
            Staff gsResult = (Staff) user;
            assertStaffEquals(gregoryStafferson, gsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testUpdateCustomer() {
        Customer newJohnSmith = new Customer(
            johnSmith.getUserId(),
            johnSmith.getFirstName() + "1",
            johnSmith.getLastName() + "2",
            johnSmith.getEmail() + "3",
            johnSmith.getPhone() + "4",
            johnSmith.getPassword() + "5",
            johnSmith.getType()
        );

        try {
            userDBManager.updateCustomer(newJohnSmith);
            Customer newJsResult = (Customer) userDBManager.getUser(johnSmith.getUserId());
            assertCustomerEquals(newJohnSmith, newJsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try { conn.rollback(); } catch (SQLException e) { System.err.println(e); }
        }
    }
    @Test
    public void testUpdateStaff() {
        Staff newGregoryStafferson = new Staff(
            gregoryStafferson.getUserId(),
            gregoryStafferson.getFirstName() + "1",
            gregoryStafferson.getLastName() + "2",
            gregoryStafferson.getEmail() + "3",
            gregoryStafferson.getPhone() + "4",
            gregoryStafferson.getPassword() + "5",
            gregoryStafferson.getStaffCardId() + 6,
            gregoryStafferson.isAdmin()
        );
    
        try {
            userDBManager.updateStaff(newGregoryStafferson);
    
            User user = userDBManager.getUser(gregoryStafferson.getUserId());
            Assert.assertTrue("Expected Staff instance", user instanceof Staff);
            Staff newGsResult = (Staff) user;
    
            assertStaffEquals(newGregoryStafferson, newGsResult);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try { conn.rollback(); } catch (SQLException e) { System.err.println(e); }
        }
    }

    @Test
    public void testDeleteUser() {
        try {
            userDBManager.deleteUser(johnSmith.getUserId());
            User deletedUser = userDBManager.getUser(johnSmith.getUserId());
            Assert.assertNull(deletedUser);
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try { conn.rollback(); } catch (SQLException e) { System.err.println(e); }
        }
    }
    @Test
    public void testGetAllCustomers() {
        try {
            List<Customer> customers = userDBManager.getAllCustomers();
            Assert.assertNotNull("Customer list should not be null", customers);
            Assert.assertTrue("Customer list should contain at least one customer", customers.size() > 0);

            boolean foundJohnSmith = customers.stream()
                .anyMatch(c -> c.getUserId() == johnSmith.getUserId());
            Assert.assertTrue("Expected to find John Smith in customer list", foundJohnSmith);
        } catch (SQLException e) {
        Assert.fail(e.getMessage());
        } finally {
            try { conn.rollback(); } catch (SQLException e) { System.err.println(e); }
        }
    }

    @Test
    public void testDeactivateAndReactivateCustomer() {
        try {
            int testUserId = johnSmith.getUserId(); 

            userDBManager.setCustomerDeactivated(testUserId, true);
            Customer deactivated = (Customer) userDBManager.getUser(testUserId);
            Assert.assertTrue("Customer should be deactivated", deactivated.isDeactivated());

            userDBManager.reactivateCustomer(testUserId);
            Customer reactivated = (Customer) userDBManager.getUser(testUserId);
            Assert.assertFalse("Customer should be active (not deactivated)", reactivated.isDeactivated());

        } catch (SQLException e) {
        Assert.fail("SQL error during deactivate/reactivate test: " + e.getMessage());
            } finally {
        try { conn.rollback(); } catch (SQLException e) { System.err.println(e); }
        }
    }


    @Test
    public void testGetCustomersByType() {
        try {
            List<Customer> allCustomers = userDBManager.getAllCustomers();
            Assert.assertNotNull(allCustomers);
            Assert.assertTrue("Expected some customers in DB", allCustomers.size() > 0);

            List<Customer> individuals = allCustomers.stream()
                .filter(c -> c.getType() == Customer.Type.INDIVIDUAL)
                .collect(Collectors.toList());

                for (Customer c : individuals) {
                 Assert.assertEquals(Customer.Type.INDIVIDUAL, c.getType());
         }

            List<Customer> companies = allCustomers.stream()
                .filter(c -> c.getType() == Customer.Type.COMPANY)
                .collect(Collectors.toList());

                for (Customer c : companies) {
                    Assert.assertEquals(Customer.Type.COMPANY, c.getType());
                }

            Assert.assertEquals(allCustomers.size(), individuals.size() + companies.size());

        } catch (SQLException e) {
            Assert.fail("SQL Exception: " + e.getMessage());
        }
    }

}
