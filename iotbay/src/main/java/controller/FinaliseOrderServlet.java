package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.dao.*;
import model.*;
import model.Enums.OrderStatus;
import model.Enums.PaymentStatus;


@WebServlet("/FinaliseOrderServlet")
public class FinaliseOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        User user = (User) session.getAttribute("user");
        Integer orderId = (Integer) session.getAttribute("editingOrderId");

        OrderDBManager orderDBManager = (OrderDBManager) session.getAttribute("orderDBManager");
        CardDBManager cardDBManager = (CardDBManager) session.getAttribute("cardDBManager");
        PaymentDBManager paymentDBManager = (PaymentDBManager) session.getAttribute("paymentDBManager");
        ProductListEntryDBManager entryDBManager = (ProductListEntryDBManager) session.getAttribute("productListEntryDBManager");

        String stringCardId = request.getParameter("cardId");
        String stringAmount = request.getParameter("amount");
        String stringPaymentStatus = request.getParameter("paymentStatus");
        String orderIdParam = request.getParameter("orderId");

        int cardId = Integer.parseInt(stringCardId);
        double amount = Double.parseDouble(stringAmount);
        int paymentStatusIndex = Integer.parseInt(stringPaymentStatus);
        orderId = Integer.parseInt(orderIdParam);

        PaymentStatus paymentStatus = PaymentStatus.values()[paymentStatusIndex];
        Card card = null;
        try {
            card = cardDBManager.getCard(cardId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (user == null || !(user instanceof Customer)) {
                response.sendRedirect("LoginPageServlet");
                return;
            }

            //NEED PAYMENT ID HERE --> WANT TO REMOVE
            Payment payment = new Payment(0, amount, card, paymentStatus);
            int paymentId = paymentDBManager.addPayment(cardId, amount, paymentStatusIndex);

            // Save products to DB for this cart
            int cartId = cart.getCartId();
            entryDBManager.clearCart(cartId); // clear old entries --> NEEDS TO BE WRITTEN
            for (ProductListEntry entry : cart.getProductList()) {
                entryDBManager.addProduct(cartId, entry.getProduct().getProductId(), entry.getQuantity());
            }

            // Update the existing order
            Order updatedOrder = new Order(orderId, cart.getProductList(), payment, new Timestamp(System.currentTimeMillis()), OrderStatus.PROCESSING);
            orderDBManager.updateOrder(updatedOrder);

            // Clear edit mode
            session.removeAttribute("editingOrderId");
            session.removeAttribute("cart");

            response.sendRedirect("myorders.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("cart.jsp");
        }
    }
}