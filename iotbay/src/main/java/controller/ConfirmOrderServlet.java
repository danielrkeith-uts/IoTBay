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
            Payment payment = new Payment(total, card, PaymentStatus.PENDING);
            int paymentId = paymentDBManager.addPayment(card.getCardId(), total, payment.getPaymentStatus().ordinal());

            Integer userId = null;
            if (user != null) {
                userId = user.getUserId();
            } else {
                userId = null;
            }

            CartDBManager cartDBManager = (CartDBManager) session.getAttribute("cartDBManager");
            int newCartId = cartDBManager.addCart(new Timestamp(System.currentTimeMillis()));
            cart.setCartId(newCartId);

            ProductListEntryDBManager productListEntryDBManager = (ProductListEntryDBManager) session.getAttribute("productListEntryDBManager");
            if (productListEntryDBManager == null) {
                throw new ServletException("ProductListEntryDBManager not found in session");
            }

            for (ProductListEntry entry : cart.getProductList()) {
                productListEntryDBManager.addProduct(newCartId, entry.getProduct().getProductId(), entry.getQuantity());
            }


            Timestamp datePlaced = new Timestamp(System.currentTimeMillis());
            int orderId = orderDBManager.addOrder(
                userId,
                newCartId,
                paymentId,
                datePlaced,
                OrderStatus.PLACED.toString()
            );
            if (orderId <= 0) {
                throw new SQLException("Order insert failed or returned invalid ID.");
            }

            // Clear cart and create new one after order is placed
            session.removeAttribute("cart");
            session.setAttribute("cart", new Cart());
            
            Cart newCart = new Cart();
            session.setAttribute("cart", newCart);
            if (user instanceof Customer) {
                ((Customer) user).setCart(newCart);
            }

            // Redirect to confirmation page
            response.sendRedirect("orderconfirmation.jsp?orderId=" + orderId);

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("cartError", "Failed to place order.");
            response.sendRedirect("cart.jsp");
            logger.log(Level.SEVERE, "Order placement failed", e);
        }
    }
}
