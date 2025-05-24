package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.*;

public class ProductListEntryDBManager {

    private Statement st;
    private Connection conn;

    public ProductListEntryDBManager(Connection conn) throws SQLException {
        this.conn = conn;
        st = conn.createStatement();
    }

    public ProductListEntry getProductListEntry(int ProductId, int CartId) throws SQLException {

        //create a product instance first
        ProductDBManager productDBManager = new ProductDBManager(conn);
        Product Product = productDBManager.getProduct(ProductId);

        String query = "SELECT * FROM ProductListEntry WHERE CartId = " + CartId + " AND ProductId = " + ProductId; 
        ResultSet rs = st.executeQuery(query);
        
        if (rs.next()) {
            int Quantity = rs.getInt("Qauntity");
            return new ProductListEntry(Product, Quantity); 
        } else {
            //the product isn't in the cart
            return null;
        }  
    }

    public List<ProductListEntry> getProductList(int CartId) throws SQLException {
        String query = "SELECT * FROM ProductListEntry WHERE CartId = '" + CartId + "'"; 
        ResultSet rs = st.executeQuery(query);      

        List<ProductListEntry> productList = new ArrayList<>();
        ProductDBManager productDBManager = new ProductDBManager(conn);

        while (rs.next()) {
            int ProductId = rs.getInt("ProductId");
            Product Product = productDBManager.getProduct(ProductId);
            int Quantity = rs.getInt("Quantity");
            ProductListEntry entry = new ProductListEntry(Product, Quantity);
            productList.add(entry);
        }
        return productList;
    }

    public void addProduct(int CartId, int ProductId, int Quantity) throws SQLException {
        // First check if the product is already in the cart
        String selectQuery = "SELECT Quantity FROM ProductListEntry WHERE CartId = ? AND ProductId = ?";
        PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
        selectStmt.setInt(1, CartId);
        selectStmt.setInt(2, ProductId);
        ResultSet rs = selectStmt.executeQuery();

        if (rs.next()) {
            //The product is in the cart --> update quantity
            Quantity = rs.getInt("Quantity");
            String updateQuery = "UPDATE ProductListEntry SET Quantity = ? WHERE CartId = ? AND ProductId = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, Quantity + 1);
            updateStmt.setInt(2, CartId);
            updateStmt.setInt(3, ProductId);
            updateStmt.executeUpdate();
        } else {
            //The product is not in the cart --> add it
            String insertQuery = "INSERT INTO ProductListEntry (CartId, ProductId, Quantity) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setInt(1, CartId);
            insertStmt.setInt(2, ProductId);
            insertStmt.setInt(3, Quantity);
            insertStmt.executeUpdate();
        }
    }
}
