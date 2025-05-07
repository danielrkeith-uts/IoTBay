package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;

import java.io.IOException;
import java.util.List;

import model.Customer;
import model.Order;
import model.dao.OrderDBManager;

@WebServlet("/OrderHistoryServlet")
public class OrderHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        // Redirect to login if user not logged in
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Get current customer
        Customer customer = (Customer) session.getAttribute("user");

        // Get OrderDBManager from session (set by ConnServlet or similar)
        OrderDBManager orderManager = (OrderDBManager) session.getAttribute("orderManager");

        try {
            // Fetch list of orders for the user
            List<Order> orders = orderManager.getOrdersByCustomerId(customer.getUserId());

            // Send order list to JSP
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("order_history.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Unable to retrieve your order history.");
            request.getRequestDispatcher("view_account.jsp").forward(request, response);
        }
    }
}
