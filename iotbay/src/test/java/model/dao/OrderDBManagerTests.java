package model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import model.*;
import model.Enums.*;

public class OrderDBManagerTests {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static Date datePlaced;
    static {
        try {
            datePlaced = sdf.parse("2025-04-25 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
            datePlaced = new Date(); // Fallback option
        }
    }

    static List<ProductListEntry> productList = new ArrayList<>();
    static Payment payment = new Payment(0, 0, null, null);
    static String statusString = "PROCESSING";
    static OrderStatus status = OrderStatus.valueOf(statusString);

    private static Order order = new Order(1, productList, payment, datePlaced, status);
    OrderDBManager orderDBManager;
    private final Connection conn;

    public OrderDBManagerTests() throws ClassNotFoundException, SQLException {
        this.conn = new DBConnector().openConnection();
        conn.setAutoCommit(false);
        orderDBManager = new OrderDBManager(conn);
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

        Payment payment = order.getPayment();
        List<ProductListEntry> productlist = order.getProductList();
        
        //Testing the DatePlaced field
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = sdf.format(order.getDatePlaced());
        Assert.assertEquals("2025-04-25", formatted);

        //Check Payment fields
        Assert.assertEquals(23.45, payment.getAmount(), 0.01);
        Assert.assertEquals(1, payment.getCard().getCardId());
        Assert.assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus());

        //Check ProductList fields
        for (int i = 0; i < productlist.size(); i++) {
            Assert.assertEquals("Raspberry Pi", productlist.get(i).getProduct().getName());
            Assert.assertEquals(1, productlist.get(i).getQuantity());
        }

        //Check OrderStatus field
        Assert.assertEquals(OrderStatus.PROCESSING, order.getOrderStatus());
    }

    @Test
    public void testUpdateOrder() {
        Date newDate = new Date();
        List<ProductListEntry> testPLE = new ArrayList<ProductListEntry>();
        Product product = new Product("Raspberry Pi", "", 99.99, 3);
        testPLE.add(new ProductListEntry(product,1));
        String statusString = "PROCESSING";
        OrderStatus status = OrderStatus.valueOf(statusString);
        Order newOrder = new Order(
            order.getOrderId(),
            testPLE,
            order.getPayment(),
            newDate,
            status
        );

        try {
            orderDBManager.updateOrder(newOrder);
            Order newOrderResult = (Order) orderDBManager.getOrder(newOrder.getOrderId());
            Assert.assertEquals(newOrderResult.getDatePlaced(), newDate);
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
    public void testAddOrder() {
        try {
            int OrderId = 999;
            int UserId = 1;
            int CartId = 1;
            int PaymentId = 1;
            Date DatePlaced = new Date();
            String statusString = "PROCESSING";
            
            orderDBManager.addOrder(OrderId, UserId, CartId, PaymentId, new java.sql.Date(DatePlaced.getTime()), statusString);
            
            Order order = orderDBManager.getOrder(OrderId);
            Assert.assertNotNull(order);
            Assert.assertEquals(OrderId, order.getOrderId());

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
    public void testCancelOrder() {
        try {
            orderDBManager.cancelOrder(1);
            Order updatedOrder = orderDBManager.getOrder(1);
            Assert.assertNotNull(updatedOrder);
            Assert.assertEquals("CANCELLED", String.valueOf(updatedOrder.getOrderStatus()));
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
