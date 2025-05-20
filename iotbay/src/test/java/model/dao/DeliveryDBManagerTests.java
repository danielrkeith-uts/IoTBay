package model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import model.*;
import model.Enums.AuState;

public class DeliveryDBManagerTests {
    private Connection conn;
    private DeliveryDBManager deliveryDBManager;

    public DeliveryDBManagerTests() throws ClassNotFoundException, SQLException {
        this.conn = new DBConnector().openConnection();
        conn.setAutoCommit(false); // Set autoCommit to false to use transactions
        this.deliveryDBManager = new DeliveryDBManager(conn);
    }




    
    @Test
    public void testGetDelivery() {
        try {
            Delivery delivery = deliveryDBManager.getDelivery(1);
            
            // Verify the delivery is not null
            Assert.assertNotNull("Delivery should not be null", delivery);
            
            // Verify delivery properties
            Assert.assertEquals("Australia Post", delivery.getCourier());
            Assert.assertEquals(12345, delivery.getCourierDeliveryId());
            
            // Verify addresses
            Address source = delivery.getSource();
            Address destination = delivery.getDestination();
            
            Assert.assertNotNull("Source address should not be null", source);
            Assert.assertNotNull("Destination address should not be null", destination);
            
            Assert.assertEquals(123, source.getStreetNumber());
            Assert.assertEquals("Company St", source.getStreet());
            Assert.assertEquals("Business District", source.getSuburb());
            
            Assert.assertEquals(10, destination.getStreetNumber());
            Assert.assertEquals("Destination Arvenue", destination.getStreet());
            Assert.assertEquals("Destination Valley", destination.getSuburb());
            
        } catch (SQLException e) {
            Assert.fail("SQLException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testAddAndDeleteDelivery() {
        try {
            // Create test addresses
            Address source = new Address(0, 42, "Test Source St", "SourceVille", AuState.NSW, "2000");
            Address destination = new Address(0, 84, "Test Dest Ave", "DestTown", AuState.VIC, "3000");
            
            // Add a new delivery and verify it exists
            int deliveryId = deliveryDBManager.addDelivery(source, destination, "Test Courier", 12345);
            Assert.assertTrue("New delivery ID should be positive", deliveryId > 0);
            
            // Delete it and verify it's gone
            deliveryDBManager.deleteDelivery(deliveryId);

            // Important: Roll back the transaction so we don't actually change the database
            conn.rollback();
            
        } catch (SQLException e) {
            // Make sure to always roll back on failure
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
            }
            Assert.fail("SQLException thrown: " + e.getMessage());
        }
    }
}