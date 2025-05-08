package controller;

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

                // Add payment to DB
                dbManager.addPayment(payment);
