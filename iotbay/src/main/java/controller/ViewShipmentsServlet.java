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
import model.Customer;
import model.Shipment;
import model.User;
import model.dao.ShipmentDBManager;

@WebServlet("/ViewShipmentsServlet")
public class ViewShipmentsServlet extends HttpServlet {
    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(ViewShipmentsServlet.class.getName());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (!(user instanceof Customer)) {
            response.sendRedirect("index.jsp");
            return;
        }

        ShipmentDBManager shipmentDBManager = (ShipmentDBManager) session.getAttribute("shipmentDBManager");
        if (shipmentDBManager == null) {
            throw new ServletException("ShipmentDBManager retrieved from session is null");
        }

        String searchQuery = request.getParameter("searchQuery");
        
        try {
            List<Shipment> shipments;
            if (searchQuery != null && !searchQuery.isEmpty()) {
                shipments = shipmentDBManager.searchShipments(user.getUserId(), searchQuery);
            } else {
                shipments = shipmentDBManager.getShipmentsByUser(user.getUserId());
            }
            
            ((Customer)user).setShipments(shipments);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Could not retrieve shipments");
        }
    }
}