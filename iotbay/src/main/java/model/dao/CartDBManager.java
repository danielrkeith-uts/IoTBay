package model.dao;

import model.*;
import java.sql.*;

public class CartDBManager {
    private Statement st;
    private Connection conn;
    private static final String UPDATE_CART_STMT = "UPDATE Cart SET LastUpdated = ? WHERE CartId = ?;";
    private final PreparedStatement updateCartPs;
        
    public CartDBManager(Connection conn) throws SQLException {    
        this.conn = conn;
        st = conn.createStatement(); 
        this.updateCartPs = conn.prepareStatement(UPDATE_CART_STMT);  
    }

    public Cart getCart(int cartId) throws SQLException {   

        String query = "SELECT * FROM Cart WHERE CartId = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, cartId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Timestamp timestamp = rs.getTimestamp("LastUpdated");
            Timestamp LastUpdated = new java.sql.Timestamp(timestamp.getTime());

            Cart cart = new Cart();
            cart.setCartId(cartId);
            cart.setLastUpdated(LastUpdated);

            return cart;
        } 
        return null;
    }
  
    public int addCart(Timestamp LastUpdated) throws SQLException {       
        String query = "INSERT INTO Cart (LastUpdated) VALUES (?)";
        PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setTimestamp(1, LastUpdated);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1); // generated cartId
        } else {
            throw new SQLException("Cart ID not generated.");
        }
    }

    public void updateCart(Cart cart) throws SQLException {
        String query = "SELECT * FROM Cart WHERE CartId = " + cart.getCartId(); 
        ResultSet rs = st.executeQuery(query); 
        Timestamp timestamp = new Timestamp(cart.getLastUpdated().getTime());

        String formatted = timestamp.toLocalDateTime().toString().replace("T", " ");
        updateCartPs.setString(1, formatted); // first ?
        updateCartPs.setInt(2, cart.getCartId()); // second ?

        updateCartPs.executeUpdate();
    } 

    public void deleteCart(int CartId) throws SQLException{       
        st.executeUpdate("DELETE FROM ProductListEntry WHERE CartId = ?"); 
    }

    public void clearCart(int cartId) throws SQLException {
        String deleteQuery = "DELETE FROM ProductListEntry WHERE CartId = ?";
        
        try (PreparedStatement pst = conn.prepareStatement(deleteQuery)) {
            pst.setInt(1, cartId);
            pst.executeUpdate();
        }
    }
}