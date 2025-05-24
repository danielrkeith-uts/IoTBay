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
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter YEAR_MONTH_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");
    
    static Date now = new Date();
    static Timestamp timestamp = new Timestamp(now.getTime());

    static List<ProductListEntry> productList = new ArrayList<>();
    static Payment payment;
    static {
        try {
            Card card = new Card(1, "John Smith", "123456789", YearMonth.parse("2026-08", YEAR_MONTH_FORMAT), "123");
            payment = new Payment(1, 1, 23.45, card, PaymentStatus.PENDING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
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
        String formatted = DATE_FORMAT.format(order.getDatePlaced());
        Assert.assertEquals("2025-04-25 00:00:00", formatted);

        Assert.assertEquals(23.45, payment.getAmount(), 0.01);
        Assert.assertEquals(1, payment.getCard().getCardId());
        Assert.assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus());
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
        try {
            Date now = new Date();
            Timestamp timestamp = new Timestamp(now.getTime());

            List<ProductListEntry> testPLE = new ArrayList<>();
            Product product = new Product("Raspberry Pi", "", ProductType.COMPONENTS, 99.99, 3, "");
            testPLE.add(new ProductListEntry(product, 1));

            OrderStatus status = OrderStatus.PROCESSING;
            YearMonth expirationDate = YearMonth.parse("2026-08", YEAR_MONTH_FORMAT);
            Card card = new Card(1, "John Smith", "123456789", expirationDate, "123");
            Payment payment = new Payment(1, 1, 30.00, card, PaymentStatus.ACCEPTED);

            Order updatedOrder = new Order(
                order.getOrderId(),
                testPLE,
                payment,
                timestamp,
                status
            );

            orderDBManager.updateOrder(updatedOrder, 1);
            Order result = orderDBManager.getOrder(updatedOrder.getOrderId());

            Assert.assertEquals(updatedOrder.getOrderId(), result.getOrderId());
            Assert.assertEquals(payment.getPaymentId(), result.getPayment().getPaymentId());
            Assert.assertEquals(status, result.getOrderStatus());

            // Date check (using timestamp)
            String expectedDate = DATE_FORMAT.format(timestamp);
            String actualDate = DATE_FORMAT.format(result.getDatePlaced());
            Assert.assertEquals(expectedDate, actualDate);

            // Check product name
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
            
            Timestamp timestamp = new Timestamp(DatePlaced.getTime());
            orderDBManager.addOrder(OrderId, UserId, CartId, PaymentId, timestamp, statusString);
            
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
