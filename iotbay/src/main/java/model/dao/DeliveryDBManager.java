package model.dao;

import model.*;
import model.Enums.AuState;
import model.Enums.DeliveryStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDBManager {

    private Connection conn;

    public DeliveryDBManager(Connection conn) throws SQLException {
        this.conn = conn;
    }

    public Delivery getDelivery(int deliveryId) throws SQLException {
        System.out.println("Looking for Delivery ID: " + deliveryId);
        String query = "SELECT * FROM Delivery WHERE DeliveryId = ?"; 
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, deliveryId);
        ResultSet rs = ps.executeQuery(); 

        if (rs.next()) {
            int sourceAddressId = rs.getInt("SourceAddressId");
            int destinationAddressId = rs.getInt("DestinationAddressId");
            String courier = rs.getString("Courier");
            int courierDeliveryId = rs.getInt("CourierDeliveryId");
            int statusIndex = rs.getInt("Status");
            DeliveryStatus status = DeliveryStatus.values()[statusIndex];
            Date estimatedDeliveryDate = rs.getDate("EstimatedDeliveryDate");

            // Create a source address
            String sourceAddQuery = "SELECT * FROM Address WHERE AddressId = ?"; 
            PreparedStatement sourcePs = conn.prepareStatement(sourceAddQuery);
            sourcePs.setInt(1, sourceAddressId);
            ResultSet sourceAddRs = sourcePs.executeQuery(); 
            
            if (sourceAddRs.next()) {
                int sourStreetNumber = sourceAddRs.getInt("StreetNumber");
                String sourStreet = sourceAddRs.getString("Street");
                String sourSuburb = sourceAddRs.getString("Suburb");
                int sourStateIndex = sourceAddRs.getInt("State");
                AuState sourState = AuState.values()[sourStateIndex];
                String sourPostcode = String.valueOf(sourceAddRs.getInt("Postcode"));
                Address source = new Address(sourceAddressId, sourStreetNumber, sourStreet, sourSuburb, sourState, sourPostcode);

                // Create a delivery address
                String destinationAddQuery = "SELECT * FROM Address WHERE AddressId = ?"; 
                PreparedStatement destPs = conn.prepareStatement(destinationAddQuery);
                destPs.setInt(1, destinationAddressId);
                ResultSet destinationAddRs = destPs.executeQuery(); 
                
                if (destinationAddRs.next()) {
                    int destStreetNumber = destinationAddRs.getInt("StreetNumber");
                    String destStreet = destinationAddRs.getString("Street");
                    String destSuburb = destinationAddRs.getString("Suburb");
                    int destStateIndex = destinationAddRs.getInt("State");
                    AuState destState = AuState.values()[destStateIndex];
                    String destPostcode = String.valueOf(destinationAddRs.getInt("Postcode"));
                    Address destination = new Address(destinationAddressId, destStreetNumber, destStreet, destSuburb, destState, destPostcode);

                    // Create an order list 
                    String orderQuery = "SELECT * FROM `Order` WHERE DeliveryId = ?"; 
                    PreparedStatement orderPs = conn.prepareStatement(orderQuery);
                    orderPs.setInt(1, deliveryId);
                    ResultSet orderRs = orderPs.executeQuery(); 

                    List<Order> orders = new ArrayList<>();
                    
                    while (orderRs.next()) {
                        int orderId = orderRs.getInt("OrderId");
                        int productListId = orderRs.getInt("ProductListId");
                        int paymentId = orderRs.getInt("PaymentId");
                        Date datePlaced = orderRs.getDate("DatePlaced");

                        ProductListEntryDBManager productListEntryDBManager = new ProductListEntryDBManager(conn);
                        List<ProductListEntry> productList = productListEntryDBManager.getProductList(productListId);

                        PaymentDBManager paymentDBManager = new PaymentDBManager(conn);
                        Payment payment = paymentDBManager.getPayment(paymentId);

                        Order order = new Order(productList, payment, datePlaced);
                        orders.add(order); 
                    }
                    
                    Delivery delivery = new Delivery(deliveryId, orders, source, destination, courier, courierDeliveryId);
                    delivery.setEstimatedDeliveryDate(estimatedDeliveryDate);
                    delivery.setStatus(status);
                    
                    for (Order o : orders) {
                        o.setDelivery(delivery);
                    }
                    return delivery;
                }
            }
        }
        return null;
    }

    // Add a delivery into the database   
    public int addDelivery(Address source, Address destination, String courier, int courierDeliveryId, DeliveryStatus status, Date estimatedDeliveryDate) throws SQLException {       
        // First add addresses if they don't exist
        int sourceAddressId = addOrGetAddress(source);
        int destinationAddressId = addOrGetAddress(destination);
        
        // Now add delivery with address IDs
        String query = "INSERT INTO Delivery (SourceAddressId, DestinationAddressId, Courier, CourierDeliveryId, Status, EstimatedDeliveryDate) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
        
        PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, sourceAddressId);
        ps.setInt(2, destinationAddressId);
        ps.setString(3, courier);
        ps.setInt(4, courierDeliveryId);
        ps.setInt(5, status.ordinal());
        ps.setDate(6, new java.sql.Date(estimatedDeliveryDate.getTime()));
        
        ps.executeUpdate();
        
        // Get the generated delivery ID
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        
        return -1; // Return -1 if no ID was generated
    }

    // Helper method to add an address or get its ID if it already exists
    private int addOrGetAddress(Address address) throws SQLException {
        // Check if address already exists
        String query = "SELECT AddressId FROM Address " +
                      "WHERE StreetNumber = ? AND Street = ? AND Suburb = ? AND State = ? AND Postcode = ?";
        
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, address.getStreetNumber());
        ps.setString(2, address.getStreet());
        ps.setString(3, address.getSuburb());
        ps.setInt(4, address.getState().ordinal());
        ps.setString(5, address.getPostcode());
        
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            return rs.getInt("AddressId");
        } else {
            // Address doesn't exist, so add it
            String insertQuery = "INSERT INTO Address (StreetNumber, Street, Suburb, State, Postcode) " +
                               "VALUES (?, ?, ?, ?, ?)";
            
            PreparedStatement insertPs = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertPs.setInt(1, address.getStreetNumber());
            insertPs.setString(2, address.getStreet());
            insertPs.setString(3, address.getSuburb());
            insertPs.setInt(4, address.getState().ordinal());
            insertPs.setString(5, address.getPostcode());
            
            insertPs.executeUpdate();
            
            ResultSet insertRs = insertPs.getGeneratedKeys();
            if (insertRs.next()) {
                return insertRs.getInt(1);
            }
            return -1;
        }
    }

    // Update a delivery's details in the database   
    public void updateDelivery(int deliveryId, Address source, Address destination, 
                              String courier, int courierDeliveryId, DeliveryStatus status, 
                              Date estimatedDeliveryDate) throws SQLException {   
        // First update or add addresses
        int sourceAddressId = addOrGetAddress(source);
        int destinationAddressId = addOrGetAddress(destination);
        
        // Now update the delivery
        String query = "UPDATE Delivery SET SourceAddressId = ?, DestinationAddressId = ?, " +
                      "Courier = ?, CourierDeliveryId = ?, Status = ?, EstimatedDeliveryDate = ? " +
                      "WHERE DeliveryId = ?";
        
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, sourceAddressId);
        ps.setInt(2, destinationAddressId);
        ps.setString(3, courier);
        ps.setInt(4, courierDeliveryId);
        ps.setInt(5, status.ordinal());
        ps.setDate(6, new java.sql.Date(estimatedDeliveryDate.getTime()));
        ps.setInt(7, deliveryId);
        
        ps.executeUpdate();
    }       

    // Delete a delivery from the database   
    public void deleteDelivery(int deliveryId) throws SQLException {       
        String query = "DELETE FROM Delivery WHERE DeliveryId = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, deliveryId);
        ps.executeUpdate();
    }
    
    // Get all deliveries for a specific user
    public List<Delivery> getUserDeliveries(int userId) throws SQLException {
        List<Delivery> deliveries = new ArrayList<>();
        
        String query = "SELECT d.DeliveryId FROM Delivery d " +
                      "JOIN `Order` o ON d.DeliveryId = o.DeliveryId " +
                      "WHERE o.UserId = ? " +
                      "GROUP BY d.DeliveryId";
        
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            int deliveryId = rs.getInt("DeliveryId");
            Delivery delivery = getDelivery(deliveryId);
            if (delivery != null) {
                deliveries.add(delivery);
            }
        }
        
        return deliveries;
    }
    
    // Search deliveries by date range
    public List<Delivery> searchDeliveriesByDate(int userId, Date startDate, Date endDate) throws SQLException {
        List<Delivery> deliveries = new ArrayList<>();
        
        String query = "SELECT DISTINCT d.DeliveryId FROM Delivery d " +
                      "JOIN `Order` o ON d.DeliveryId = o.DeliveryId " +
                      "WHERE o.UserId = ? AND " +
                      "d.EstimatedDeliveryDate BETWEEN ? AND ?";
        
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userId);
        ps.setDate(2, new java.sql.Date(startDate.getTime()));
        ps.setDate(3, new java.sql.Date(endDate.getTime()));
        
        ResultSet rs = ps.executeQuery();
        
        while (rs.next()) {
            int deliveryId = rs.getInt("DeliveryId");
            Delivery delivery = getDelivery(deliveryId);
            if (delivery != null) {
                deliveries.add(delivery);
            }
        }
        
        return deliveries;
    }
    
    // Search delivery by ID
    public Delivery searchDeliveryById(int userId, int deliveryId) throws SQLException {
        String query = "SELECT DISTINCT d.DeliveryId FROM Delivery d " +
                      "JOIN `Order` o ON d.DeliveryId = o.DeliveryId " +
                      "WHERE o.UserId = ? AND d.DeliveryId = ?";
        
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userId);
        ps.setInt(2, deliveryId);
        
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            return getDelivery(deliveryId);
        }
        
        return null;
    }
}