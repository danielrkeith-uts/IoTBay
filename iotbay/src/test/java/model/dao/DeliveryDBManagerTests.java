package model.dao;

import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import model.*;

public class DeliveryDBManagerTests {
    DeliveryDBManager deliveryDBManager;

    public DeliveryDBManagerTests() throws ClassNotFoundException, SQLException {
        deliveryDBManager = new DeliveryDBManager(new DBConnector().openConnection());
    }

    @Test
    public void testGetDelivery() {
        Delivery delivery;
        try {
            delivery = deliveryDBManager.getDelivery(1);
        } catch (SQLException e) {
            Assert.fail();
            return;
        }

        Assert.assertNotNull(delivery);
        

        Address source = delivery.getSource();
        Address destination = delivery.getDestination();

        Assert.assertEquals("Best Couriers", delivery.getCourier());
        Assert.assertEquals(4, delivery.getCourierDeliveryId());

        // Check source address fields
        Assert.assertEquals(5, source.getStreetNumber());
        Assert.assertEquals("Source Avenue", source.getStreet());
        Assert.assertEquals("Source Valley", source.getSuburb());

        // Check destination address fields
        Assert.assertEquals(10, destination.getStreetNumber());
        Assert.assertEquals("Destination Avenue", destination.getStreet());
        Assert.assertEquals("Destination Valley", destination.getSuburb());
    }
}
