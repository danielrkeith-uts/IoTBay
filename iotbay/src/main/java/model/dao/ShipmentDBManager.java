package model.dao;

import model.*;
import model.Enums.AuState;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ShipmentDBManager {
    private static final String ADD_SHIPMENT_STMT = "INSERT INTO Shipment (OrderId, AddressId, Method, ShipmentDate) VALUES (?, ?, ?, ?);";
    private static final String GET_SHIPMENT_STMT = "SELECT * FROM Shipment WHERE ShipmentId = ?;";
    private static final String GET_SHIPMENTS_BY_USER_STMT = "SELECT s.* FROM Shipment s JOIN `Order` o ON s.OrderId = o.OrderId WHERE o.UserId = ?;";
    private static final String GET_SHIPMENTS_BY_ID_AND_DATE_STMT = "SELECT s.* FROM Shipment s JOIN `Order` o ON s.OrderId = o.OrderId WHERE o.UserId = ? AND (s.ShipmentId = ? OR DATE(s.ShipmentDate) = ?);";
    private static final String UPDATE_SHIPMENT_STMT = "UPDATE Shipment SET AddressId = ?, Method = ?, ShipmentDate = ? WHERE ShipmentId = ?;";
    private static final String DELETE_SHIPMENT_STMT = "DELETE FROM Shipment WHERE ShipmentId = ?;";

    private final Connection conn;
    private final PreparedStatement addShipmentPs;
    private final PreparedStatement getShipmentPs;
    private final PreparedStatement getShipmentsByUserPs;
    private final PreparedStatement getShipmentsByIdAndDatePs;
    private final PreparedStatement updateShipmentPs;
    private final PreparedStatement deleteShipmentPs;

    public ShipmentDBManager(Connection conn) throws SQLException {
        this.conn = conn;
        this.addShipmentPs = conn.prepareStatement(ADD_SHIPMENT_STMT, Statement.RETURN_GENERATED_KEYS);
        this.getShipmentPs = conn.prepareStatement(GET_SHIPMENT_STMT);
        this.getShipmentsByUserPs = conn.prepareStatement(GET_SHIPMENTS_BY_USER_STMT);
        this.getShipmentsByIdAndDatePs = conn.prepareStatement(GET_SHIPMENTS_BY_ID_AND_DATE_STMT);
        this.updateShipmentPs = conn.prepareStatement(UPDATE_SHIPMENT_STMT);
        this.deleteShipmentPs = conn.prepareStatement(DELETE_SHIPMENT_STMT);
    }

    public Shipment addShipment(Order order, Address address, String method, java.util.Date shipmentDate) throws SQLException {
        // STEP 1: Always insert the address first
        PreparedStatement insertAddressPs = conn.prepareStatement(
            "INSERT INTO Address (StreetNumber, Street, Suburb, State, Postcode) VALUES (?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        
        insertAddressPs.setString(1, String.valueOf(address.getStreetNumber()));
        insertAddressPs.setString(2, address.getStreet());
        insertAddressPs.setString(3, address.getSuburb());
        insertAddressPs.setInt(4, address.getState().ordinal());
        insertAddressPs.setString(5, address.getPostcode());
        
        insertAddressPs.executeUpdate();
        ResultSet addressRs = insertAddressPs.getGeneratedKeys();
        
        if (!addressRs.next()) {
            throw new SQLException("Failed to insert address");
        }
        
        int addressId = addressRs.getInt(1);
        
        // STEP 2: Now insert the shipment with the new address ID
        addShipmentPs.setInt(1, order.getOrderId());
        addShipmentPs.setInt(2, addressId);
        addShipmentPs.setString(3, method);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(shipmentDate);
        addShipmentPs.setString(4, formattedDate);
        
        addShipmentPs.executeUpdate();
        ResultSet shipmentRs = addShipmentPs.getGeneratedKeys();
        
        if (!shipmentRs.next()) {
            throw new SQLException("Failed to insert shipment");
        }
        
        int shipmentId = shipmentRs.getInt(1);
        
        // Create a new Address object with the generated ID
        Address savedAddress = new Address(addressId, address.getStreetNumber(), address.getStreet(), 
                                           address.getSuburb(), address.getState(), address.getPostcode());
        
        return new Shipment(shipmentId, order, savedAddress, method, shipmentDate);

    }

    public Shipment getShipment(int shipmentId) throws SQLException {
        getShipmentPs.setInt(1, shipmentId);
        ResultSet rs = getShipmentPs.executeQuery();

        if (rs.next()) {
            return toShipment(rs);
        }
        return null;
    }

    public List<Shipment> getShipmentsByUser(int userId) throws SQLException {
        getShipmentsByUserPs.setInt(1, userId);
        ResultSet rs = getShipmentsByUserPs.executeQuery();

        List<Shipment> shipments = new ArrayList<>();
        while (rs.next()) {
            shipments.add(toShipment(rs));
        }
        return shipments;
    }

    public List<Shipment> searchShipments(int userId, String searchQuery) throws SQLException {
        getShipmentsByIdAndDatePs.setInt(1, userId);
        
        // Try to parse shipment ID
        int shipmentId = 0;
        try {
            shipmentId = Integer.parseInt(searchQuery);
        } catch (NumberFormatException e) {
            shipmentId = -1;
        }
        
        getShipmentsByIdAndDatePs.setInt(2, shipmentId);
        
        // Try to parse date
        try {
            java.sql.Date date = java.sql.Date.valueOf(searchQuery);
            getShipmentsByIdAndDatePs.setDate(3, date);
        } catch (IllegalArgumentException e) {
            getShipmentsByIdAndDatePs.setDate(3, null);
        }
        
        ResultSet rs = getShipmentsByIdAndDatePs.executeQuery();
        
        List<Shipment> shipments = new ArrayList<>();
        while (rs.next()) {
            shipments.add(toShipment(rs));
        }
        return shipments;
    }

    public void updateShipment(Shipment shipment) throws SQLException {
        updateShipmentPs.setInt(1, shipment.getAddress().getAddressId());
        updateShipmentPs.setString(2, shipment.getMethod());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(shipment.getShipmentDate());
        updateShipmentPs.setString(3, formattedDate);
        updateShipmentPs.setInt(4, shipment.getShipmentId());

        updateShipmentPs.executeUpdate();
    }

    public void deleteShipment(int shipmentId) throws SQLException {
        deleteShipmentPs.setInt(1, shipmentId);
        deleteShipmentPs.executeUpdate();
    }
    public void updateAddress(int addressId, int streetNumber, String street, String suburb, AuState state, String postcode) throws SQLException {
        PreparedStatement updateAddressPs = conn.prepareStatement(
            "UPDATE Address SET StreetNumber = ?, Street = ?, Suburb = ?, State = ?, Postcode = ? WHERE AddressId = ?"
        );
        
        updateAddressPs.setString(1, String.valueOf(streetNumber));
        updateAddressPs.setString(2, street);
        updateAddressPs.setString(3, suburb);
        updateAddressPs.setInt(4, state.ordinal());
        updateAddressPs.setString(5, postcode);
        updateAddressPs.setInt(6, addressId);
        
        updateAddressPs.executeUpdate();
    }
    
    private Shipment toShipment(ResultSet rs) throws SQLException {
        int shipmentId = rs.getInt("ShipmentId");
        int orderId = rs.getInt("OrderId");
        int addressId = rs.getInt("AddressId");
        String method = rs.getString("Method");
        Date shipmentDate = new Date(rs.getTimestamp("ShipmentDate").getTime());

        OrderDBManager orderDBManager = new OrderDBManager(conn);
        Order order = orderDBManager.getOrder(orderId);

        // Get address details
        String addressQuery = "SELECT * FROM Address WHERE AddressId = ?";
        PreparedStatement addressPs = conn.prepareStatement(addressQuery);
        addressPs.setInt(1, addressId);
        ResultSet addressRs = addressPs.executeQuery();
        
        Address address = null;
        if (addressRs.next()) {
            int streetNumber = Integer.parseInt(addressRs.getString("StreetNumber"));
            String street = addressRs.getString("Street");
            String suburb = addressRs.getString("Suburb");
            int stateIndex = addressRs.getInt("State");
            AuState state = AuState.values()[stateIndex];
            String postcode = String.valueOf(addressRs.getInt("Postcode"));
            
            address = new Address(addressId, streetNumber, street, suburb, state, postcode);
        }

        return new Shipment(shipmentId, order, address, method, shipmentDate);
    }
}