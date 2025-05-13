package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Call ConnServlet to initialize DB managers
        request.getRequestDispatcher("/ConnServlet").include(request, response);
        
        // Get DeliveryDBManager from session
        DeliveryDBManager deliveryDBManager = (DeliveryDBManager) session.getAttribute("deliveryDBManager");
        
        try {
            // Check if we're viewing or editing a specific shipment
            String action = request.getParameter("action");
            String shipmentId = request.getParameter("shipmentId");
            
            if ((action != null) && (shipmentId != null)) {
                int id = Integer.parseInt(shipmentId);
                Delivery shipment = deliveryDBManager.getDelivery(id);
                request.setAttribute("selectedShipment", shipment);
                
                if ("view".equals(action)) {
                    request.setAttribute("viewOnly", true);
                }
            }
            
            // Get all shipments to display in the table
            List<Delivery> shipments = deliveryDBManager.getAllDeliveries();
            request.setAttribute("shipments", shipments);
            
        } catch (Exception e) {
            session.setAttribute("shippingError", "Error: " + e.getMessage());
        }
        
        // Forward to the JSP page
        request.getRequestDispatcher(PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Redirect to login if not logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Call ConnServlet to initialize DB managers
        request.getRequestDispatcher("/ConnServlet").include(request, response);
        
        // Get DeliveryDBManager from session
        DeliveryDBManager deliveryDBManager = (DeliveryDBManager) session.getAttribute("deliveryDBManager");
        
        try {
            // DELETE operation
            if (request.getParameter("delete") != null) {
                int deliveryId = Integer.parseInt(request.getParameter("shipmentId"));
                deliveryDBManager.deleteDelivery(deliveryId);
                session.setAttribute("successMessage", "Shipment successfully deleted");
            }
            // CREATE operation
            // In your doPost method, modify the create shipment section:
            // CREATE operation
            else if (request.getParameter("create") != null) {
                try {
                    String method = request.getParameter("shipmentMethod");
                    String courier = mapMethodToCourier(method);
                    String addressStr = request.getParameter("address");
                    
                    // Create addresses
                    Address source = new Address(0, 123, "Company St", "Business District", AuState.NSW, "2000");
                    Address destination = parseAddress(addressStr);
                    
                    if (destination == null) {
                        session.setAttribute("shippingError", "Invalid address format. Please use: street number, street, suburb, state, postcode");
                    } else {
                        // Add the shipment
                        deliveryDBManager.addDelivery(source, destination, courier, 12345);
                        session.setAttribute("successMessage", "Shipment successfully created");
                    }
                } catch (Exception e) {
                    session.setAttribute("shippingError", "Error creating shipment: " + e.getMessage());
                }
                
                // Important: Redirect to the GET version to refresh the page with the new data
                response.sendRedirect("ShippingServlet");
                return;  // Return to prevent further processing
            }
            // UPDATE operation
            else if (request.getParameter("update") != null) {
                processCreateOrUpdate(request, session, deliveryDBManager, true);
            }
            
        } catch (Exception e) {
            session.setAttribute("shippingError", "Error: " + e.getMessage());
        }
        
        // Redirect back to the shipping page
        response.sendRedirect("ShippingServlet");
    }
    
    private void processCreateOrUpdate(HttpServletRequest request, HttpSession session, 
            DeliveryDBManager deliveryDBManager, boolean isUpdate) throws SQLException {
        
        // Get form data
        String method = request.getParameter("shipmentMethod");
        String courier = mapMethodToCourier(method);
        String addressStr = request.getParameter("address");
        
        // Create addresses
        Address source = new Address(0, 123, "Company St", "Business District", AuState.NSW, "2000");
        Address destination = parseAddress(addressStr);
        
        if (destination == null) {
            session.setAttribute("shippingError", "Invalid address format. Please use: street number, street, suburb, state, postcode");
            return;
        }
        
        // Perform update or create
        if (isUpdate) {
            int deliveryId = Integer.parseInt(request.getParameter("shipmentId"));
            deliveryDBManager.updateDelivery(deliveryId, source, destination, courier, 12345);
            session.setAttribute("successMessage", "Shipment successfully updated");
        } else {
            deliveryDBManager.addDelivery(source, destination, courier, 12345);
            session.setAttribute("successMessage", "Shipment successfully created");
        }
    }
    
    private Address parseAddress(String addressStr) {
        if (addressStr == null || addressStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            String[] parts = addressStr.split(",");
            if (parts.length < 5) {
                return null;
            }
            
            int streetNumber = Integer.parseInt(parts[0].trim());
            String street = parts[1].trim();
            String suburb = parts[2].trim();
            AuState state = AuState.valueOf(parts[3].trim().toUpperCase());
            String postcode = parts[4].trim();
            
            return new Address(0, streetNumber, street, suburb, state, postcode);
        } catch (Exception e) {
            return null;
        }
    }
    
    private String mapMethodToCourier(String method) {
        if ("Express".equals(method)) return "DHL";
        if ("Priority".equals(method)) return "FedEx";
        return "Australia Post";
    }
}