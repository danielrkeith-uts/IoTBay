package model.dao;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDBManager {
    private Connection conn;
    private Statement st;
    private static final String UPDATE_PRODUCT_STMT = "UPDATE Cart SET LastUpdated = ? WHERE CartId = ?;";
    private final PreparedStatement updateProductPs;

    public ProductDBManager(Connection conn) throws SQLException {
        this.conn = conn;
        st = conn.createStatement();
        this.updateProductPs = conn.prepareStatement(UPDATE_PRODUCT_STMT);
    }

    public Product getProduct(int ProductId) throws SQLException {
        String query = "SELECT * FROM Product WHERE ProductId = '" + ProductId + "'"; 
        ResultSet rs = st.executeQuery(query); 

        if (rs.next()) {
            String Name = rs.getString("Name");
            String Description = rs.getString("Description");
            double Cost = rs.getDouble("Cost");
            int Stock = rs.getInt("Stock");
            return new Product(Name, Description, Cost, Stock);
        }
        return null;
    }
}