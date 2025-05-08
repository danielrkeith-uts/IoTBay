package model.dao;

import model.*;
import model.Enums.PaymentStatus;
import java.sql.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class PaymentDBManager {

    private Statement st;

    public PaymentDBManager(Connection conn) throws SQLException {
        st = conn.createStatement();
    }

    public void addPayment(Payment payment) throws SQLException {
        String query = "INSERT INTO Payment (Amount, CardId, PaymentStatus, OrderId, Date) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = st.getConnection().prepareStatement(query)) {
            ps.setDouble(1, payment.getAmount());
            ps.setInt(2, payment.getCard().getCardId());
            ps.setInt(3, payment.getPaymentStatus().ordinal());
            ps.setInt(4, payment.getOrderId());
            ps.setDate(5, Date.valueOf(payment.getDate()));

            ps.executeUpdate();
        }
    }

    public Payment getPayment(int paymentId) throws SQLException {
        String query = "SELECT * FROM Payment WHERE PaymentId = ?"; 
        try (PreparedStatement ps = st.getConnection().prepareStatement(query)) {
            ps.setInt(1, paymentId);
            ResultSet rs = ps.executeQuery();
    
            if (rs.next()) {
                int cardId = rs.getInt("CardId");
                double amount = rs.getDouble("Amount");
                int statusIndex = rs.getInt("PaymentStatus");
                PaymentStatus paymentStatus = PaymentStatus.values()[statusIndex];
                int orderId = rs.getInt("OrderId");
                Date date = rs.getDate("Date");
    
                String cardQuery = "SELECT * FROM Card WHERE CardId = ?"; 
                try (PreparedStatement cardPs = st.getConnection().prepareStatement(cardQuery)) {
                    cardPs.setInt(1, cardId);
                    ResultSet cardRs = cardPs.executeQuery();
    
                    if (cardRs.next()) {
                        String name = cardRs.getString("Name");
                        String number = cardRs.getString("Number");
                        YearMonth expiry = YearMonth.from(cardRs.getDate("Expiry").toLocalDate());
                        String cvc = cardRs.getString("CVC");
                        Card card = new Card(cardId, name, number, expiry, cvc);
    
                        return new Payment(amount, card, paymentStatus, orderId, date.toLocalDate());
                    }
                }
            }
        }
        return null;
    }
    

    public List<Payment> getAllPaymentsForUser(int userId) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM Payment WHERE UserId = ?";
    
        try (PreparedStatement ps = st.getConnection().prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
    
            while (rs.next()) {
                int cardId = rs.getInt("CardId");
                double amount = rs.getDouble("Amount");
                int statusIndex = rs.getInt("PaymentStatus");
                PaymentStatus paymentStatus = PaymentStatus.values()[statusIndex];
                int orderId = rs.getInt("OrderId");
                Date date = rs.getDate("Date");
    
                String cardQuery = "SELECT * FROM Card WHERE CardId = ?";
                try (PreparedStatement cps = st.getConnection().prepareStatement(cardQuery)) {
                    cps.setInt(1, cardId);
                    ResultSet cardRs = cps.executeQuery();
    
                    if (cardRs.next()) {
                        String name = cardRs.getString("Name");
                        String number = cardRs.getString("Number");
                        YearMonth expiry = YearMonth.from(cardRs.getDate("Expiry").toLocalDate());
                        String cvc = cardRs.getString("CVC");
    
                        Card card = new Card(cardId, name, number, expiry, cvc);
                        Payment payment = new Payment(amount, card, paymentStatus, orderId, date.toLocalDate());
                        payments.add(payment);
                    }
                }
            }
        }
        return payments;
    }    

public void updatePayment(int paymentId, double newAmount) throws SQLException {
    String query = "UPDATE Payment SET Amount = ? WHERE PaymentId = ?";
    try (PreparedStatement ps = st.getConnection().prepareStatement(query)) {
        ps.setDouble(1, newAmount);
        ps.setInt(2, paymentId);
        ps.executeUpdate();
    }
}

public void deletePayment(int paymentId) throws SQLException {
    String query = "DELETE FROM Payment WHERE PaymentId = ?";
    try (PreparedStatement ps = st.getConnection().prepareStatement(query)) {
        ps.setInt(1, paymentId);
        ps.executeUpdate();
    }
}

}