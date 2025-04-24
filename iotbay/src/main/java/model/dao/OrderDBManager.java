package model.dao;

import model.*;
import java.sql.*;
import java.util.List;

public class OrderDBManager {
    
    private Statement st;
        
    public OrderDBManager(Connection conn) throws SQLException {       
        st = conn.createStatement();   
    }

    //Find an order by OrderId in the database   
    public Order findOrder(int inputOrderId) throws SQLException {       
        //get the ProductListId, PaymentId, DeliveryId, and DatePlaced from the Order table
        String firstQuery = "SELECT * FROM Order WHERE OrderID = '" + inputOrderId + "'"; 
        ResultSet rs = st.executeQuery(firstQuery);      
        while (rs.next()) {
            int OrderId = rs.getInt("OrderID");
            if (inputOrderId == OrderId) {
                int ProductListId = rs.getInt("ProductListId");
                int PaymentId = rs.getInt("PaymentId");
                int DeliveryId = rs.getInt("DeliveryId");
                //return new Order(ProductListId, PaymentId, DeliveryId);
            }
        }

        //create ProductList instance to add to Order constructor


        //create Payment instance to add to Order constructor


        //create Delivery instance to add to Order constructor

        return null;   
    }

    //Add an order into the database   
    public void addOrder(int OrderId, int UserId, int ProductListId, int PaymentId, int DeliveryId,, Date DatePlaced) throws SQLException {       
        st.executeUpdate("INSERT INTO Order VALUES ('" + OrderId + "', '" + UserId + "', '" + ProductListId + "', '" + PaymentId + "', " + DeliveryId + "', " + DatePlaced + ")");   
    }

    //update an order's details in the database   
    public void updateOrder(int OrderId, int UserId, int ProductListId, int PaymentId, int DeliveryId,, Date DatePlaced) throws SQLException {       
        st.executeUpdate("UPDATE Order SET UserId = '" + UserId + "', ProductListId = '" + ProductListId + "', PaymentId = '" + PaymentId + "', DeliveryId = '" + DeliveryId + "', DatePlaced = '" + DatePlaced + "' WHERE OrderId = '" + OrderId);    
    }       

    //delete an order from the database   
    public void deleteOrder(int OrderId) throws SQLException{       
        st.executeUpdate("DELETE FROM Order WHERE OrderId = '" + OrderId); 
    }
}