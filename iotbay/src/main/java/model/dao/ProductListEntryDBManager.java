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
}
