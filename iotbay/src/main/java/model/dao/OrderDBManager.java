package model.dao;

import model.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class OrderDBManager {
    
    private Statement st;
    private Connection conn;
        
    public OrderDBManager(Connection conn) throws SQLException {       
        this.conn = conn;
        st = conn.createStatement();   
    }

    //Find an order by OrderId in the database   
    public Order getOrder(int inputOrderId) throws SQLException {       
        //get the ProductListId, PaymentId, and DatePlaced from the Order table
        String query = "SELECT * FROM `Order` WHERE OrderID = '" + inputOrderId + "'"; 
        ResultSet rs = st.executeQuery(query); 

        if (rs.next()) {
            int ProductListId = rs.getInt("ProductListId");
            int PaymentId = rs.getInt("PaymentId");
            int DeliveryId = rs.getInt("DeliveryId");
            Timestamp timestamp = rs.getTimestamp("DatePlaced");
            Date DatePlaced = new Date(timestamp.getTime());
            
            //Step 1: Get all entries from ProductListEntry table with this ProductListId
            String entryQuery = "SELECT * FROM ProductListEntry WHERE ProductListId = '" + ProductListId + "'"; 
            ResultSet entryRs = st.executeQuery(entryQuery);

            List<ProductListEntry> ProductList = new ArrayList<>();
            ProductDBManager productDBManager = new ProductDBManager(conn);

            while (entryRs.next()) {
                int ProductId = entryRs.getInt("ProductId");
                int Quantity = entryRs.getInt("Quantity");

                //Retrieves a product and creates a ProductListEntry
                Product Product = productDBManager.getProduct(ProductId);
                ProductListEntry Entry = new ProductListEntry(Product, Quantity);
                ProductList.add(Entry);
            }

            //Step 3: Create Payment instance to add to Order constructor
            PaymentDBManager paymentDBManager = new PaymentDBManager(conn);
            Payment Payment = paymentDBManager.getPayment(PaymentId);

            //Step 4: Create Delivery instance to get DeliveryId for setDelivery()
            DeliveryDBManager deliveryDBManager = new DeliveryDBManager(conn);
            Delivery Delivery = deliveryDBManager.getDelivery(DeliveryId);

            Order order = new Order(ProductList, Payment, DatePlaced);
            order.setDelivery(Delivery);
            return order;
        } 
        return null;
    }

    //Add an order into the database   
    public void addOrder(int OrderId, int UserId, int ProductListId, int PaymentId, int DeliveryId, Date DatePlaced) throws SQLException {       
        st.executeUpdate("INSERT INTO `Order` VALUES ('" + OrderId + "', '" + UserId + "', '" + ProductListId + "', '" + PaymentId + "', " + DeliveryId + "', " + DatePlaced + ")");   
    }

    //update an order's details in the database   
    public void updateOrder(int OrderId, int UserId, int ProductListId, int PaymentId, int DeliveryId, Date DatePlaced) throws SQLException {       
        st.executeUpdate("UPDATE `Order` SET UserId = '" + UserId + "', ProductListId = '" + ProductListId + "', PaymentId = '" + PaymentId + "', DeliveryId = '" + DeliveryId + "', DatePlaced = '" + DatePlaced + "' WHERE OrderId = '" + OrderId);    
    }       

    //delete an order from the database   
    public void deleteOrder(int OrderId) throws SQLException{       
        st.executeUpdate("DELETE FROM `Order` WHERE OrderId = '" + OrderId); 
    }
}