package model.dao;

import model.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import model.Enums.*;
import java.time.format.DateTimeParseException;

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

            String dateString = rs.getString("DatePlaced");
            LocalDateTime ldt = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
            Timestamp timestamp = Timestamp.valueOf(ldt);

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

    public Order getOrderByDate(String DatePlaced) throws SQLException { 
        if (!DatePlaced.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new DateTimeParseException("Invalid date format", DatePlaced, 0);
        }
        try {
            LocalDate date = LocalDate.parse(DatePlaced);  // e.g., "2025-04-25"
            LocalDateTime startOfDay = date.atStartOfDay(); // 2025-04-25 00:00:00
            LocalDateTime endOfDay = date.atTime(23, 59, 59, 999000000); // 2025-04-25 23:59:59.999

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

            String start = startOfDay.format(formatter);
            String end = endOfDay.format(formatter);

            String query = "SELECT * FROM `Order` WHERE DatePlaced BETWEEN '" + start + "' AND '" + end + "'";
            ResultSet rs = st1.executeQuery(query);

            if (rs.next()) {
                int orderId = rs.getInt("OrderID");
                int CartId = rs.getInt("CartId");
                int PaymentId = rs.getInt("PaymentId");

                String dateString = rs.getString("DatePlaced");
                LocalDateTime ldt = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
                Timestamp timestamp = Timestamp.valueOf(ldt);

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
        } catch (DateTimeParseException e) {
            System.out.println("Invalid input. Expected: yyyy-MM-dd");
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
            
            Timestamp timestamp = new Timestamp(DatePlaced.getTime());
            String formatted = timestamp.toLocalDateTime().toString().replace("T", " ");
            pst.setString(5, formatted);
            pst.setString(6, status);

            pst.executeUpdate();
        }
    }

    //update an order's details in the database   
    public void updateOrder(Order order) throws SQLException {
        String query = "SELECT UserId, CartId, PaymentId, DatePlaced, OrderStatus, OrderId FROM `Order` WHERE OrderID = '" + order.getOrderId() + "'"; 
        ResultSet rs = st1.executeQuery(query); 

        updateOrderPs.setInt(1, rs.getInt("UserId"));
        updateOrderPs.setInt(2, rs.getInt("CartId"));
        updateOrderPs.setInt(3, rs.getInt("PaymentId"));

        String formattedDate = order.getDatePlaced().toLocalDateTime().toString().replace("T", " ");
        updateOrderPs.setString(4, formattedDate);

        updateOrderPs.setString(5, rs.getString("OrderStatus"));
        updateOrderPs.setInt(6, rs.getInt("OrderId"));

        updateOrderPs.executeUpdate();
    } 

    //orders can't be deleted, only set to cancelled  
    public void cancelOrder(int OrderId) throws SQLException{       
        st1.executeUpdate("UPDATE `Order` SET OrderStatus = 'CANCELLED' WHERE OrderId = " + OrderId); 
    }
}