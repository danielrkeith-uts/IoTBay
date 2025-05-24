package model.dao;

import model.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import model.Enums.*;

public class OrderDBManager {
    private Statement st1;
    private Statement st2;
    private Connection conn;
    private static final String UPDATE_ORDER_STMT = "UPDATE `Order` SET UserId = ?, CartId = ?, PaymentId = ?, DatePlaced = ?, OrderStatus = ? WHERE OrderId = ?;";
    private final PreparedStatement updateOrderPs;
        
    public OrderDBManager(Connection conn) throws SQLException {    
        this.conn = conn;
        this.st1 = conn.createStatement(); 
        this.st2 = conn.createStatement(); 
        this.updateOrderPs = conn.prepareStatement(UPDATE_ORDER_STMT);  
    }

    //Find an order by OrderId in the database   
    public Order getOrder(int inputOrderId) throws SQLException {       
        //get the CartId, PaymentId, and DatePlaced from the Order table
        String query = "SELECT * FROM `Order` WHERE OrderID = '" + inputOrderId + "'"; 
        ResultSet rs = st1.executeQuery(query); 

        if (rs.next()) {
            int orderId = rs.getInt("OrderID");
            int CartId = rs.getInt("CartId");
            int PaymentId = rs.getInt("PaymentId");
            Timestamp timestamp = rs.getTimestamp("DatePlaced");

            String statusString = rs.getString("OrderStatus");
            OrderStatus status = OrderStatus.valueOf(statusString);
            
            //Step 1: Get all entries from ProductListEntry table with this CartId
            String entryQuery = "SELECT * FROM ProductListEntry WHERE CartId = '" + CartId + "'"; 
            ResultSet entryRs = st2.executeQuery(entryQuery);

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

            Order order = new Order(orderId, ProductList, Payment, timestamp, status);
            return order;
        } 
        return null;
    }

    //Add an order into the database   
    public void addOrder(int OrderId, int UserId, int CartId, int PaymentId, java.sql.Timestamp DatePlaced, String status) throws SQLException {       
        String query = "INSERT INTO `Order` (OrderId, UserId, CartId, PaymentId, DatePlaced, OrderStatus) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, OrderId);
            pst.setInt(2, UserId);
            pst.setInt(3, CartId);
            pst.setInt(4, PaymentId);
            pst.setTimestamp(5, DatePlaced);
            pst.setString(6, status);

            pst.executeUpdate();
        }
    }

    //update an order's details in the database   
    public void updateOrder(Order order, int cartId) throws SQLException {
        int userId = order.getPayment().getUserId();
        int paymentId = order.getPayment().getPaymentId();
    
        updateOrderPs.setInt(1, userId);
        updateOrderPs.setInt(2, cartId);
        updateOrderPs.setInt(3, paymentId);
        updateOrderPs.setTimestamp(4, order.getDatePlaced());
        updateOrderPs.setString(5, order.getOrderStatus().name());
        updateOrderPs.setInt(6, order.getOrderId());
        updateOrderPs.executeUpdate();
    
        try (PreparedStatement deleteProductsPs = conn.prepareStatement("DELETE FROM ProductListEntry WHERE CartId = ?")) {
            deleteProductsPs.setInt(1, cartId);
            deleteProductsPs.executeUpdate();
        }
    
        String insertProductSql = "INSERT INTO ProductListEntry (CartId, ProductId, Quantity) VALUES (?, ?, ?)";
        try (PreparedStatement insertProductPs = conn.prepareStatement(insertProductSql)) {
            for (ProductListEntry ple : order.getProductList()) {
                insertProductPs.setInt(1, cartId);
                insertProductPs.setInt(2, ple.getProduct().getProductId());
                insertProductPs.setInt(3, ple.getQuantity());
                insertProductPs.addBatch();
            }
            insertProductPs.executeBatch();
        }
    }

    public void cancelOrder(int orderId) throws SQLException {
        String sql = "UPDATE `Order` SET OrderStatus = 'CANCELLED' WHERE OrderId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        }
    }
}