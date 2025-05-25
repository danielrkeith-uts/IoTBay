package controller;

import java.io.IOException;
import java.sql.Timestamp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.dao.*;
import model.*;
import model.Enums.*;

@WebServlet("/UpdateOrderServlet")
public class UpdateOrderServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        HttpSession session = request.getSession();

        OrderDBManager orderDBManager = (OrderDBManager) session.getAttribute("orderDBManager");
        ProductListEntryDBManager entryDBManager = (ProductListEntryDBManager) session.getAttribute("productListEntryDBManager");
        ProductDBManager productDBManager = (ProductDBManager) session.getAttribute("productDBManager");

        try {
            Order order = orderDBManager.getOrder(orderId);
            if (order != null && order.getOrderStatus() == OrderStatus.SAVED) {

                // Create a new cart based on the saved product list
                CartDBManager cartDBManager = (CartDBManager) session.getAttribute("cartDBManager");

                Cart updatedCart = new Cart();
                for (ProductListEntry entry : order.getProductList()) {
                    updatedCart.addProduct(entry.getProduct(), entry.getQuantity());
                }
                int newCartId = cartDBManager.addCart(new Timestamp(System.currentTimeMillis()));
                updatedCart.setCartId(newCartId);

                // Save to session
                session.setAttribute("cart", updatedCart);
                session.setAttribute("editingOrderId", orderId);

                // Store the cart in session for editing
                session.setAttribute("cart", updatedCart);
                session.setAttribute("editingOrderId", orderId); 

                // Redirect to cart page for editing
                response.sendRedirect("cart.jsp");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("myorders.jsp");
    }
}