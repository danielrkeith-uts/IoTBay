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
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import model.dao.*;
import model.*;

@WebServlet("/ConfirmOrderServlet")
public class ConfirmOrderServlet extends HttpServlet {

    private Logger logger;

    public void init() {
        logger = Logger.getLogger(ConnServlet.class.getName());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        session.setAttribute("firstName", firstName);
        session.setAttribute("lastName", lastName);
        session.setAttribute("email", email);
        session.setAttribute("phone", phone);
        response.sendRedirect("order.jsp");

        String streetNumber = request.getParameter("streetNumber");
        String streetName = request.getParameter("streetName");
        String suburb = request.getParameter("suburb");
        String state = request.getParameter("state");
        String postCode = request.getParameter("postCode");

        session.setAttribute("streetNumber", streetNumber);
        session.setAttribute("streetName", streetName);
        session.setAttribute("suburb", suburb);
        session.setAttribute("state", state);
        session.setAttribute("postCode", postCode);

        String cardNumber = request.getParameter("cardNumber");
        String cardName = request.getParameter("cardName");
        String cvc = request.getParameter("cvc");
        YearMonth expiry = null;
        try {
            String expiryString = request.getParameter("expiry");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            expiry = YearMonth.parse(expiryString, formatter);
        } catch (Exception e) {
            // Log or handle invalid expiry format
            e.printStackTrace();
        }

        session.setAttribute("cardNumber", cardNumber);
        session.setAttribute("cardName", cardName);
        session.setAttribute("cvc", cvc);
        session.setAttribute("expiry", expiry);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        DeliveryDBManager deliveryDBManager = (DeliveryDBManager) session.getAttribute("deliveryDBManager");
        if (deliveryDBManager == null) {
            throw new ServletException("DeliveryDBManager retrieved from session is null");
        }

        try {
            Delivery delivery = deliveryDBManager.getDelivery(0);
            Address address = delivery.getDestination();
            session.setAttribute("address", address);
            return;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error setting address", e);
        }
    }
}
