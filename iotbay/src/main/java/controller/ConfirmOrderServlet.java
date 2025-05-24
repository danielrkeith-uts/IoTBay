package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import java.time.format.DateTimeParseException;

import model.dao.*;
import model.*;
import model.Enums.*;

@WebServlet("/ConfirmOrderServlet")
public class ConfirmOrderServlet extends HttpServlet {

    private Logger logger;

    public void init() {
        logger = Logger.getLogger(ConnServlet.class.getName());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Get and store form data
        // String firstName = request.getParameter("firstName");
        // String lastName = request.getParameter("lastName");
        // String email = request.getParameter("email");
        // String phone = request.getParameter("phone");

        // String streetNumberStr = request.getParameter("streetNumber");
        // String streetName = request.getParameter("streetName");
        // String suburb = request.getParameter("suburb");
        // String stateStr = request.getParameter("state");
        // String postCode = request.getParameter("postCode");

        String cardNumber = request.getParameter("cardNumber");
        String cardName = request.getParameter("cardName");
        String cvc = request.getParameter("cvc");

        String expiryString = request.getParameter("expiry"); // e.g., "05/26"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth expiry = null;

        try {
            expiry = YearMonth.parse(expiryString, formatter);
        } catch (DateTimeParseException e) {
            session.setAttribute("cartError", "Invalid expiry date format. Use MM/yy.");
            response.sendRedirect("login.jsp");
            return;
        }

        // Get session data
        User user = (User) session.getAttribute("user");
        Cart cart = (Cart) session.getAttribute("cart");
        OrderDBManager orderDBManager = (OrderDBManager) session.getAttribute("orderDBManager");

        if (cart == null) {
            session.setAttribute("cartError", "Cart is empty or user is not logged in.");
            response.sendRedirect("products.jsp");
            return;
        }

        if (cart.getProductList().isEmpty()) {
            session.setAttribute("cartError", "Cart is empty or user is not logged in.");
            response.sendRedirect("index.jsp");
            return;
        }

        if (orderDBManager == null) {
            throw new ServletException("OrderDBManager not found in session");
        }

        try {
            CardDBManager cardDBManager = (CardDBManager) session.getAttribute("cardDBManager");
            if (cardDBManager == null) {
                throw new ServletException("CardDBManager not found in session");
            }

            Card card = new Card(0, cardName, cardNumber, expiry, cvc); // CardId is 0 for autoincrement
            int cardId = cardDBManager.addCard(card.getName(), card.getNumber(), card.getExpiry(), card.getCvc()); 
            card.setCardId(cardId); 

            PaymentDBManager paymentDBManager = (PaymentDBManager) session.getAttribute("paymentDBManager");
            if (paymentDBManager == null) {
                throw new ServletException("PaymentDBManager not found in session");
            }

            double total = cart.totalCost();
            Payment payment = new Payment(0, total, card, PaymentStatus.PENDING);
            int paymentId = paymentDBManager.addPayment(card.getCardId(), total, payment.getPaymentStatus().ordinal());

            // Add order to DB regardless of logged in user or not
            Integer userId = null;
            if (user != null) {
                userId = user.getUserId();
            } else {
                // guest order, no userId or use special guest ID
                userId = 0;  // or null if DB supports
            }

            Timestamp datePlaced = new Timestamp(System.currentTimeMillis());
            int orderId = orderDBManager.addOrder(
                userId,
                cart.getCartId(),
                paymentId,
                datePlaced,
                OrderStatus.PLACED.toString()
            );
            if (orderId <= 0) {
                throw new SQLException("Order insert failed or returned invalid ID.");
            }

            // Clear cart after order is placed
            session.removeAttribute("cart");

            // Redirect to confirmation page
            response.sendRedirect("orderconfirmation.jsp?orderId=" + orderId);

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("cartError", "Failed to place order.");
            response.sendRedirect("cart.jsp");
            logger.log(Level.SEVERE, "Order placement failed", e);
        }
    }

    // protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //     HttpSession session = request.getSession();

    //     DeliveryDBManager deliveryDBManager = (DeliveryDBManager) session.getAttribute("deliveryDBManager");
    //     if (deliveryDBManager == null) {
    //         throw new ServletException("DeliveryDBManager retrieved from session is null");
    //     }

    //     try {
    //         Delivery delivery = deliveryDBManager.getDelivery(0);
    //         Address address = delivery.getDestination();
    //         session.setAttribute("address", address);
    //         return;
    //     } catch (SQLException e) {
    //         logger.log(Level.SEVERE, "Error setting address", e);
    //     }
    // }
}
