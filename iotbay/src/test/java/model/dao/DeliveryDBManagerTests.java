package model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import model.*;
import model.Enums.AuState;

public class DeliveryDBManagerTests {
    private DeliveryDBManager deliveryDBManager;
    private Connection conn;

    public DeliveryDBManagerTests() throws ClassNotFoundException, SQLException {
        // Open connection and disable auto-commit for test safety
        conn = new DBConnector().openConnection();
        conn.setAutoCommit(false);
        deliveryDBManager = new DeliveryDBManager(conn);
    }

    @Test
    public void testGetDelivery() throws SQLException {
        // Get delivery with ID 1
        Delivery delivery = deliveryDBManager.getDelivery(1);
        
        // Basic delivery verification
        Assert.assertNotNull(delivery);
        Assert.assertEquals("Australia Post", delivery.getCourier());
        Assert.assertEquals(12345, delivery.getCourierDeliveryId());
        
        // Source address verification
        Address source = delivery.getSource();
        Assert.assertNotNull(source);
        Assert.assertEquals(5, source.getStreetNumber());
        Assert.assertEquals("Source Avenue", source.getStreet());
        
        // Destination address verification
        Address destination = delivery.getDestination();
        Assert.assertNotNull(destination);
        Assert.assertEquals(10, destination.getStreetNumber());
        Assert.assertEquals("Destination Avenue", destination.getStreet());
    }

    @Test
    public void testAddAndDeleteDelivery() throws SQLException {
        try {
            // Test data
            Address source = new Address(0, 42, "Test St", "SourceVille", AuState.NSW, "2000");
            Address destination = new Address(0, 84, "Test Ave", "DestTown", AuState.VIC, "3000");
            
            // Test adding delivery
            int deliveryId = deliveryDBManager.addDelivery(source, destination, "Test Courier", 12345);
            Assert.assertTrue(deliveryId > 0);
            
            // Test deleting delivery
            deliveryDBManager.deleteDelivery(deliveryId);
        } finally {
            // Always rollback to keep database clean
            conn.rollback();
        }
    }
}