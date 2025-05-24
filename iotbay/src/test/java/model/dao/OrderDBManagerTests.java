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
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;
import model.*;
import model.Enums.*;

public class OrderDBManagerTests {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    static Date datePlaced;
    static {
        try {
            datePlaced = sdf.parse("2025-04-25");
        } catch (ParseException e) {
            e.printStackTrace();
            datePlaced = new Date();
        }
    }

    static List<ProductListEntry> productList = new ArrayList<>();
    static YearMonth expirationDate = YearMonth.parse("2026-08", DateTimeFormatter.ofPattern("yyyy-MM"));
    static Card card = new Card(1, "John Smith", "123456789", expirationDate, "123");
    static int userId = 1;
    static Timestamp timestamp = new Timestamp(datePlaced.getTime());
    static OrderStatus status = OrderStatus.PROCESSING;
    
    static Payment payment = new Payment(1, 30.0, card, PaymentStatus.ACCEPTED, new Date(), userId);

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
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String formatted = sdf.format(order.getDatePlaced());
        Assert.assertEquals("2025-04-25 00:00:00.000", formatted);

        Assert.assertEquals(23.45, payment.getAmount(), 0.01);
        Assert.assertEquals(1, payment.getCard().getCardId());
        Assert.assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus());
        
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String paymentDateFormatted = sdf2.format(payment.getDate());
        Assert.assertEquals("2025-04-25", paymentDateFormatted);

        Assert.assertEquals(1, payment.getUserId());

        List<String> expectedNames = Arrays.asList("Google Home Voice Controller", "Philips Hue Smart Bulbs");
        Assert.assertEquals(expectedNames.size(), productlist.size());

        for (int i = 0; i < productlist.size(); i++) {
            String actualName = productlist.get(i).getProduct().getName();
            Assert.assertEquals(expectedNames.get(i), actualName);
        }

        Assert.assertEquals(OrderStatus.PROCESSING, order.getOrderStatus());
    }

    @Test
    public void testUpdateOrder() {
        int testOrderId = 99;
        Date now = new Date();
        Timestamp timestamp = new Timestamp(now.getTime());

        List<ProductListEntry> testPLE = new ArrayList<>();
        Product product = new Product("Raspberry Pi", "", ProductType.COMPONENTS, 99.99, 3, "");
        testPLE.add(new ProductListEntry(product, 1));

        OrderStatus status = OrderStatus.PROCESSING;
        String expirationDateString = "2026-08";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth expirationDate = YearMonth.parse(expirationDateString, formatter);

        Payment payment = new Payment(1, 30.00, new Card(1, "John Smith", "123456789", expirationDate, "123"), PaymentStatus.ACCEPTED, new Date(), userId);

        Order updatedOrder = new Order(
            testOrderId,
            testPLE,
            payment,
            timestamp,
            status
        );

        try {
            int cartId = 1;
            orderDBManager.addOrder(testOrderId, userId, cartId, payment.getPaymentId(), timestamp, status.name());
            conn.createStatement().executeUpdate("DELETE FROM ProductListEntry WHERE CartId = 1");

            orderDBManager.updateOrder(updatedOrder, cartId);
            Order result = orderDBManager.getOrder(testOrderId);

            Assert.assertEquals(testOrderId, result.getOrderId());
            Assert.assertEquals(payment.getPaymentId(), result.getPayment().getPaymentId());
            Assert.assertEquals(status, result.getOrderStatus());
            Assert.assertEquals(timestamp.getTime(), result.getDatePlaced().getTime());

            Assert.assertEquals("Raspberry Pi", result.getProductList().get(0).getProduct().getName());

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
            int OrderId = 999;
            int UserId = 1;
            int CartId = 1;
            int PaymentId = 1;
            Date DatePlaced = new Date();
            String statusString = "PROCESSING";
            
            orderDBManager.addOrder(OrderId, UserId, CartId, PaymentId, new java.sql.Timestamp(DatePlaced.getTime()), statusString);
            
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
