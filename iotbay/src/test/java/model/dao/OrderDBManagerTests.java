package model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import model.*;
import model.Enums.*;

public class OrderDBManagerTests {
    static Date now = new Date();
    static Timestamp timestamp = new Timestamp(now.getTime());

    static List<ProductListEntry> productList = new ArrayList<>();
    static Payment payment = new Payment(0, null, null);
    static String statusString = "PROCESSING";
    static OrderStatus status = OrderStatus.valueOf(statusString);

    private static Order order = new Order(1, productList, payment, timestamp, status);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String formatted = sdf.format(order.getDatePlaced());
        Assert.assertEquals("2025-04-25 00:00:00.000", formatted);

        //Check Payment fields
        Assert.assertEquals(23.45, payment.getAmount(), 0.01);
        Assert.assertEquals(1, payment.getCard().getCardId());
        Assert.assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus());

        //Check ProductList fields
        List<String> expectedNames = Arrays.asList("Google Home Voice Controller", "Philips Hue Smart Bulbs");
        Assert.assertEquals(expectedNames.size(), productlist.size());

        for (int i = 0; i < productlist.size(); i++) {
            String actualName = productlist.get(i).getProduct().getName();
            Assert.assertEquals(expectedNames.get(i), actualName);
        }

        //Check OrderStatus field
        Assert.assertEquals(OrderStatus.PROCESSING, order.getOrderStatus());
    }

    @Test
    public void testUpdateOrder() throws SQLException {
        Date now = new Date();
        Timestamp timestamp = new Timestamp(now.getTime());

        List<ProductListEntry> testPLE = new ArrayList<>();
        Product product = new Product("Raspberry Pi", "", ProductType.COMPONENTS, 99.99, 3, "");
        testPLE.add(new ProductListEntry(product, 1));

        OrderStatus status = OrderStatus.PROCESSING;
        String expirationDateString = "2026-08";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth expirationDate = YearMonth.parse(expirationDateString, formatter);

        PaymentDBManager paymentDBManager = new PaymentDBManager(conn);
        int generatedPaymentId = paymentDBManager.addPayment(1, 130.00, PaymentStatus.ACCEPTED.ordinal());
        payment.setPaymentId(generatedPaymentId);

        Payment payment = new Payment(130.00, new Card(1, "John Smith", "123456789", expirationDate, "123"), PaymentStatus.ACCEPTED);
        payment.setPaymentId(generatedPaymentId);

        Order updatedOrder = new Order(
            order.getOrderId(),
            testPLE,
            payment,
            timestamp,
            status
        );

        try {
            orderDBManager.updateOrder(updatedOrder);
            Order result = orderDBManager.getOrder(updatedOrder.getOrderId());

            Assert.assertEquals(updatedOrder.getOrderId(), result.getOrderId());
            Assert.assertEquals(updatedOrder.getPayment().getPaymentId(), result.getPayment().getPaymentId());
            Assert.assertEquals(status, result.getOrderStatus());

            // Date check (using timestamp)
            Assert.assertEquals(timestamp.getTime() / 1000, result.getDatePlaced().getTime() / 1000);

            // Optional: check product name
            Assert.assertEquals("Google Home Voice Controller", result.getProductList().get(0).getProduct().getName());

        } catch (SQLException e) {
            Assert.fail("SQLException: " + e.getMessage());
        } finally {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.err.println("Rollback failed: " + e.getMessage());
            }
        }
    }

    @Test
    public void testAddOrder() {
        try {
            int UserId = 1;
            int CartId = 1;
            int PaymentId = 1;
            Timestamp DatePlaced = new Timestamp(System.currentTimeMillis());
            String statusString = "PROCESSING";
            
            // Get the generated OrderId from the insert
            int generatedOrderId = orderDBManager.addOrder(
                UserId, 
                CartId, 
                PaymentId, 
                new java.sql.Timestamp(DatePlaced.getTime()), 
                statusString
            );

            Order order = orderDBManager.getOrder(generatedOrderId); // Use returned ID
            Assert.assertNotNull(order);
            Assert.assertEquals(generatedOrderId, order.getOrderId());

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
