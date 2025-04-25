package model.dao;

import model.*;
import model.Enums.PaymentStatus;
import java.sql.*;
import java.time.YearMonth;

public class PaymentDBManager {

    private Statement st;

    public Payment getPayment(int PaymentId) throws SQLException {
        String query = "SELECT * FROM Payment WHERE PaymentId = '" + PaymentId + "'"; 
        ResultSet rs = st.executeQuery(query); 

        if (rs.next()) {
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
                Card Card = new Card(Name, Number, Expiry, CVC);

                return new Payment(Amount, Card, paymentStatus);
            }
        }
        return null;
    }
}