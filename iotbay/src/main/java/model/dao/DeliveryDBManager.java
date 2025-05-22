package model.dao;

import model.*;
import model.Enums.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDBManager {

    private Statement st;
    private Connection conn;

    public DeliveryDBManager(Connection conn) throws SQLException {
        this.conn = conn;
        st = conn.createStatement();
    }

    public Delivery getDelivery(int DeliveryId) throws SQLException {
        String query = "SELECT * FROM Delivery WHERE DeliveryId = '" + DeliveryId + "'"; 
        ResultSet rs = st.executeQuery(query); 

        if (rs.next()) {
            int OrderId = rs.getInt("OrderId");
            int SourceAddressId = rs.getInt("SourceAddressId");
            int DestinationAddressId = rs.getInt("DestinationAddressId");
            String Courier = rs.getString("Courier");
            int CourierDeliveryId = rs.getInt("CourierDeliveryId");

            // create a source address
            String sourceAddQuery = "SELECT * FROM Address WHERE AddressId = '" + SourceAddressId + "'"; 
            ResultSet sourceAddRs = st.executeQuery(sourceAddQuery); 
            
            if (sourceAddRs.next()) {
                int sourStreetNumber = Integer.parseInt(sourceAddRs.getString("StreetNumber"));
                String sourStreet = sourceAddRs.getString("Street");
                String sourSuburb = sourceAddRs.getString("Suburb");
                int sourStateIndex = sourceAddRs.getInt("State");
                AuState sourState = AuState.values()[sourStateIndex];
                String sourPostcode = String.valueOf(sourceAddRs.getInt("Postcode"));
                Address source = new Address(SourceAddressId, sourStreetNumber, sourStreet, sourSuburb, sourState, sourPostcode);

                // create a delivery address
                String destinationAddQuery = "SELECT * FROM Address WHERE AddressId = '" + DestinationAddressId + "'"; 
                ResultSet destinationAddRs = st.executeQuery(destinationAddQuery); 
                
                if (destinationAddRs.next()) {
                    int destStreetNumber = Integer.parseInt(destinationAddRs.getString("StreetNumber"));
                    String destStreet = destinationAddRs.getString("Street");
                    String destSuburb = destinationAddRs.getString("Suburb");
                    int destStateIndex = destinationAddRs.getInt("State");
                    AuState destState = AuState.values()[destStateIndex];
                    String destPostcode = String.valueOf(destinationAddRs.getInt("Postcode"));
                    Address destination = new Address(DestinationAddressId, destStreetNumber, destStreet, destSuburb, destState, destPostcode);

                    // create a product list via order
                    String orderQuery = "SELECT * FROM `Order` WHERE OrderId = '" + OrderId + "'"; 
                    ResultSet orderRs = st.executeQuery(orderQuery); 

                    List<Order> orders = new ArrayList<>();
                    
                    while (orderRs.next()) {
                        int CartId = orderRs.getInt("CartId");
                        int PaymentId = orderRs.getInt("PaymentId");
                        Date DatePlaced = orderRs.getDate("DatePlaced");
                        String statusString = orderRs.getString("OrderStatus");
                        OrderStatus status = OrderStatus.valueOf(statusString);

                        ProductListEntryDBManager productListEntryDBManager = new ProductListEntryDBManager(conn);
                        List<ProductListEntry> productList = productListEntryDBManager.getProductList(CartId);

                        PaymentDBManager paymentDBManager = new PaymentDBManager(conn);
                        Payment payment = paymentDBManager.getPayment(PaymentId);

                        Order order = new Order(OrderId, productList, payment, DatePlaced, status);
                        orders.add(order); 
                    }
                    Delivery delivery = new Delivery(DeliveryId, orders, source, destination, Courier, CourierDeliveryId);
                    return delivery;
                }
                return null;
            }
            return null;
        }
        return null;
    }

    //Add a delivery into the database   
    public void addDelivery(int DeliveryId, Address source, Address destination, String courier, int courierDeliveryId) throws SQLException {       
        st.executeUpdate("INSERT INTO Delivery VALUES ('" + source + "', '" + destination + "', '" + courier + "', " + courierDeliveryId + ")");   
    }

    //update a delivery's details in the database   
    public void updateDelivery(int DeliveryId, Address source, Address destination, String courier, int courierDeliveryId) throws SQLException {       
        st.executeUpdate("UPDATE Delivery SET SourceAddressId = '" + source + "', DestinationAddressId = '" + destination + "', Courier = '" + courier + "', CourierDeliveryId = '" + courierDeliveryId + "' WHERE DeliveryId = '" + DeliveryId);    
    }       

    //delete a delivery from the database   
    public void deleteDelivery(int DeliveryId) throws SQLException{       
        st.executeUpdate("DELETE FROM Delivery WHERE DeliveryId = '" + DeliveryId); 
    }
}