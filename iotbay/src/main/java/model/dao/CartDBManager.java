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
            Date LastUpdated = new Date(timestamp.getTime());

            Cart cart = new Cart();
            cart.setCartId(cartId);
            cart.setLastUpdated(LastUpdated);

            return cart;
        } 
        return null;
    }

    //Add a cart to the database   
    public int addCart(Date LastUpdated) throws SQLException {       
        String query = "INSERT INTO Cart (LastUpdated) VALUES (?)";
        PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setDate(1, LastUpdated);
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
        String query = "SELECT * FROM Cart WHERE CartId = " + cart.getCartId(); 
        ResultSet rs = st.executeQuery(query); 
        Timestamp timestamp = new Timestamp(cart.getLastUpdated().getTime());
        java.sql.Date sqlDate = new java.sql.Date(timestamp.getTime());

        updateCartPs.setInt(1, rs.getInt("CartId"));
        updateCartPs.setDate(2, sqlDate);

        updateCartPs.executeUpdate();
    } 

    //delete a cart from the database   
    public void deleteCart(int CartId) throws SQLException{       
        st.executeUpdate("DELETE FROM Cart WHERE CartId = " + CartId); 
    }
}