package model.dao;

import model.*;
import model.Enums.AuState;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDBManager {
    private Statement st;
    
    public DeliveryDBManager(Connection conn) throws SQLException {
        st = conn.createStatement();
    }

    public Delivery getDelivery(int DeliveryId) throws SQLException {
        String query = "SELECT * FROM Delivery WHERE DeliveryId = '" + DeliveryId + "'"; 
        ResultSet rs = st.executeQuery(query); 

        if (rs.next()) {
            int SourceAddressId = rs.getInt("SourceAddressId");
            int DestinationAddressId = rs.getInt("DestinationAddressId");
            String Courier = rs.getString("Courier");
            int CourierDeliveryId = rs.getInt("CourierDeliveryId");

            // Create addresses
            Address source = getAddress(SourceAddressId);
            Address destination = getAddress(DestinationAddressId);
            
            if (source == null || destination == null) return null;
            
            // Create simple delivery without loading orders to avoid circular reference
            List<Order> emptyOrders = new ArrayList<>();
            return new Delivery(DeliveryId, emptyOrders, source, destination, Courier, CourierDeliveryId);
        }
        return null;
    }
    
    // Get all deliveries from the database
    public List<Delivery> getAllDeliveries() throws SQLException {
        String query = "SELECT DeliveryId FROM Delivery";
        ResultSet rs = st.executeQuery(query);
        
        List<Delivery> deliveries = new ArrayList<>();
        while (rs.next()) {
            int deliveryId = rs.getInt("DeliveryId");
            Delivery delivery = getDelivery(deliveryId);
            if (delivery != null) {
                deliveries.add(delivery);
            }
        }
        
        return deliveries;
    }
    
    private Address getAddress(int AddressId) throws SQLException {
        String query = "SELECT * FROM Address WHERE AddressId = '" + AddressId + "'"; 
        ResultSet rs = st.executeQuery(query);
        
        if (rs.next()) {
            int streetNumber = Integer.parseInt(rs.getString("StreetNumber"));
            String street = rs.getString("Street");
            String suburb = rs.getString("Suburb");
            int stateIndex = rs.getInt("State");
            AuState state = AuState.values()[stateIndex];
            String postcode = String.valueOf(rs.getInt("Postcode"));
            return new Address(AddressId, streetNumber, street, suburb, state, postcode);
        }
        return null;
    }

    // Add a delivery into the database   
    public int addDelivery(Address source, Address destination, String courier, int courierDeliveryId) throws SQLException {
        // First, add the addresses
        int sourceAddressId = addAddress(source);
        int destinationAddressId = addAddress(destination);
        
        // Then create the delivery
        String query = "INSERT INTO Delivery (SourceAddressId, DestinationAddressId, Courier, CourierDeliveryId) " +
                      "VALUES ('" + sourceAddressId + "', '" + destinationAddressId + "', '" + courier + "', '" + courierDeliveryId + "')";
        st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        
        // Get the generated delivery ID
        ResultSet rs = st.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        
        return -1; // Error
    }
    
    private int addAddress(Address address) throws SQLException {
        String query = "INSERT INTO Address (StreetNumber, Street, Suburb, State, Postcode) " +
                      "VALUES ('" + address.getStreetNumber() + "', '" + address.getStreet() + "', '" + 
                      address.getSuburb() + "', '" + address.getState().ordinal() + "', '" + address.getPostcode() + "')";
        st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        
        // Get the generated address ID
        ResultSet rs = st.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        
        return -1; // Error
    }

    // Update a delivery's details in the database   
    public void updateDelivery(int DeliveryId, Address source, Address destination, String courier, int courierDeliveryId) throws SQLException {
        // Get the current delivery
        String query = "SELECT * FROM Delivery WHERE DeliveryId = '" + DeliveryId + "'";
        ResultSet rs = st.executeQuery(query);
        
        if (rs.next()) {
            int sourceAddressId = rs.getInt("SourceAddressId");
            int destinationAddressId = rs.getInt("DestinationAddressId");
            
            // Update the addresses
            updateAddress(sourceAddressId, source);
            updateAddress(destinationAddressId, destination);
            
            // Update the delivery
            String updateQuery = "UPDATE Delivery SET Courier = '" + courier + 
                                "', CourierDeliveryId = '" + courierDeliveryId + 
                                "' WHERE DeliveryId = '" + DeliveryId + "'";
            st.executeUpdate(updateQuery);
        }
    }
    
    private void updateAddress(int addressId, Address address) throws SQLException {
        String query = "UPDATE Address SET StreetNumber = '" + address.getStreetNumber() + 
                      "', Street = '" + address.getStreet() + 
                      "', Suburb = '" + address.getSuburb() + 
                      "', State = '" + address.getState().ordinal() + 
                      "', Postcode = '" + address.getPostcode() + 
                      "' WHERE AddressId = '" + addressId + "'";
        st.executeUpdate(query);
    }

    // Delete a delivery from the database   
    public void deleteDelivery(int DeliveryId) throws SQLException {
        st.executeUpdate("DELETE FROM Delivery WHERE DeliveryId = '" + DeliveryId + "'");
    }
}