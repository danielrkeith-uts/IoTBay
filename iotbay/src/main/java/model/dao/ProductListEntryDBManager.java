package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.*;

public class ProductListEntryDBManager {

    private Statement st;

    public ProductListEntry getProductListEntry(int ProductId, int ProductListId) throws SQLException {

        //create a product instance first
        ProductDBManager productDBManager = new ProductDBManager();
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
        ProductDBManager productDBManager = new ProductDBManager();

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
