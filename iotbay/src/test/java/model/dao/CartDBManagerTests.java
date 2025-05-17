package model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import model.*;

public class CartDBManagerTests {
    static Date LastUpdated;
    static int ProductListId;
    static Cart cart = new Cart();
    CartDBManager cartDBManager;
    private final Connection conn;

    public CartDBManagerTests() throws ClassNotFoundException, SQLException {
        cart.setCartId(1);
        this.conn = new DBConnector().openConnection();
        conn.setAutoCommit(false);
        cartDBManager = new CartDBManager(conn);
    }

    @Test
    public void testGetCart() {
        try {
            cart = cartDBManager.getCart(1);
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail("SQLException thrown: " + e.getMessage());
            return;
        }
    }

    @Test
    public void testUpdateCart() {
        try {
            cartDBManager.updateCart(cart);
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail("SQLException thrown: " + e.getMessage());
            return;
        } finally {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    @Test
    public void testAddCart() {
        try {
            Timestamp timestamp = new Timestamp(cart.getLastUpdated().getTime());
            java.sql.Date sqlDate = new java.sql.Date(timestamp.getTime());
            cartDBManager.addCart(sqlDate);
        } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail("SQLException thrown: " + e.getMessage());
            return;
        } finally {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
    }

    @Test
    public void testDeleteCart() {
       try {
            cartDBManager.deleteCart(cart.getCartId());
       } catch (SQLException e) {
            e.printStackTrace();
            Assert.fail("SQLException thrown: " + e.getMessage());
            return;
       } finally {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.err.println(e);
            }
       }
    }
}
