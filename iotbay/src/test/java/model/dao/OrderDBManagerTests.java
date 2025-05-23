package model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import model.*;
import model.Enums.PaymentStatus;

public class OrderDBManagerTests {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    static Date datePlaced;
    static {
        try {
            datePlaced = sdf.parse("2025-04-25");
        } catch (ParseException e) {
            e.printStackTrace();
            datePlaced = new Date(); // Fallback option
        }
    }

    static List<ProductListEntry> productList = new ArrayList<>();
    static Payment payment = new Payment(0.0, null, PaymentStatus.PENDING, new Date(), 0);

    private static Order order = new Order(1, productList, payment, datePlaced);
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
        Assert.assertEquals(new Date(1745539200), payment.getDate());
        Assert.assertEquals(0, payment.getUserId());

        // //Check ProductList fields
        for (int i = 0; i < productlist.size(); i++) {
            Assert.assertEquals("Raspberry Pi", productlist.get(i).getProduct().getName());
            Assert.assertEquals(1, productlist.get(i).getQuantity());
        }
    }

    @Test
    public void testUpdateOrder() {
        Date newDate = new Date();
        List<ProductListEntry> testPLE = new ArrayList<ProductListEntry>();
        Product product = new Product("Raspberry Pi", "", 99.99, 3);
        testPLE.add(new ProductListEntry(product,1));
        Order newOrder = new Order(
            order.getOrderId(),
            testPLE,
            order.getPayment(),
            newDate
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
    public void testDeleteOrder() {
        try {
            orderDBManager.deleteOrder(1);
            Order deletedOrder = orderDBManager.getOrder(1);
            Assert.assertNull(deletedOrder);
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
