package model.dao;

import model.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import model.Enums.ProductType;

public class ProductDBManager {
    private Statement st;

    public ProductDBManager(Connection conn) throws SQLException {
        st = conn.createStatement();
    }

    public Product getProduct(int ProductId) throws SQLException {
        String query = "SELECT * FROM Product WHERE ProductId = '" + ProductId + "'"; 
        ResultSet rs = st.executeQuery(query); 

        if (rs.next()) {
            String Name = rs.getString("Name");
            String Description = rs.getString("Description");
            String typeStr = rs.getString("Type"); 
            ProductType type = ProductType.valueOf(typeStr); 
            double Cost = rs.getDouble("Cost");
            int Stock = rs.getInt("Stock");
            String imageUrl = rs.getString("ImageUrl");
            return new Product(Name, Description, type, Cost, Stock, imageUrl);
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
        String typeStr = rs.getString("Type");
        ProductType type = ProductType.valueOf(typeStr);
        double cost = rs.getDouble("Cost");
        int stock = rs.getInt("Stock");
        String imageUrl = rs.getString("ImageUrl");
        products.add(new Product(name, description, type, cost, stock, imageUrl));
    }

    return products;
}

public void addProduct(Product product) throws SQLException {
    String query = "INSERT INTO Product (Name, Description, Type, Cost, Stock, ImageUrl) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement pstmt = st.getConnection().prepareStatement(query)) {
        pstmt.setString(1, product.getName());
        pstmt.setString(2, product.getDescription());
        pstmt.setString(3, product.getType().name());
        pstmt.setDouble(4, product.getCost());
        pstmt.setInt(5, product.getStock());
        pstmt.setString(6, product.getImageUrl());
        pstmt.executeUpdate();
    }
}

public void updateProduct(String originalName, Product updatedProduct) throws SQLException {
    String query = "UPDATE Product SET Name=?, Description=?, Type=?, Cost=?, Stock=?, ImageUrl=? WHERE Name=?";
    try (PreparedStatement pstmt = st.getConnection().prepareStatement(query)) {
        pstmt.setString(1, updatedProduct.getName());
        pstmt.setString(2, updatedProduct.getDescription());
        pstmt.setString(3, updatedProduct.getType().name());
        pstmt.setDouble(4, updatedProduct.getCost());
        pstmt.setInt(5, updatedProduct.getStock());
        pstmt.setString(6, updatedProduct.getImageUrl());
        pstmt.setString(7, originalName);
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
