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

    // CREATE: Add a new delivery record
    public int addDelivery(Address source, Address destination, String courier, int courierDeliveryId) throws SQLException {
        // Add source address and get ID
        int sourceId = addAddress(source);
        
        // Add destination address and get ID
        int destId = addAddress(destination);
        
        // Insert the delivery record
        String query = "INSERT INTO Delivery (SourceAddressId, DestinationAddressId, Courier, CourierDeliveryId) " +
                       "VALUES (" + sourceId + ", " + destId + ", '" + courier + "', " + courierDeliveryId + ")";
        
        st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        
        // Get the generated ID
        ResultSet rs = st.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    // Helper method to add an address
    private int addAddress(Address address) throws SQLException {
        String query = "INSERT INTO Address (StreetNumber, Street, Suburb, State, Postcode) " +
                       "VALUES ('" + address.getStreetNumber() + "', '" + 
                       address.getStreet() + "', '" + 
                       address.getSuburb() + "', '" + 
                       address.getState().ordinal() + "', '" + 
                       address.getPostcode() + "')";
        
        st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
        
        ResultSet rs = st.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    // READ: Get a single delivery by ID
    public Delivery getDelivery(int deliveryId) throws SQLException {
        String query = "SELECT * FROM Delivery WHERE DeliveryId = " + deliveryId; 
        ResultSet rs = st.executeQuery(query); 

        if (rs.next()) {
            int sourceAddressId = rs.getInt("SourceAddressId");
            int destAddressId = rs.getInt("DestinationAddressId");
            String courier = rs.getString("Courier");
            int courierDeliveryId = rs.getInt("CourierDeliveryId");

            // Get the addresses
            Address source = getAddress(sourceAddressId);
            Address destination = getAddress(destAddressId);
            
            // Create an empty list for orders
            List<Order> emptyOrders = new ArrayList<>();
            
            // Create and return the delivery object
            return new Delivery(deliveryId, emptyOrders, source, destination, courier, courierDeliveryId);
        }
        return null;
    }

    // READ: Get all deliveries
    public List<Delivery> getAllDeliveries() throws SQLException {
        List<Delivery> deliveries = new ArrayList<>();
        
        String query = "SELECT DeliveryId FROM Delivery";
        ResultSet rs = st.executeQuery(query);
        
        while (rs.next()) {
            int deliveryId = rs.getInt("DeliveryId");
            Delivery delivery = getDelivery(deliveryId);
            if (delivery != null) {
                deliveries.add(delivery);
            }
        }
        
        return deliveries;
    }

    // Helper method to get an address
    private Address getAddress(int addressId) throws SQLException {
        String query = "SELECT * FROM Address WHERE AddressId = " + addressId; 
        ResultSet rs = st.executeQuery(query);
        
        if (rs.next()) {
            int streetNumber = rs.getInt("StreetNumber");
            String street = rs.getString("Street");
            String suburb = rs.getString("Suburb");
            int stateIndex = rs.getInt("State");
            AuState state = AuState.values()[stateIndex];
            String postcode = String.valueOf(rs.getInt("Postcode"));
            
            return new Address(addressId, streetNumber, street, suburb, state, postcode);
        }
        return null;
    }

    // UPDATE: Update a delivery
    public void updateDelivery(int deliveryId, Address source, Address destination, String courier, int courierDeliveryId) throws SQLException {
        // Get the current delivery to find address IDs
        String query = "SELECT * FROM Delivery WHERE DeliveryId = " + deliveryId;
        ResultSet rs = st.executeQuery(query);
        
        if (rs.next()) {
            int sourceAddressId = rs.getInt("SourceAddressId");
            int destAddressId = rs.getInt("DestinationAddressId");
            
            // Update the addresses
            updateAddress(sourceAddressId, source);
            updateAddress(destAddressId, destination);
            
            // Update the delivery record
            String updateQuery = "UPDATE Delivery SET Courier = '" + courier + 
                                "', CourierDeliveryId = " + courierDeliveryId + 
                                " WHERE DeliveryId = " + deliveryId;
            st.executeUpdate(updateQuery);
        }
    }

    // Helper method to update an address
    private void updateAddress(int addressId, Address address) throws SQLException {
        String query = "UPDATE Address SET " +
                      "StreetNumber = " + address.getStreetNumber() + ", " +
                      "Street = '" + address.getStreet() + "', " +
                      "Suburb = '" + address.getSuburb() + "', " +
                      "State = " + address.getState().ordinal() + ", " +
                      "Postcode = '" + address.getPostcode() + "' " +
                      "WHERE AddressId = " + addressId;
        st.executeUpdate(query);
    }

    // DELETE: Delete a delivery
    public void deleteDelivery(int deliveryId) throws SQLException {
        st.executeUpdate("DELETE FROM Delivery WHERE DeliveryId = " + deliveryId);
    }
}