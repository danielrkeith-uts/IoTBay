package model.dao;

import model.*;
import model.Enums.PaymentStatus;
import java.sql.*;
import java.time.YearMonth;

public class PaymentDBManager {

    private Connection conn;
    private Statement st;

    public PaymentDBManager(Connection conn) throws SQLException {
        this.st = conn.createStatement(); 
    }

    public Payment getPayment(int PaymentId) throws SQLException {
        String query = "SELECT * FROM Payment WHERE PaymentId = '" + PaymentId + "'"; 
        ResultSet rs = st.executeQuery(query); 

        if (rs.next()) {
            int paymentId = rs.getInt("PaymentId");
            int CardId = rs.getInt("CardId");
            double Amount = rs.getDouble("Amount");
            int statusIndex = rs.getInt("PaymentStatus");
            PaymentStatus paymentStatus = PaymentStatus.values()[statusIndex];

            // create a Card instance
            String cardQuery = "SELECT * FROM Card WHERE CardId = '" + CardId + "'"; 
            ResultSet cardRs = st.executeQuery(cardQuery); 
            
            if (cardRs.next()) {
                String Name = rs.getString("Name");
                String Number = rs.getString("Number");
                YearMonth Expiry = YearMonth.from(rs.getDate("Expiry").toLocalDate());
                String CVC = rs.getString("CVC");
                Card Card = new Card(CardId, Name, Number, Expiry, CVC);

                return new Payment(paymentId, Amount, Card, paymentStatus);
            }
        }
        return null;
    }
    
    public int addPayment(int CardId, double Amount, int PaymentStatus) throws SQLException {       
        String query = "INSERT INTO Payment (CardId, Amount, PaymentStatus) VALUES (?, ?, ?)";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, CardId);
            pst.setDouble(2, Amount);
            pst.setInt(3, PaymentStatus);

            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return generated PaymentId
                } else {
                    throw new SQLException("Payment insertion failed, no ID obtained.");
                }
            }
        }
    }
}