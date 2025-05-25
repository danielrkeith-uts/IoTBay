package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.dao.OrderDBManager;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/CancelOrderServlet")
public class CancelOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        HttpSession session = request.getSession();
        OrderDBManager orderDBManager = (OrderDBManager) session.getAttribute("orderDBManager");

        try {
            orderDBManager.cancelOrder(orderId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("myorders.jsp");
    }
}