package model.dao;

import model.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class CartDBManager {
    private Statement st;
    private Connection conn;
    private static final String UPDATE_CART_STMT = "UPDATE Cart SET LastUpdated = ? WHERE CartId = ?;";
    private final PreparedStatement updateCartPs;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
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
            try {
                String timestampStr = rs.getString("LastUpdated");
                Timestamp LastUpdated;
                
                // If timestamp string doesn't contain time part, append default time
                if (timestampStr.matches("\\d{4}-\\d{2}(-\\d{2})?")) {
                    timestampStr += timestampStr.length() == 7 ? "-01 00:00:00" : " 00:00:00";
                }
                
                LastUpdated = Timestamp.valueOf(timestampStr);

                Cart cart = new Cart();
                cart.setCartId(cartId);
                cart.setLastUpdated(LastUpdated);

                return cart;
            } catch (IllegalArgumentException e) {
                throw new SQLException("Error parsing timestamp: " + e.getMessage());
            }
        } 
        return null;
    }

    //Add a cart to the database   
    public int addCart(Timestamp LastUpdated) throws SQLException {       
        String query = "INSERT INTO Cart (LastUpdated) VALUES (?)";
        PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        String formatted = DATE_FORMAT.format(LastUpdated);
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
        String formatted = DATE_FORMAT.format(cart.getLastUpdated());
    
        updateCartPs.setString(1, formatted);
        updateCartPs.setInt(2, cart.getCartId());
    
        updateCartPs.executeUpdate();
    }

    //delete a cart from the database   
    public void deleteCart(int CartId) throws SQLException{       
        st.executeUpdate("DELETE FROM Cart WHERE CartId = " + CartId); 
    }
}