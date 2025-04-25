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

    public ProductListEntry getProductListEntry(int ProductId, int ProductListId) throws SQLException {

        //create a product instance first
        ProductDBManager productDBManager = new ProductDBManager(conn);
        Product Product = productDBManager.getProduct(ProductId);

        String query = "SELECT * FROM ProductListEntry WHERE ProductListId = '" + ProductListId + "'"; 
        ResultSet rs = st.executeQuery(query);      
        int Quantity = rs.getInt("Quantity");

        return new ProductListEntry(Product, Quantity); 
    }

    public List<ProductListEntry> getProductList(int ProductListId) throws SQLException {
        String query = "SELECT * FROM ProductListEntry WHERE ProductListId = '" + ProductListId + "'"; 
        ResultSet rs = st.executeQuery(query);      

        List<ProductListEntry> productList = new ArrayList<>();
        ProductDBManager productDBManager = new ProductDBManager(conn);

        while (rs.next()) {
            int ProductId = rs.getInt("ProductId");
            Product Product = productDBManager.getProduct(ProductId);
            int Quantity = rs.getInt("Quantity");
            ProductListEntry entry = new ProductListEntry(Product, Quantity);
            productList.add(entry);
            return productList; 
        }
        return null;
    }
}
