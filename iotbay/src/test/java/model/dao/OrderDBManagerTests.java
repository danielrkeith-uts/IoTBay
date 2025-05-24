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
    private OrderDBManager orderDBManager;
    private Connection conn;

    public OrderDBManagerTests() {
        try {
            this.conn = new DBConnector().openConnection();
            if (this.conn != null) {
                this.conn.setAutoCommit(false);
                this.orderDBManager = new OrderDBManager(this.conn);
            } else {
                throw new SQLException("Failed to establish database connection");
            }
        } catch (Exception e) {
            System.err.println("Error initializing OrderDBManagerTests: " + e.getMessage());
            if (this.conn != null) {
                try {
                    this.conn.close();
                } catch (SQLException se) {
                    System.err.println("Error closing connection: " + se.getMessage());
                }
            }
        }
    }

    @Test
    public void testGetOrder() {
        if (conn == null || orderDBManager == null) {
            Assert.fail("Database connection not initialized");
            return;
        }

        Order order = null;
        try {
            order = orderDBManager.getOrder(1);
            Assert.assertNotNull("Order should not be null", order);
            
            Payment payment = order.getPayment();
            List<ProductListEntry> productlist = order.getProductList();
            Assert.assertNotNull("Payment should not be null", payment);
            Assert.assertNotNull("Product list should not be null", productlist);
            
            //Testing the DatePlaced field
            String formatted = DATE_FORMAT.format(order.getDatePlaced());
            Assert.assertEquals("2025-04-25 00:00:00", formatted);

            Assert.assertEquals(23.45, payment.getAmount(), 0.01);
            Assert.assertEquals(1, payment.getCard().getCardId());
            Assert.assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus());
            Assert.assertEquals(1, payment.getUserId());

            Assert.assertTrue("Product list should not be empty", productlist.size() > 0);
            Assert.assertEquals("Raspberry Pi", productlist.get(0).getProduct().getName());
            Assert.assertEquals(OrderStatus.PROCESSING, order.getOrderStatus());
            
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail("SQLException thrown: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                System.err.println("Rollback failed: " + e.getMessage());
            }
        }
    }

    @Test
    public void testUpdateOrder() {
        if (conn == null || orderDBManager == null) {
            Assert.fail("Database connection not initialized");
            return;
        }

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
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                System.err.println("Rollback failed: " + e.getMessage());
            }
        }
    }

    @Test
    public void testAddOrder() {
        if (conn == null || orderDBManager == null) {
            Assert.fail("Database connection not initialized");
            return;
        }

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
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                System.err.println("Rollback failed: " + e.getMessage());
            }
        }
    }

    @Test
    public void testCancelOrder() {
        if (conn == null || orderDBManager == null) {
            Assert.fail("Database connection not initialized");
            return;
        }

        try {
            orderDBManager.cancelOrder(1);
            Order updatedOrder = orderDBManager.getOrder(1);
            Assert.assertNotNull(updatedOrder);
            Assert.assertEquals("CANCELLED", String.valueOf(updatedOrder.getOrderStatus()));
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e) {
                System.err.println("Rollback failed: " + e.getMessage());
            }
        }
    }
}
