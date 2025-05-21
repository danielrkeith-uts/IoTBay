package model.dao;

import model.*;
import java.sql.*;

public class ProductDBManager {
    private Connection conn;
    private Statement st;

    public ProductDBManager(Connection conn) throws SQLException {
        this.conn = conn;
        this.st = conn.createStatement();
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