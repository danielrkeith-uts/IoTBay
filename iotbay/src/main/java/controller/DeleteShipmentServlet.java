package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Customer;
import model.dao.ShipmentDBManager;
import model.exceptions.InvalidInputException;

@WebServlet("/DeleteShipmentServlet")
public class DeleteShipmentServlet extends HttpServlet {
    private static final String ERROR_ATTR = "deleteShipmentError";
    
    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(DeleteShipmentServlet.class.getName());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        if (!(session.getAttribute("user") instanceof Customer)) {
            response.sendRedirect("index.jsp");
            return;
        }

        ShipmentDBManager shipmentDBManager = (ShipmentDBManager) session.getAttribute("shipmentDBManager");
        if (shipmentDBManager == null) {
            throw new ServletException("ShipmentDBManager retrieved from session is null");
        }

        String shipmentIdStr = request.getParameter("shipmentId");
        
        try {
            if (shipmentIdStr == null || shipmentIdStr.isEmpty()) {
                throw new InvalidInputException("Shipment ID is required");
            }
            
            int shipmentId = Integer.parseInt(shipmentIdStr);
            
            try {
                shipmentDBManager.deleteShipment(shipmentId);
                response.sendRedirect("shipments.jsp");
                return;
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Failed to delete shipment", e);
                throw new InvalidInputException("Database error: " + e.getMessage());
            }
            
        } catch (InvalidInputException | NumberFormatException e) {
            session.setAttribute(ERROR_ATTR, e.getMessage());
            response.sendRedirect("shipments.jsp");
        }
    }
}