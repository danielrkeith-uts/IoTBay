package model.dao;

import model.*;
import java.sql.*;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class CardDBManager {
    private Statement st;
    private Connection conn;
    private static final String UPDATE_CARD_STMT = "UPDATE Card SET Name = ?, Number = ?, Expiry = ?, CVC = ? WHERE CartId = ?;";
    private final PreparedStatement updateCardPs;
        
    public CardDBManager(Connection conn) throws SQLException {    
        this.conn = conn;
        st = conn.createStatement(); 
        this.updateCardPs = conn.prepareStatement(UPDATE_CARD_STMT);  
    }

    //Find a cart by CarDId in the database   
    public Card getCard(int cardId) throws SQLException {   

        //get a cart from the db
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

    //Add a card to the database   
    public int addCard(String name, String number, YearMonth expiry, String cvc) throws SQLException {       
        String query = "INSERT INTO Card (Name, Number, Expiry, CVC) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1); // generated cardId
        } else {
            throw new SQLException("Card ID not generated.");
        }
    }
}
