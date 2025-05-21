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
import model.Order;
import model.Shipment;
import model.Enums.AuState;
import model.dao.OrderDBManager;
import model.dao.ShipmentDBManager;
import model.exceptions.InvalidInputException;

@WebServlet("/CreateShipmentServlet")
public class CreateShipmentServlet extends HttpServlet {
    public static final String PAGE = "createshipment.jsp";
    private static final String ERROR_ATTR = "shipmentError";
    
    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(CreateShipmentServlet.class.getName());
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
        
        OrderDBManager orderDBManager = (OrderDBManager) session.getAttribute("orderDBManager");
        if (orderDBManager == null) {
            throw new ServletException("OrderDBManager retrieved from session is null");
        }

        // Get form parameters
        String orderIdStr = request.getParameter("orderId");
        String streetNumberStr = request.getParameter("streetNumber");
        String street = request.getParameter("street");
        String suburb = request.getParameter("suburb");
        String stateStr = request.getParameter("state");
        String postcode = request.getParameter("postcode");
        String method = request.getParameter("method");
        String shipmentDateStr = request.getParameter("shipmentDate");
        
        try {
            // Validate inputs
            if (orderIdStr == null || orderIdStr.isEmpty() || 
                streetNumberStr == null || streetNumberStr.isEmpty() || 
                street == null || street.isEmpty() || 
                suburb == null || suburb.isEmpty() || 
                stateStr == null || stateStr.isEmpty() || 
                postcode == null || postcode.isEmpty() ||
                method == null || method.isEmpty() ||
                shipmentDateStr == null || shipmentDateStr.isEmpty()) {
                throw new InvalidInputException("All fields are required");
            }
            
            // Parse values
            int orderId = Integer.parseInt(orderIdStr);
            int streetNumber = Integer.parseInt(streetNumberStr);
            AuState state = AuState.valueOf(stateStr);
            
            // Validate postcode format
            if (!postcode.matches("\\d{4}")) {
                throw new InvalidInputException("Postcode must be 4 digits");
            }
            
            // Get Order
            Order order;
            try {
                order = orderDBManager.getOrder(orderId);
                if (order == null) {
                    throw new InvalidInputException("Order not found");
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Failed to retrieve order", e);
                throw new InvalidInputException("Database error: " + e.getMessage());
            }
            
            // Create Address
            Address address = new Address(-1, streetNumber, street, suburb, state, postcode);
            
            // Parse date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date shipmentDate;
            try {
                shipmentDate = dateFormat.parse(shipmentDateStr);
            } catch (ParseException e) {
                throw new InvalidInputException("Invalid shipment date format");
            }
            
            // Create shipment
            try {
                Shipment shipment = shipmentDBManager.addShipment(order, address, method, shipmentDate);
                ((Customer)session.getAttribute("user")).addShipment(shipment);
                session.removeAttribute(ERROR_ATTR);
                response.sendRedirect("shipments.jsp");
                return;
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Failed to create shipment", e);
                throw new InvalidInputException("Database error: " + e.getMessage());
            }
            
        } catch (InvalidInputException | NumberFormatException e) {
            session.setAttribute(ERROR_ATTR, e.getMessage());
            request.getRequestDispatcher(PAGE).include(request, response);
        }
    }
}