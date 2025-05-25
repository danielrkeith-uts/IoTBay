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

        User user = (User) session.getAttribute("user");
        OrderDBManager orderDBManager = (OrderDBManager) session.getAttribute("orderDBManager");
        ProductListEntryDBManager entryDBManager = (ProductListEntryDBManager) session.getAttribute("productListEntryDBManager");
        ProductDBManager productDBManager = (ProductDBManager) session.getAttribute("productDBManager");

        try {
            Order order = orderDBManager.getOrder(orderId);
            if (order != null && order.getOrderStatus() == OrderStatus.SAVED) {
                Cart updatedCart = new Cart();

                for (ProductListEntry entry : order.getProductList()) {
                    updatedCart.addProduct(entry.getProduct(), entry.getQuantity());
                }
                
                System.out.println("DEBUG: orderId=" + orderId + ", order.getCartId()=" + order.getCartId());
                updatedCart.setCartId(order.getCartId());

                session.setAttribute("cart", updatedCart);
                session.setAttribute("editingCartId", updatedCart.getCartId());
                session.setAttribute("editingOrderId", order.getOrderId()); 

                if (user instanceof Customer) {
                    ((Customer) user).setCart(updatedCart);
                }

                response.sendRedirect("cart.jsp");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("myorders.jsp");
    }
}