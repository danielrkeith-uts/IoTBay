package model.dao;

import model.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import model.Enums.ProductType;

public class ProductDBManager {

    
    private final Connection conn;
    private final Statement st;

    public ProductDBManager(Connection conn) throws SQLException {
        
        this.conn = conn;
        this.st   = conn.createStatement();
    }

    public Product getProduct(int productId) throws SQLException {
        String query = "SELECT * FROM Product WHERE ProductId = " + productId;
        try (ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                return mapRowToProduct(rs);
            }
        }
        return null;
    }

    public Product getProductByName(String productName) throws SQLException {
        String query = "SELECT * FROM Product WHERE Name = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, productName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToProduct(rs);
                }
            }
        }
        return null;
    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        try (ResultSet rs = st.executeQuery("SELECT * FROM Product")) {
            while (rs.next()) {
                products.add(mapRowToProduct(rs));
            }
        }
        return products;
    }

    public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO Product (Name, Description, Type, Cost, Stock, ImageUrl) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setString(3, product.getType().name());
            ps.setDouble(4, product.getCost());
            ps.setInt(5, product.getStock());
            ps.setString(6, product.getImageUrl());
            ps.executeUpdate();
        }
    }

    public void updateProduct(String originalName, Product updatedProduct) throws SQLException {
        String sql = "UPDATE Product SET Name=?, Description=?, Type=?, Cost=?, Stock=?, ImageUrl=? WHERE Name=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, updatedProduct.getName());
            ps.setString(2, updatedProduct.getDescription());
            ps.setString(3, updatedProduct.getType().name());
            ps.setDouble(4, updatedProduct.getCost());
            ps.setInt(5, updatedProduct.getStock());
            ps.setString(6, updatedProduct.getImageUrl());
            ps.setString(7, originalName);
            ps.executeUpdate();
        }
    }

    public void deleteProduct(String name) throws SQLException {
        String sql = "DELETE FROM Product WHERE Name=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        }
    }

    /** Helper to map a ResultSet row into a Product object. */
    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        int productId = rs.getInt("ProductId");
        String name        = rs.getString("Name");
        String description = rs.getString("Description");
        ProductType type   = ProductType.valueOf(rs.getString("Type"));
        double cost        = rs.getDouble("Cost");
        int stock          = rs.getInt("Stock");
        String imageUrl    = rs.getString("ImageUrl");

        Product product = new Product(name, description, type, cost, stock, imageUrl);
        product.setProductId(productId);
        return product;
    }
}
