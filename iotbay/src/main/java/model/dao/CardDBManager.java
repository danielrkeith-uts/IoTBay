package model.dao;

import model.*;
import java.sql.*;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class CardDBManager {
    private Statement st;
    private Connection conn;
    private static final String UPDATE_CARD_STMT = "UPDATE Card SET Name = ?, Number = ?, Expiry = ?, CVC = ? WHERE CardId = ?;";
    private final PreparedStatement updateCardPs;
        
    public CardDBManager(Connection conn) throws SQLException {    
        this.conn = conn;
        st = conn.createStatement(); 
        this.updateCardPs = conn.prepareStatement(UPDATE_CARD_STMT);  
    }

    public Card getCard(int cardId) throws SQLException {   

        String query = "SELECT * FROM Card WHERE CardId = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, cardId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String name = rs.getString("Name");
            String number = rs.getString("Number");
            String expiryStr = rs.getString("Expiry");
            YearMonth expiry = YearMonth.parse(expiryStr, DateTimeFormatter.ofPattern("MM/yy"));
            String cvc = rs.getString("CVC");

            Card card = new Card(cardId, name, number, expiry, cvc);

            return card;
        } 
        return null;
    }
  
    public int addCard(String name, String number, YearMonth expiry, String cvc) throws SQLException {   
        String query = "INSERT INTO Card (Name, Number, Expiry, CVC) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, name);
            pst.setString(2, number);

            java.sql.Date sqlExpiry = java.sql.Date.valueOf(expiry.atDay(1));
            pst.setDate(3, sqlExpiry);

            pst.setString(4, cvc);

            pst.executeUpdate();

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return generated CardId
                } else {
                    throw new SQLException("Creating card failed, no ID obtained.");
                }
            }
        }
    }
}
