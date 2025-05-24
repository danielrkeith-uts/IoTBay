package model.dao;

import model.*;
import model.Enums.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeliveryDBManager {
    private static final String GET_DELIVERY_STMT = "SELECT * FROM Delivery WHERE DeliveryId = ?";
    private static final String GET_ADDRESS_STMT = "SELECT * FROM Address WHERE AddressId = ?";
    private static final String ADD_DELIVERY_STMT = "INSERT INTO Delivery (DeliveryId, OrderId, SourceAddressId, DestinationAddressId, Courier, CourierDeliveryId) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_DELIVERY_STMT = "UPDATE Delivery SET SourceAddressId = ?, DestinationAddressId = ?, Courier = ?, CourierDeliveryId = ? WHERE DeliveryId = ?";
    private static final String DELETE_DELIVERY_STMT = "DELETE FROM Delivery WHERE DeliveryId = ?";

    private final Connection conn;
    private final PreparedStatement getDeliveryPs;
    private final PreparedStatement getAddressPs;
    private final PreparedStatement addDeliveryPs;
    private final PreparedStatement updateDeliveryPs;
    private final PreparedStatement deleteDeliveryPs;

    public DeliveryDBManager(Connection conn) throws SQLException {
        this.conn = conn;
        this.getDeliveryPs = conn.prepareStatement(GET_DELIVERY_STMT);
        this.getAddressPs = conn.prepareStatement(GET_ADDRESS_STMT);
        this.addDeliveryPs = conn.prepareStatement(ADD_DELIVERY_STMT);
        this.updateDeliveryPs = conn.prepareStatement(UPDATE_DELIVERY_STMT);
        this.deleteDeliveryPs = conn.prepareStatement(DELETE_DELIVERY_STMT);
    }

    public Delivery getDelivery(int deliveryId) throws SQLException {
        getDeliveryPs.setInt(1, deliveryId);
        try (ResultSet rs = getDeliveryPs.executeQuery()) {
            if (rs.next()) {
                int orderId = rs.getInt("OrderId");
                int sourceAddressId = rs.getInt("SourceAddressId");
                int destinationAddressId = rs.getInt("DestinationAddressId");
                String courier = rs.getString("Courier");
                int courierDeliveryId = rs.getInt("CourierDeliveryId");

                // Get source address
                Address source = getAddress(sourceAddressId);
                
                // Get destination address
                Address destination = getAddress(destinationAddressId);

                // Get order details
                OrderDBManager orderDBManager = new OrderDBManager(conn);
                List<Order> orders = new ArrayList<>();
                Order order = orderDBManager.getOrder(orderId);
                if (order != null) {
                    orders.add(order);
                }

                return new Delivery(deliveryId, orders, source, destination, courier, courierDeliveryId);
            }
        }
        return null;
    }

    private Address getAddress(int addressId) throws SQLException {
        getAddressPs.setInt(1, addressId);
        try (ResultSet rs = getAddressPs.executeQuery()) {
            if (rs.next()) {
                int streetNumber = Integer.parseInt(rs.getString("StreetNumber"));
                String street = rs.getString("Street");
                String suburb = rs.getString("Suburb");
                int stateIndex = rs.getInt("State");
                AuState state = AuState.values()[stateIndex];
                String postcode = String.valueOf(rs.getInt("Postcode"));
                return new Address(addressId, streetNumber, street, suburb, state, postcode);
            }
        }
        return null;
    }

    public void addDelivery(int deliveryId, int orderId, Address source, Address destination, String courier, int courierDeliveryId) throws SQLException {
        addDeliveryPs.setInt(1, deliveryId);
        addDeliveryPs.setInt(2, orderId);
        addDeliveryPs.setInt(3, source.getAddressId());
        addDeliveryPs.setInt(4, destination.getAddressId());
        addDeliveryPs.setString(5, courier);
        addDeliveryPs.setInt(6, courierDeliveryId);
        addDeliveryPs.executeUpdate();
    }

    public void updateDelivery(int deliveryId, Address source, Address destination, String courier, int courierDeliveryId) throws SQLException {
        updateDeliveryPs.setInt(1, source.getAddressId());
        updateDeliveryPs.setInt(2, destination.getAddressId());
        updateDeliveryPs.setString(3, courier);
        updateDeliveryPs.setInt(4, courierDeliveryId);
        updateDeliveryPs.setInt(5, deliveryId);
        updateDeliveryPs.executeUpdate();
    }

    public void deleteDelivery(int deliveryId) throws SQLException {
        deleteDeliveryPs.setInt(1, deliveryId);
        deleteDeliveryPs.executeUpdate();
    }
}