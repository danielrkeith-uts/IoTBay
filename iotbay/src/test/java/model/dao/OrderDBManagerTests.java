package model.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import model.*;
import model.enums.PaymentStatus;

public class OrderDBManagerTests {
    OrderDBManager orderDBManager;

    public OrderDBManagerTests() throws ClassNotFoundException, SQLException {
        orderDBManager = new OrderDBManager(new DBConnector().openConnection());
    }

    @Test
    public void testGetOrder() {
        Order order;
        try {
            order = orderDBManager.getOrder(1);
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail("SQLException thrown: " + e.getMessage());
            return;
        }

        Delivery delivery = order.getDelivery();
        Payment payment = order.getPayment();
        List<ProductListEntry> productlist = order.getProductList();
        
        //Testing the DatePlaced field
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = sdf.format(order.getDatePlaced());
        Assert.assertEquals("2025-04-25", formatted);
        
        //Check Delivery fields
        Assert.assertEquals(2, delivery.getSource().getAddressId());
        Assert.assertEquals(3, delivery.getDestination().getAddressId());
        Assert.assertEquals("Best Couriers", delivery.getCourier());
        Assert.assertEquals(4, delivery.getCourierDeliveryId());

        //Check Payment fields
        Assert.assertEquals(23.45, payment.getAmount(), 0.01);
        Assert.assertEquals(1, payment.getCard().getCardId());
         Assert.assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus());

        // //Check ProductList fields
        for (int i = 0; i < productlist.size(); i++) {
            Assert.assertEquals("John Smith", productlist.get(i).getProduct());
            Assert.assertEquals("123456789", productlist.get(i).getQuantity());
        }
    }
}
