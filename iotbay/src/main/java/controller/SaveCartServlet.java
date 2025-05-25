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

import model.dao.*;
import model.*;

@WebServlet("/SaveCartServlet")
public class SaveCartServlet extends HttpServlet {

    private Logger logger;

    public void init() {
        logger = Logger.getLogger(ConnServlet.class.getName());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

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
            CartDBManager cartDBManager = (CartDBManager) session.getAttribute("cartDBManager");
            int newCartId = cartDBManager.addCart(new Timestamp(System.currentTimeMillis()));
            cart.setCartId(newCartId);

            ProductListEntryDBManager productListEntryDBManager = (ProductListEntryDBManager) session.getAttribute("productListEntryDBManager");
            for (ProductListEntry entry : cart.getProductList()) {
                productListEntryDBManager.addProduct(newCartId, entry.getProduct().getProductId(), entry.getQuantity());
            }


            Timestamp datePlaced = new Timestamp(System.currentTimeMillis());
            int orderId = orderDBManager.addOrderAsSavedCart(
                user.getUserId(),
                newCartId,
                datePlaced
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
            response.sendRedirect("savedconfirmation.jsp?orderId=" + orderId);

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("cartError", "Failed to place order.");
            response.sendRedirect("cart.jsp");
            logger.log(Level.SEVERE, "Order placement failed", e);
        }
    }
}
