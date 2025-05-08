package controller;

import model.*;
import model.Enums.AuState;
import model.Enums.DeliveryStatus;
import model.dao.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ShipmentServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        
        // If no action specified, default to list shipments
        if (action == null) {
            action = "listShipments";
        }
        
        try {
            // Get database connection
            DBConnector connector = new DBConnector();
            Connection conn = connector.openConnection();
            
            // Get current user
            User user = (User) session.getAttribute("user");
            
            if (user == null) {
                // Redirect to login if not logged in
                response.sendRedirect("login.jsp");
                return;
            }
            
            int userId = user.getUserId();
            DeliveryDBManager deliveryManager = new DeliveryDBManager(conn);
            
            switch (action) {
                case "listShipments":
                    // Get all shipments for the user
                    List<Delivery> deliveries = deliveryManager.getUserDeliveries(userId);
                    request.setAttribute("deliveries", deliveries);
                    request.getRequestDispatcher("/listShipments.jsp").forward(request, response);
                    break;
                    
                case "viewShipment":
                    // View a specific shipment
                    int deliveryId = Integer.parseInt(request.getParameter("deliveryId"));
                    Delivery delivery = deliveryManager.searchDeliveryById(userId, deliveryId);
                    
                    if (delivery == null) {
                        request.setAttribute("errorMessage", "Shipment not found");
                        request.getRequestDispatcher("/listShipments.jsp").forward(request, response);
                    } else {
                        request.setAttribute("delivery", delivery);
                        request.getRequestDispatcher("/viewShipment.jsp").forward(request, response);
                    }
                    break;
                    
                case "createShipment":
                    // Show create shipment form
                    request.getRequestDispatcher("/createShipment.jsp").forward(request, response);
                    break;
                    
                case "updateShipment":
                    // Show update shipment form
                    deliveryId = Integer.parseInt(request.getParameter("deliveryId"));
                    delivery = deliveryManager.searchDeliveryById(userId, deliveryId);
                    
                    if (delivery == null) {
                        request.setAttribute("errorMessage", "Shipment not found");
                        request.getRequestDispatcher("/listShipments.jsp").forward(request, response);
                    } else if (delivery.isFinalized()) {
                        request.setAttribute("errorMessage", "Cannot update a finalized shipment");
                        request.setAttribute("delivery", delivery);
                        request.getRequestDispatcher("/viewShipment.jsp").forward(request, response);
                    } else {
                        request.setAttribute("delivery", delivery);
                        request.getRequestDispatcher("/updateShipment.jsp").forward(request, response);
                    }
                    break;
                    
                case "searchShipments":
                    // Show search form
                    request.getRequestDispatcher("/searchShipments.jsp").forward(request, response);
                    break;
                    
                default:
                    // Default to list shipments
                    deliveries = deliveryManager.getUserDeliveries(userId);
                    request.setAttribute("deliveries", deliveries);
                    request.getRequestDispatcher("/listShipments.jsp").forward(request, response);
                    break;
            }
            
            // Close the connection
            connector.closeConnection();
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ShipmentServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("errorMessage", "Database error: " + ex.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        
        try {
            // Get database connection
            DBConnector connector = new DBConnector();
            Connection conn = connector.openConnection();
            
            // Get current user
            User user = (User) session.getAttribute("user");
            
            if (user == null) {
                // Redirect to login if not logged in
                response.sendRedirect("login.jsp");
                return;
            }
            
            int userId = user.getUserId();
            DeliveryDBManager deliveryManager = new DeliveryDBManager(conn);
            
            switch (action) {
                case "createShipment":
                    // Handle create shipment form submission
                    createShipment(request, response, conn, deliveryManager, userId);
                    break;
                    
                case "updateShipment":
                    // Handle update shipment form submission
                    updateShipment(request, response, conn, deliveryManager, userId);
                    break;
                    
                case "deleteShipment":
                    // Handle delete shipment request
                    deleteShipment(request, response, conn, deliveryManager, userId);
                    break;
                    
                case "finalizeShipment":
                    // Handle finalize shipment request
                    finalizeShipment(request, response, conn, deliveryManager, userId);
                    break;
                    
                case "searchShipments":
                    // Handle search shipments form submission
                    searchShipments(request, response, conn, deliveryManager, userId);
                    break;
                    
                default:
                    // Default to list shipments
                    List<Delivery> deliveries = deliveryManager.getUserDeliveries(userId);
                    request.setAttribute("deliveries", deliveries);
                    request.getRequestDispatcher("/listShipments.jsp").forward(request, response);
                    break;
            }
            
            // Close the connection
            connector.closeConnection();
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ShipmentServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("errorMessage", "Database error: " + ex.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    private void createShipment(HttpServletRequest request, HttpServletResponse response, 
                                Connection conn, DeliveryDBManager deliveryManager, int userId)
            throws ServletException, IOException, SQLException {
        
        try {
            // Get order ID
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            
            // Get form data for source address
            int srcStreetNumber = Integer.parseInt(request.getParameter("srcStreetNumber"));
            String srcStreet = request.getParameter("srcStreet");
            String srcSuburb = request.getParameter("srcSuburb");
            AuState srcState = AuState.valueOf(request.getParameter("srcState"));
            String srcPostcode = request.getParameter("srcPostcode");
            
            // Get form data for destination address
            int destStreetNumber = Integer.parseInt(request.getParameter("destStreetNumber"));
            String destStreet = request.getParameter("destStreet");
            String destSuburb = request.getParameter("destSuburb");
            AuState destState = AuState.valueOf(request.getParameter("destState"));
            String destPostcode = request.getParameter("destPostcode");
            
            // Get courier and delivery date
            String courier = request.getParameter("courier");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date estimatedDeliveryDate = dateFormat.parse(request.getParameter("estimatedDeliveryDate"));
            
            // Create Address objects
            Address sourceAddress = new Address(0, srcStreetNumber, srcStreet, srcSuburb, srcState, srcPostcode);
            Address destAddress = new Address(0, destStreetNumber, destStreet, destSuburb, destState, destPostcode);
            
            // Add the delivery - courier tracking ID is typically 0 until it's finalized
            int deliveryId = deliveryManager.addDelivery(sourceAddress, destAddress, courier, 0, DeliveryStatus.PENDING, estimatedDeliveryDate);
            
            if (deliveryId > 0) {
                // Update the order with the new delivery ID
                OrderDBManager orderManager = new OrderDBManager(conn);
                orderManager.updateOrderDelivery(orderId, deliveryId);
                
                request.setAttribute("successMessage", "Shipment created successfully");
                response.sendRedirect("shipment?action=viewShipment&deliveryId=" + deliveryId);
            } else {
                request.setAttribute("errorMessage", "Failed to create shipment");
                request.getRequestDispatcher("/createShipment.jsp").forward(request, response);
            }
        } catch (NumberFormatException | ParseException ex) {
            Logger.getLogger(ShipmentServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("errorMessage", "Invalid input: " + ex.getMessage());
            request.getRequestDispatcher("/createShipment.jsp").forward(request, response);
        }
    }
    
    private void updateShipment(HttpServletRequest request, HttpServletResponse response, 
                               Connection conn, DeliveryDBManager deliveryManager, int userId)
            throws ServletException, IOException, SQLException {
        
        try {
            // Get delivery ID
            int deliveryId = Integer.parseInt(request.getParameter("deliveryId"));
            
            // First check if the delivery exists and belongs to this user
            Delivery delivery = deliveryManager.searchDeliveryById(userId, deliveryId);
            
            if (delivery == null) {
                request.setAttribute("errorMessage", "Shipment not found");
                List<Delivery> deliveries = deliveryManager.getUserDeliveries(userId);
                request.setAttribute("deliveries", deliveries);
                request.getRequestDispatcher("/listShipments.jsp").forward(request, response);
                return;
            }
            
            if (delivery.isFinalized()) {
                request.setAttribute("errorMessage", "Cannot update a finalized shipment");
                request.setAttribute("delivery", delivery);
                request.getRequestDispatcher("/viewShipment.jsp").forward(request, response);
                return;
            }
            
            // Get form data for source address
            int srcStreetNumber = Integer.parseInt(request.getParameter("srcStreetNumber"));
            String srcStr