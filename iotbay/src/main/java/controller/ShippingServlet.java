package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Address;
import model.Delivery;
import model.Enums.AuState;
import model.User;
import model.dao.DeliveryDBManager;

@WebServlet("/ShippingServlet")
public class ShippingServlet extends HttpServlet {
    private static final String PAGE = "shipping.jsp";
    private static final String ERROR_ATTR = "shippingError";
    private static final String SUCCESS_ATTR = "successMessage";
    
    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(ShippingServlet.class.getName());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Ensure ConnServlet is called to initialize DB managers
        request.getRequestDispatcher("/ConnServlet").include(request, response);
        
        // Get DeliveryDBManager from session
        DeliveryDBManager deliveryDBManager = (DeliveryDBManager) session.getAttribute("deliveryDBManager");
        if (deliveryDBManager == null) {
            throw new ServletException("DeliveryDBManager not initialized");
        }
        
        String action = request.getParameter("action");
        String shipmentId = request.getParameter("shipmentId");
        
        if ("view".equals(action) || "edit".equals(action)) {
            try {
                int id = Integer.parseInt(shipmentId);
                Delivery shipment = deliveryDBManager.getDelivery(id);
                request.setAttribute("selectedShipment", shipment);
                if ("view".equals(action)) {
                    request.setAttribute("viewOnly", true);
                }
            } catch (Exception e) {
                session.setAttribute(ERROR_ATTR, "Error loading shipment: " + e.getMessage());
                logger.log(Level.SEVERE, "Error loading shipment", e);
            }
        }
        
        // Fetch all deliveries from the database
        try {
            List<Delivery> shipments = deliveryDBManager.getAllDeliveries();
            request.setAttribute("shipments", shipments);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error loading all shipments", e);
            // Don't set an error attribute here to avoid confusing the user
            // Just log it and continue - the JSP will handle the case when shipments is null
        }
        
        request.getRequestDispatcher(PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Ensure ConnServlet is called to initialize DB managers
        request.getRequestDispatcher("/ConnServlet").include(request, response);
        
        // Get DeliveryDBManager from session
        DeliveryDBManager deliveryDBManager = (DeliveryDBManager) session.getAttribute("deliveryDBManager");
        if (deliveryDBManager == null) {
            throw new ServletException("DeliveryDBManager not initialized");
        }
        
        // Handle Delete action
        if (request.getParameter("delete") != null) {
            try {
                int deliveryId = Integer.parseInt(request.getParameter("shipmentId"));
                deliveryDBManager.deleteDelivery(deliveryId);
                session.setAttribute(SUCCESS_ATTR, "Shipment successfully deleted");
            } catch (Exception e) {
                session.setAttribute(ERROR_ATTR, "Error deleting shipment: " + e.getMessage());
                logger.log(Level.SEVERE, "Error deleting shipment", e);
            }
        }
        // Handle Create/Update action
        else if (request.getParameter("create") != null || request.getParameter("update") != null) {
            String courier = mapMethodToCourier(request.getParameter("shipmentMethod"));
            String addressStr = request.getParameter("address");
            
            try {
                Address source = new Address(0, 123, "Company St", "Business District", AuState.NSW, "2000");
                Address destination = parseAddress(addressStr);
                
                if (destination == null) {
                    session.setAttribute(ERROR_ATTR, "Invalid address format. Please use: street number, street, suburb, state, postcode");
                } else {
                    if (request.getParameter("update") != null) {
                        int deliveryId = Integer.parseInt(request.getParameter("shipmentId"));
                        deliveryDBManager.updateDelivery(deliveryId, source, destination, courier, 12345);
                        session.setAttribute(SUCCESS_ATTR, "Shipment successfully updated");
                    } else {
                        deliveryDBManager.addDelivery(source, destination, courier, 12345);
                        session.setAttribute(SUCCESS_ATTR, "Shipment successfully created");
                    }
                }
            } catch (Exception e) {
                session.setAttribute(ERROR_ATTR, "Error processing shipment: " + e.getMessage());
                logger.log(Level.SEVERE, "Error processing shipment", e);
            }
        }
        
        response.sendRedirect(PAGE);
    }
    
    private Address parseAddress(String addressStr) {
        if (addressStr == null || addressStr.trim().isEmpty()) {
            return null;
        }
        
        String[] parts = addressStr.split(",");
        if (parts.length < 5) {
            return null;
        }
        
        try {
            int streetNumber = Integer.parseInt(parts[0].trim());
            String street = parts[1].trim();
            String suburb = parts[2].trim();
            AuState state = AuState.valueOf(parts[3].trim().toUpperCase());
            String postcode = parts[4].trim();
            
            return new Address(0, streetNumber, street, suburb, state, postcode);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error parsing address: " + e.getMessage(), e);
            return null;
        }
    }
    
    private String mapMethodToCourier(String method) {
        if ("Express".equals(method)) return "DHL";
        if ("Priority".equals(method)) return "FedEx";
        return "Australia Post";
    }
}