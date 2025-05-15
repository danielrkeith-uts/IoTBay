package model.dao;

import model.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class OrderDBManager {
    private Statement st;
    private Connection conn;
    private static final String UPDATE_ORDER_STMT = "UPDATE `Order` SET UserId = ?, ProductListId = ?, PaymentId = ?, DatePlaced = ? WHERE OrderId = ?;";
    private final PreparedStatement updateOrderPs;
        
    public OrderDBManager(Connection conn) throws SQLException {    
        this.conn = conn;
        st = conn.createStatement(); 
        this.updateOrderPs = conn.prepareStatement(UPDATE_ORDER_STMT);  
    }

    //Find an order by OrderId in the database   
    public Order getOrder(int inputOrderId) throws SQLException {       
        //get the ProductListId, PaymentId, and DatePlaced from the Order table
        String query = "SELECT * FROM `Order` WHERE OrderID = '" + inputOrderId + "'"; 
        ResultSet rs = st.executeQuery(query); 

        if (rs.next()) {
            int orderId = rs.getInt("OrderID");
            int ProductListId = rs.getInt("ProductListId");
            int PaymentId = rs.getInt("PaymentId");
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

            Order order = new Order(orderId, ProductList, Payment, DatePlaced);
            return order;
        } 
        return null;
    }

    //Add an order into the database   
    public void addOrder(int OrderId, int UserId, int ProductListId, int PaymentId, Date DatePlaced) throws SQLException {       
        st.executeUpdate("INSERT INTO `Order` VALUES ('" + OrderId + "', '" + UserId + "', '" + ProductListId + "', '" + PaymentId + "', " + DatePlaced + ")");   
    }

    //update an order's details in the database   
    public void updateOrder(Order order) throws SQLException {
        String query = "SELECT UserId, ProductListId, PaymentId, DatePlaced, OrderId FROM `Order` WHERE OrderID = '" + order.getOrderId() + "'"; 
        ResultSet rs = st.executeQuery(query); 

        updateOrderPs.setInt(1, rs.getInt("UserId"));
        updateOrderPs.setInt(2, rs.getInt("ProductListId"));
        updateOrderPs.setInt(3, rs.getInt("PaymentId"));
        updateOrderPs.setTimestamp(4, new Timestamp(order.getDatePlaced().getTime()));
        updateOrderPs.setInt(5, rs.getInt("OrderId"));

        updateOrderPs.executeUpdate();
    } 

    //delete an order from the database   
    public void deleteOrder(int OrderId) throws SQLException{       
        st.executeUpdate("DELETE FROM `Order` WHERE OrderId = '" + OrderId + "'"); 
    }
}