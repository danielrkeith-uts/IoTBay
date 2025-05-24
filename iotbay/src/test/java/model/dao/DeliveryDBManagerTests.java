package model.dao;

import java.sql.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import model.*;
import model.Enums.AuState;

public class DeliveryDBManagerTests {
    private DeliveryDBManager deliveryDBManager;
    private Connection conn;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        conn = new DBConnector().openConnection();
        conn.setAutoCommit(false);
        deliveryDBManager = new DeliveryDBManager(conn);
    }

    @After
    public void tearDown() {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.err.println("Error rolling back transaction: " + e.getMessage());
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    @Test
    public void testGetDelivery() {
        try {
            Delivery delivery = deliveryDBManager.getDelivery(1);
            Assert.assertNotNull("Delivery should not be null", delivery);
            
            Address source = delivery.getSource();
            Address destination = delivery.getDestination();
            
            Assert.assertNotNull("Source address should not be null", source);
            Assert.assertNotNull("Destination address should not be null", destination);

            Assert.assertEquals("Best Couriers", delivery.getCourier());
            Assert.assertEquals(1001, delivery.getCourierDeliveryId());

            // Check source address fields
            Assert.assertEquals(5, source.getStreetNumber());
            Assert.assertEquals("Source Avenue", source.getStreet());
            Assert.assertEquals("Source Valley", source.getSuburb());

            // Check destination address fields
            Assert.assertEquals(10, destination.getStreetNumber());
            Assert.assertEquals("Destination Avenue", destination.getStreet());
            Assert.assertEquals("Destination Valley", destination.getSuburb());
        } catch (SQLException e) {
            Assert.fail("SQLException: " + e.getMessage());
        }
    }

    @Test
    public void testAddDelivery() {
        try {
            // First, insert the test addresses into the database
            int sourceAddressId = 100;
            int destAddressId = 101;
            
            // Insert source address
            PreparedStatement insertAddressPs = conn.prepareStatement(
                "INSERT INTO Address (AddressId, StreetNumber, Street, Suburb, State, Postcode) VALUES (?, ?, ?, ?, ?, ?)"
            );
            
            // Insert source address
            insertAddressPs.setInt(1, sourceAddressId);
            insertAddressPs.setString(2, "1");
            insertAddressPs.setString(3, "Test Street");
            insertAddressPs.setString(4, "Test Suburb");
            insertAddressPs.setInt(5, AuState.NSW.ordinal());
            insertAddressPs.setString(6, "2000");
            insertAddressPs.executeUpdate();
            
            // Insert destination address
            insertAddressPs.setInt(1, destAddressId);
            insertAddressPs.setString(2, "2");
            insertAddressPs.setString(3, "Dest Street");
            insertAddressPs.setString(4, "Dest Suburb");
            insertAddressPs.setInt(5, AuState.VIC.ordinal());
            insertAddressPs.setString(6, "3000");
            insertAddressPs.executeUpdate();

            // Create Address objects with the inserted IDs
            Address source = new Address(sourceAddressId, 1, "Test Street", "Test Suburb", AuState.NSW, "2000");
            Address destination = new Address(destAddressId, 2, "Dest Street", "Dest Suburb", AuState.VIC, "3000");
            
            // Use orderId 1 which exists in the test database
            deliveryDBManager.addDelivery(999, 1, source, destination, "Test Courier", 12345);
            
            Delivery delivery = deliveryDBManager.getDelivery(999);
            Assert.assertNotNull("Added delivery should be retrievable", delivery);
            Assert.assertEquals("Test Courier", delivery.getCourier());
            Assert.assertEquals(12345, delivery.getCourierDeliveryId());
            
            // Verify the addresses were correctly associated
            Assert.assertEquals(sourceAddressId, delivery.getSource().getAddressId());
            Assert.assertEquals(destAddressId, delivery.getDestination().getAddressId());
        } catch (SQLException e) {
            Assert.fail("SQLException: " + e.getMessage());
        }
    }
}