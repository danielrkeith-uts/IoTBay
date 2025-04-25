package model.dao;

import model.*;
import model.Enums.AuState;
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
            int SourceAddressId = rs.getInt("SourceAddressId");
            int DestinationAddressId = rs.getInt("DestinationAddressId");
            String Courier = rs.getString("Courier");
            String CourierDeliveryId = String.valueOf(rs.getInt("CourierDeliveryId"));

            // create a source address
            String sourceAddQuery = "SELECT * FROM Address WHERE AddressId = '" + SourceAddressId + "'"; 
            ResultSet sourceAddRs = st.executeQuery(sourceAddQuery); 
            
            if (sourceAddRs.next()) {
                int sourStreetNumber = Integer.parseInt(rs.getString("StreetNumber"));
                String sourStreet = rs.getString("Street");
                String sourSuburb = rs.getString("Suburb");
                int sourStateIndex = rs.getInt("State");
                AuState sourState = AuState.values()[sourStateIndex];
                String sourPostcode = String.valueOf(rs.getInt("Postcode"));
                Address source = new Address(sourStreetNumber, sourStreet, sourSuburb, sourState, sourPostcode);

                // create a delivery address
                String destinationAddQuery = "SELECT * FROM Address WHERE AddressId = '" + DestinationAddressId + "'"; 
                ResultSet destinationAddRs = st.executeQuery(destinationAddQuery); 
                
                if (destinationAddRs.next()) {
                    int destStreetNumber = Integer.parseInt(rs.getString("StreetNumber"));
                    String destStreet = rs.getString("Street");
                    String destSuburb = rs.getString("Suburb");
                    int destStateIndex = rs.getInt("State");
                    AuState destState = AuState.values()[destStateIndex];
                    String destPostcode = String.valueOf(rs.getInt("Postcode"));
                    Address destination = new Address(destStreetNumber, destStreet, destSuburb, destState, destPostcode);

                    // create an order list 
                    String orderQuery = "SELECT * FROM Order WHERE DeliveryId = '" + DeliveryId + "'"; 
                    ResultSet orderRs = st.executeQuery(orderQuery); 
                    
                    while (orderRs.next()) {
                        List<Order> orders = new ArrayList<>();
                        int UserId = rs.getInt("UserId");
                        int ProductListId = rs.getInt("ProductListId");
                        int PaymentId = rs.getInt("PaymentId");
                        Date DatePlaced = rs.getDate("DatePlaced");

                        ProductListEntryDBManager productListEntryDBManager = new ProductListEntryDBManager(conn);
                        List<ProductListEntry> productList = productListEntryDBManager.getProductList(ProductListId);

                        PaymentDBManager paymentDBManager = new PaymentDBManager();
                        Payment payment = paymentDBManager.getPayment(PaymentId);

                        Order order = new Order(productList, payment);
                        orders.add(order); 

                        Delivery delivery = new Delivery(orders, source, destination, Courier, CourierDeliveryId);
                        for (Order o : orders) {
                            o.setDelivery(delivery);
                        }
                        return delivery;
                    }
                }
            }
        }
        return null;
    }
}