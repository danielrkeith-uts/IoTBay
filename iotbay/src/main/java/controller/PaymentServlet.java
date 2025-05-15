package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Card;
import model.Payment;
import model.Enums.PaymentStatus;
import model.dao.PaymentDBManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.YearMonth;
import java.util.List;
import java.time.LocalDate;

@WebServlet("/PaymentServlet")
public class PaymentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourDB", "username", "password");
            PaymentDBManager dbManager = new PaymentDBManager(conn);

            if ("create".equals(action)) {
                double amount = Double.parseDouble(request.getParameter("amount"));
                String cardName = request.getParameter("cardName");
                String cardNumber = request.getParameter("cardNumber");
                String cardExpiryStr = request.getParameter("cardExpiry");
                String cardCVC = request.getParameter("cardCVC");

                YearMonth cardExpiry = YearMonth.parse(cardExpiryStr);
                Card card = new Card(0, cardName, cardNumber, cardExpiry, cardCVC);

                int orderId = Integer.parseInt(request.getParameter("orderId"));
                LocalDate date = LocalDate.now();

                Payment payment = new Payment(
                    amount,
                    card,
                    PaymentStatus.PENDING,
                    orderId,
                    date
                );

                dbManager.addPayment(payment);
                response.sendRedirect("payment_history.jsp");

            } else if ("update".equals(action)) {
                int paymentId = Integer.parseInt(request.getParameter("paymentId"));
                double newAmount = Double.parseDouble(request.getParameter("newAmount"));
                
                // Assuming the update method in dbManager can update payment details
                dbManager.updatePayment(paymentId, newAmount);
                response.sendRedirect("payment_history.jsp");

            } else if ("delete".equals(action)) {
                int paymentId = Integer.parseInt(request.getParameter("paymentId"));
                
                dbManager.deletePayment(paymentId);
                response.sendRedirect("payment_history.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing payment.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourDB", "username", "password")) {
            PaymentDBManager dbManager = new PaymentDBManager(conn);
        
            int userId = 1; // Replace with session-based user ID later
            List<Payment> payments = dbManager.getAllPaymentsForUser(userId);

            request.setAttribute("payments", payments);
            request.getRequestDispatcher("payment_history.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading payments.");
    }
}

}
