package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Address;
import model.Customer;
import model.Shipment;
import model.Enums.AuState;
import model.dao.ShipmentDBManager;
import model.exceptions.InvalidInputException;

@WebServlet("/UpdateShipmentServlet")
public class UpdateShipmentServlet extends HttpServlet {
    public static final String PAGE = "editshipment.jsp";
    private static final String ERROR_ATTR = "updateShipmentError";
    
    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(UpdateShipmentServlet.class.getName());
    }

    

@Override
public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();
    
    if (!(session.getAttribute("user") instanceof Customer)) {
        response.sendRedirect("index.jsp");
        return;
    }

    ShipmentDBManager shipmentDBManager = (ShipmentDBManager) session.getAttribute("shipmentDBManager");
    if (shipmentDBManager == null) {
        throw new ServletException("ShipmentDBManager retrieved from session is null");
    }

    // Get form parameters
    String shipmentIdStr = request.getParameter("shipmentId");
    String streetNumberStr = request.getParameter("streetNumber");
    String street = request.getParameter("street");
    String suburb = request.getParameter("suburb");
    String stateStr = request.getParameter("state");
    String postcode = request.getParameter("postcode");
    String method = request.getParameter("method");
    String shipmentDateStr = request.getParameter("shipmentDate");
    
    try {
        // Only shipmentId is required
        if (shipmentIdStr == null || shipmentIdStr.isEmpty()) {
            throw new InvalidInputException("Shipment ID is required");
        }
        
        // Parse shipmentId
        int shipmentId = Integer.parseInt(shipmentIdStr);
        
        // Get existing shipment
        Shipment shipment;
        try {
            shipment = shipmentDBManager.getShipment(shipmentId);
            if (shipment == null) {
                throw new InvalidInputException("Shipment not found");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to retrieve shipment", e);
            throw new InvalidInputException("Database error: " + e.getMessage());
        }
        
        // Check if address needs updating
        boolean updateAddress = false;
        int streetNumber = shipment.getAddress().getStreetNumber();
        String updatedStreet = shipment.getAddress().getStreet();
        String updatedSuburb = shipment.getAddress().getSuburb();
        AuState state = shipment.getAddress().getState();
        String updatedPostcode = shipment.getAddress().getPostcode();
        
        if (streetNumberStr != null && !streetNumberStr.isEmpty()) {
            streetNumber = Integer.parseInt(streetNumberStr);
            updateAddress = true;
        }
        
        if (street != null && !street.isEmpty()) {
            updatedStreet = street;
            updateAddress = true;
        }
        
        if (suburb != null && !suburb.isEmpty()) {
            updatedSuburb = suburb;
            updateAddress = true;
        }
        
        if (stateStr != null && !stateStr.isEmpty()) {
            try {
                state = AuState.valueOf(stateStr);
                updateAddress = true;
            } catch (IllegalArgumentException e) {
                throw new InvalidInputException("Invalid state");
            }
        }
        
        if (postcode != null && !postcode.isEmpty()) {
            // Validate postcode format if provided
            if (!postcode.matches("\\d{4}")) {
                throw new InvalidInputException("Postcode must be 4 digits");
            }
            updatedPostcode = postcode;
            updateAddress = true;
        }
        
        // Create updated address if needed
        if (updateAddress) {
            Address updatedAddress = new Address(
                shipment.getAddress().getAddressId(), 
                streetNumber, 
                updatedStreet, 
                updatedSuburb, 
                state, 
                updatedPostcode
            );
            shipment.setAddress(updatedAddress);
        }
        
        // Update method if provided
        if (method != null && !method.isEmpty()) {
            shipment.setMethod(method);
        }
        
        // Update date if provided
        if (shipmentDateStr != null && !shipmentDateStr.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date shipmentDate = dateFormat.parse(shipmentDateStr);
                shipment.setShipmentDate(shipmentDate);
            } catch (ParseException e) {
                throw new InvalidInputException("Invalid shipment date format");
            }
        }
        
        // Update shipment in database
        try {
            shipmentDBManager.updateShipment(shipment);
            
            // If address was updated, you need to update it separately in the database
            if (updateAddress) {
                shipmentDBManager.updateAddress(
                    shipment.getAddress().getAddressId(),
                    shipment.getAddress().getStreetNumber(),
                    shipment.getAddress().getStreet(),
                    shipment.getAddress().getSuburb(),
                    shipment.getAddress().getState(),
                    shipment.getAddress().getPostcode()
                );
            }
            
            session.removeAttribute(ERROR_ATTR);
            response.sendRedirect("shipments.jsp");
            return;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to update shipment", e);
            throw new InvalidInputException("Database error: " + e.getMessage());
        }
        
    } catch (InvalidInputException | NumberFormatException e) {
        session.setAttribute(ERROR_ATTR, e.getMessage());
        request.getRequestDispatcher(PAGE).include(request, response);
    }
}
}