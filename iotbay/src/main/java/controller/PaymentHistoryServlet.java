package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Payment;
import model.dao.PaymentDBManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

@WebServlet("/PaymentHistoryServlet")
public class PaymentHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourDB", "username", "password");
            PaymentDBManager dbManager = new PaymentDBManager(conn);

            int userId = Integer.parseInt(request.getParameter("userId"));
            List<Payment> payments = dbManager.getAllPaymentsForUser(userId);

            request.setAttribute("payments", payments);
            request.getRequestDispatcher("payment_history.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching payment history.");
        }
    }
}

