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

    //Find a cart by CartId in the database   
    public Cart getCart(int cartId) throws SQLException {   

        //get a cart from the db
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

    //Add a cart to the database   
    public int addCart(Timestamp LastUpdated) throws SQLException {       
        String query = "INSERT INTO Cart (LastUpdated) VALUES (?)";
        PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        String formatted = LastUpdated.toLocalDateTime().toString().replace("T", " "); // e.g., "2026-08-20 00:00:00"
        ps.setString(1, formatted);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1); // generated cartId
        } else {
            throw new SQLException("Cart ID not generated.");
        }
    }

    //update a cart's LastUpdated in the database   
    public void updateCart(Cart cart) throws SQLException {
        Timestamp timestamp = new Timestamp(cart.getLastUpdated().getTime());
        String formatted = timestamp.toLocalDateTime().toString().replace("T", " ");
    
        updateCartPs.setString(1, formatted);
        updateCartPs.setInt(2, cart.getCartId());
    
        updateCartPs.executeUpdate();
    }

    //delete a cart from the database   
    public void deleteCart(int CartId) throws SQLException{       
        st.executeUpdate("DELETE FROM Cart WHERE CartId = " + CartId); 
    }
}