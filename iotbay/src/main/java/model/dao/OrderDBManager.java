package model.dao;

import model.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
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

    public Order getOrder(int orderId) throws SQLException {       
        String query = "SELECT * FROM `Order` WHERE OrderID = '" + orderId + "'"; 
        ResultSet rs = st1.executeQuery(query); 

        if (rs.next()) {
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
            order.setCartId(CartId);
            return order;
        } 
        return null;
    }

    public List<Order> getAllCustomerOrders(int userID) throws SQLException {
        List<Order> orders = new ArrayList<Order>();       
        String query = "SELECT * FROM `Order` WHERE UserID = '" + userID + "'"; 
        ResultSet rs = st1.executeQuery(query); 

        while (rs.next()) {
            int orderId = rs.getInt("OrderID");
            int CartId = rs.getInt("CartId");
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

            int paymentId = rs.getInt("PaymentId");
            Payment payment = null;
            if (!rs.wasNull()) {
                PaymentDBManager paymentDBManager = new PaymentDBManager(conn);
                payment = paymentDBManager.getPayment(paymentId);
            }

            Order order = new Order(orderId, ProductList, payment, timestamp, status);
            order.setCartId(CartId);
            order.setUserId(rs.getInt("UserId"));
            order.setPaymentId(paymentId);
            orders.add(order);
        } 
        return orders;
    }

    //Add an order into the database   
    public int addOrder(int UserId, int CartId, int PaymentId, java.sql.Timestamp DatePlaced, String status) throws SQLException {       
        String query = "INSERT INTO `Order` (UserId, CartId, PaymentId, DatePlaced, OrderStatus) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, UserId);
            pst.setInt(2, CartId);
            pst.setInt(3, PaymentId);

            // Format DatePlaced to string in yyyy-MM-dd HH:mm:ss format
            String formatted = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DatePlaced);
            pst.setString(4, formatted);
            pst.setString(5, status);

            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return generated OrderId
                } else {
                    throw new SQLException("Order insertion failed, no ID obtained.");
                }
            }
        }
    }

    public int addOrderAsSavedCart(int UserId, int CartId, java.sql.Timestamp DatePlaced) throws SQLException {       
        String query = "INSERT INTO `Order` (UserId, CartId, DatePlaced, OrderStatus) VALUES (?, ?, ?, ?)";
        String status = "SAVED";

        try (PreparedStatement pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, UserId);
            pst.setInt(2, CartId);
            pst.setTimestamp(3, DatePlaced);
            pst.setString(4, status);

            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return generated OrderId
                } else {
                    throw new SQLException("Order insertion failed, no ID obtained.");
                }
            }
        }
    }

    //update an order's details in the database   
    public void updateOrder(Order order) throws SQLException {
        // Step 1: Fetch the order to verify it exists
        String orderQuery = "SELECT OrderId FROM `Order` WHERE OrderId = ?";
        try (PreparedStatement orderCheckPs = conn.prepareStatement(orderQuery)) {
            orderCheckPs.setInt(1, order.getOrderId());
            try (ResultSet rsOrder = orderCheckPs.executeQuery()) {
                if (!rsOrder.next()) {
                    throw new SQLException("Order not found: " + order.getOrderId());
                }
            }
        }

        // Step 2: Validate payment ID (if any)
        Integer paymentId = (order.getPayment() != null) ? order.getPayment().getPaymentId() : null;
        if (paymentId != null) {
            try (PreparedStatement paymentCheckPs = conn.prepareStatement("SELECT 1 FROM Payment WHERE PaymentId = ?")) {
                paymentCheckPs.setInt(1, paymentId);
                try (ResultSet rsPayment = paymentCheckPs.executeQuery()) {
                    if (!rsPayment.next()) {
                        System.out.println("WARNING: PaymentId " + paymentId + " does not exist. Setting NULL.");
                        paymentId = null; // override if invalid
                    }
                }
            }
        }

        // Step 3: Prepare and execute update
        String updateQuery = "UPDATE `Order` SET UserId = ?, CartId = ?, PaymentId = ?, DatePlaced = ?, OrderStatus = ? WHERE OrderId = ?";
        try (PreparedStatement updateOrderPs = conn.prepareStatement(updateQuery)) {
            updateOrderPs.setInt(1, order.getUserId());
            updateOrderPs.setInt(2, order.getCartId());

            if (paymentId != null) {
                updateOrderPs.setInt(3, paymentId);
            } else {
                updateOrderPs.setNull(3, java.sql.Types.INTEGER);
            }

            String formattedDate = order.getDatePlaced().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            updateOrderPs.setString(4, formattedDate);
            updateOrderPs.setString(5, order.getOrderStatus().name());
            updateOrderPs.setInt(6, order.getOrderId());

            System.out.println("DEBUG: Order update values:");
            System.out.println("  UserId: " + order.getUserId());
            System.out.println("  CartId: " + order.getCartId());
            System.out.println("  PaymentId: " + (paymentId != null ? paymentId : "null"));
            System.out.println("  OrderId: " + order.getOrderId());

            updateOrderPs.executeUpdate();
        }
    }

    //orders can't be deleted, only set to cancelled  
    public void cancelOrder(int OrderId) throws SQLException{       
        st1.executeUpdate("UPDATE `Order` SET OrderStatus = 'CANCELLED' WHERE OrderId = " + OrderId); 
    }
}