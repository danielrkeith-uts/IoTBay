package model.dao;

import model.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

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
            String imageUrl = rs.getString("ImageUrl");
            return new Product(Name, Description, Cost, Stock, imageUrl);
        }
        return null;
    }



public List<Product> getAllProducts() throws SQLException {
    List<Product> products = new ArrayList<>();
    String query = "SELECT * FROM Product";
    ResultSet rs = st.executeQuery(query);

    while (rs.next()) {
        String name = rs.getString("Name");
        String description = rs.getString("Description");
        double cost = rs.getDouble("Cost");
        int stock = rs.getInt("Stock");
        String imageUrl = rs.getString("ImageUrl");
        products.add(new Product(name, description, cost, stock, imageUrl));
    }

    return products;
}

public void addProduct(Product product) throws SQLException {
    String query = "INSERT INTO Product (Name, Description, Cost, Stock, ImageUrl) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement pstmt = st.getConnection().prepareStatement(query)) {
        pstmt.setString(1, product.getName());
        pstmt.setString(2, product.getDescription());
        pstmt.setDouble(3, product.getCost());
        pstmt.setInt(4, product.getStock());
        pstmt.setString(5, product.getImageUrl());
        pstmt.executeUpdate();
    }
}

public void updateProduct(String originalName, Product updatedProduct) throws SQLException {
    String query = "UPDATE Product SET Name=?, Description=?, Cost=?, Stock=?, ImageUrl=? WHERE Name=?";
    try (PreparedStatement pstmt = st.getConnection().prepareStatement(query)) {
        pstmt.setString(1, updatedProduct.getName());
        pstmt.setString(2, updatedProduct.getDescription());
        pstmt.setDouble(3, updatedProduct.getCost());
        pstmt.setInt(4, updatedProduct.getStock());
        pstmt.setString(5, updatedProduct.getImageUrl());
        pstmt.setString(6, originalName);
        pstmt.executeUpdate();
    }
}

public void deleteProduct(String name) throws SQLException {
    String query = "DELETE FROM Product WHERE Name=?";
    try (PreparedStatement pstmt = st.getConnection().prepareStatement(query)) {
        pstmt.setString(1, name);
        pstmt.executeUpdate();
    }
} }
