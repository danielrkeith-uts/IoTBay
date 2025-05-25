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

        Integer editingOrderId = (Integer) session.getAttribute("editingOrderId");
        boolean isEditing = editingOrderId != null;

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

            Card card = new Card(0, cardName, cardNumber, expiry, cvc); // CardId = 0 is replaced below (autoincrement in db)
            int cardId = cardDBManager.addCard(card.getName(), card.getNumber(), card.getExpiry(), card.getCvc()); 
            card.setCardId(cardId); 

            PaymentDBManager paymentDBManager = (PaymentDBManager) session.getAttribute("paymentDBManager");
            if (paymentDBManager == null) {
                throw new ServletException("PaymentDBManager not found in session");
            }

            double total = cart.totalCost();
            Payment payment = new Payment(total, card, PaymentStatus.PENDING);
            int paymentId = paymentDBManager.addPayment(card.getCardId(), total, payment.getPaymentStatus().ordinal());
            System.out.println("DEBUG: Generated paymentId = " + paymentId);

            Integer userId = null;
            if (user != null) {
                userId = user.getUserId();
            } else {
                userId = null;
            }

            CartDBManager cartDBManager = (CartDBManager) session.getAttribute("cartDBManager");
            ProductListEntryDBManager productListEntryDBManager = (ProductListEntryDBManager) session.getAttribute("productListEntryDBManager");
            if (productListEntryDBManager == null) {
                throw new ServletException("ProductListEntryDBManager not found in session");
            }

            Integer cartId;
            if (isEditing) {
                cartId = (Integer) session.getAttribute("editingCartId");

                System.out.println("DEBUG: session editingCartId = " + session.getAttribute("editingCartId"));
                System.out.println("DEBUG: request editingCartId = " + request.getParameter("editingCartId"));
                System.out.println("DEBUG: resolved editingCartId = " + cartId);

                if (cartId == null || cartId <= 0) {
                    throw new ServletException("Invalid editingCartId: " + cartId);
                }

                System.out.println("editingOrderId: " + editingOrderId);
                System.out.println("editingCartId: " + session.getAttribute("editingCartId"));
                System.out.println("isEditing: " + isEditing);
                cartDBManager.clearCart(cartId); 
            } else {
                cartId = cartDBManager.addCart(new Timestamp(System.currentTimeMillis()));

                if (cartId <= 0) {
                    throw new ServletException("Failed to create cart found in ConfirmOrderServlet: cartId returned was " + cartId);
                }
            }
            cart.setCartId(cartId);

            // Save product list to new cart
            for (ProductListEntry entry : cart.getProductList()) {
                productListEntryDBManager.addProduct(cartId, entry.getProduct().getProductId(), entry.getQuantity());
            }

            Timestamp datePlaced = new Timestamp(System.currentTimeMillis());
            if (isEditing) {
                int orderId = editingOrderId;

                Payment updatedPayment = new Payment(total, card, PaymentStatus.PENDING);
                updatedPayment.setPaymentId(paymentId);

                Order updatedOrder = new Order(
                    orderId,
                    cart.getProductList(),
                    updatedPayment,
                    datePlaced,
                    OrderStatus.PLACED
                );

                updatedOrder.setCartId(cart.getCartId());

                User currentUser = (User) session.getAttribute("user");
                if (currentUser != null) {
                    updatedOrder.setUserId(currentUser.getUserId());
                } else {
                    System.out.println("ERROR: No logged-in user found in session.");
                }

                orderDBManager.updateOrder(updatedOrder);

                session.removeAttribute("editingOrderId");
                session.removeAttribute("editingCartId");

                session.removeAttribute("cart");
                Cart newCart = new Cart();
                session.setAttribute("cart", newCart);
                if (user instanceof Customer) {
                    ((Customer) user).setCart(newCart);
                }

                response.sendRedirect("orderconfirmation.jsp?orderId=" + orderId);
                return;

            } else {
                int orderId = orderDBManager.addOrder(
                    userId,
                    cartId,
                    paymentId,
                    datePlaced,
                    OrderStatus.PLACED.toString()
                );
                if (orderId <= 0) {
                    throw new SQLException("Order insert failed or returned invalid ID.");
                }

                session.removeAttribute("cart");
                Cart newCart = new Cart();
                session.setAttribute("cart", newCart);
                if (user instanceof Customer) {
                    ((Customer) user).setCart(newCart);
                }

                response.sendRedirect("orderconfirmation.jsp?orderId=" + orderId);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("cartError", "Failed to place order.");
            response.sendRedirect("cart.jsp");
            logger.log(Level.SEVERE, "Order placement failed", e);
        }
    }
}
