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

@WebServlet("/PaymentDetailServlet")
public class PaymentDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourDB", "username", "password")) {
            PaymentDBManager dbManager = new PaymentDBManager(conn);

            int paymentId = Integer.parseInt(request.getParameter("paymentId"));
            Payment payment = dbManager.getPayment(paymentId);

            if (payment != null) {
                request.setAttribute("payment", payment);
                request.getRequestDispatcher("payment_detail.jsp").forward(request, response);
            } else {
                response.sendRedirect("payment_not_found.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching payment details.");
        }
    }
}

