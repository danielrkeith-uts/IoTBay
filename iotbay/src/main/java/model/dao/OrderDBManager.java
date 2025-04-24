package model.dao;

import model.*;
import model.dao.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class OrderDBManager {
    
    private Statement st;
        
    public OrderDBManager(Connection conn) throws SQLException {       
        st = conn.createStatement();   
    }

    //Find an order by OrderId in the database   
    public Order findOrder(int inputOrderId) throws SQLException {       
        //get the ProductListId, PaymentId, DeliveryId, and DatePlaced from the Order table
        String query = "SELECT * FROM Order WHERE OrderID = '" + inputOrderId + "'"; 
        ResultSet rs = st.executeQuery(query);      
        while (rs.next()) {
            int OrderId = rs.getInt("OrderID");
            if (inputOrderId == OrderId) {
                int ProductListId = rs.getInt("ProductListId");
                int PaymentId = rs.getInt("PaymentId");
                int DeliveryId = rs.getInt("DeliveryId");
                
                //create ProductList instance to add to Order constructor
                List<ProductListEntry> ProductList = new ArrayList<>();
                ProductListEntryDBManager productListEntryDBManager = new ProductListEntryDBManager();
                Product Product = productListEntryDBManager.getProductListEntry(ProductId, ProductListId);

                ProductDBManager productDBManager = new ProductDBManager();
                ProductListEntry Entry = new ProductListEntry(Product, Quantity);
                
                ProductList.add(Entry);

                //create Payment instance to add to Order constructor
                Payment payment;

                //create Delivery instance to add to Order constructor
                Delivery delivery;
            } else {
                return null;
            }
        }
        return null;
    }

    //Add an order into the database   
    public void addOrder(int OrderId, int UserId, int ProductListId, int PaymentId, int DeliveryId, Date DatePlaced) throws SQLException {       
        st.executeUpdate("INSERT INTO Order VALUES ('" + OrderId + "', '" + UserId + "', '" + ProductListId + "', '" + PaymentId + "', " + DeliveryId + "', " + DatePlaced + ")");   
    }

    //update an order's details in the database   
    public void updateOrder(int OrderId, int UserId, int ProductListId, int PaymentId, int DeliveryId, Date DatePlaced) throws SQLException {       
        st.executeUpdate("UPDATE Order SET UserId = '" + UserId + "', ProductListId = '" + ProductListId + "', PaymentId = '" + PaymentId + "', DeliveryId = '" + DeliveryId + "', DatePlaced = '" + DatePlaced + "' WHERE OrderId = '" + OrderId);    
    }       

    //delete an order from the database   
    public void deleteOrder(int OrderId) throws SQLException{       
        st.executeUpdate("DELETE FROM Order WHERE OrderId = '" + OrderId); 
    }
}