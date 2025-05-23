package model.dao;

import model.*;
import model.Enums.PaymentStatus;

import java.sql.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class PaymentDBManager {

    private final Connection conn;
    private DBConnector dbConnector;
    
    public PaymentDBManager() throws ClassNotFoundException, SQLException {
        this.dbConnector = new DBConnector();
        this.conn = dbConnector.openConnection();
    }

    public PaymentDBManager(Connection connection) {
        this.conn = connection;
    }
    
    public int addPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO Payment (Amount, CardId, PaymentStatus, PaymentDate, userId) VALUES (?, ?, ?, ?, ?)";
    
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, payment.getAmount());
            stmt.setInt(2, payment.getCard().getCardId());
            stmt.setInt(3, payment.getPaymentStatus().ordinal());
            stmt.setDate(4, new java.sql.Date(payment.getDate().getTime()));
            stmt.setInt(5, payment.getUserId());
    
            int affectedRows = stmt.executeUpdate();

<<<<<<< HEAD
            if (affectedRows == 0) {
                throw new SQLException("Creating payment failed, no rows affected.");
            }
=======
        if (rs.next()) {
            int paymentId = rs.getInt("PaymentId");
            int CardId = rs.getInt("CardId");
            double Amount = rs.getDouble("Amount");
            int statusIndex = rs.getInt("PaymentStatus");
            PaymentStatus paymentStatus = PaymentStatus.values()[statusIndex];
>>>>>>> origin/main

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating payment failed, no ID obtained.");
                }
            }
        }
    }

<<<<<<< HEAD
    public Payment getPayment(int paymentId) throws SQLException {
        String query = "SELECT * FROM Payment WHERE PaymentId = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, paymentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int paymentIdNumber = rs.getInt("PaymentId");
                    int cardId = rs.getInt("CardId");
                    double amount = rs.getDouble("Amount");
                    int statusIndex = rs.getInt("PaymentStatus");
                    PaymentStatus paymentStatus = PaymentStatus.values()[statusIndex];
                    Date date = new Date(rs.getTimestamp("PaymentDate").getTime());
                    int userId = rs.getInt("UserId");

                    String cardQuery = "SELECT * FROM Card WHERE CardId = ?";
                    try (PreparedStatement cardStmt = conn.prepareStatement(cardQuery)) {
                        cardStmt.setInt(1, cardId);
                        try (ResultSet cardRs = cardStmt.executeQuery()) {
                            if (cardRs.next()) {
                                String name = cardRs.getString("Name");
                                String number = cardRs.getString("Number");
                                YearMonth expiry = YearMonth.from(cardRs.getDate("Expiry").toLocalDate());
                                String cvc = cardRs.getString("CVC");

                                Card card = new Card(cardId, name, number, expiry, cvc);

                                return new Payment(paymentIdNumber, amount, card, paymentStatus, date, userId);
                            }
                        }
                    }
                }
=======
                return new Payment(paymentId, Amount, Card, paymentStatus);
>>>>>>> origin/main
            }
        }
        return null;
    }

    public List<Payment> getAllPaymentsForUser(int userId) throws SQLException {
        List<Payment> payments = new ArrayList<>();
    
        String sql = "SELECT * FROM Payment WHERE UserId = ?";
    
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
    
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int paymentId = rs.getInt("PaymentId");
                    int cardId = rs.getInt("CardId");
                    double amount = rs.getDouble("Amount");
                    int statusIndex = rs.getInt("PaymentStatus");
                    PaymentStatus status = PaymentStatus.values()[statusIndex];
                    Date paymentDate = new Date(rs.getTimestamp("PaymentDate").getTime());
    
                    String cardQuery = "SELECT * FROM Card WHERE CardId = ?";
                    try (PreparedStatement cardStmt = conn.prepareStatement(cardQuery)) {
                        cardStmt.setInt(1, cardId);
                        try (ResultSet cardRs = cardStmt.executeQuery()) {
                            if (cardRs.next()) {
                                String name = cardRs.getString("Name");
                                String number = cardRs.getString("Number");
                                YearMonth expiry = YearMonth.from(cardRs.getDate("Expiry").toLocalDate());
                                String cvc = cardRs.getString("CVC");
    
                                Card card = new Card(cardId, name, number, expiry, cvc);
    
                                payments.add(new Payment(paymentId, amount, card, status, paymentDate, userId));
                            }
                        }
                    }
                }
            }
        }
    
        return payments;
    } 
    
    public void deletePayment(int paymentId) throws SQLException {
        String sql = "DELETE FROM Payment WHERE PaymentId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            stmt.executeUpdate();
        }
    }
    
    public List<Payment> searchPaymentsForUser(int userId, String statusStr, Date fromDate, Date toDate) throws SQLException {
        List<Payment> results = new ArrayList<>();
    
        StringBuilder sql = new StringBuilder("SELECT * FROM Payment WHERE UserId = ?");
        List<Object> params = new ArrayList<>();
        params.add(userId);
    
        Integer statusOrdinal = null;
        if (statusStr != null && !statusStr.isEmpty()) {
            try {
                PaymentStatus statusEnum = PaymentStatus.valueOf(statusStr.toUpperCase());
                statusOrdinal = statusEnum.ordinal();
                sql.append(" AND PaymentStatus = ?");
                params.add(statusOrdinal);
            } catch (IllegalArgumentException e) {
            }
        }
    
        if (fromDate != null) {
            sql.append(" AND PaymentDate >= ?");
            params.add(new java.sql.Date(fromDate.getTime()));
        }
        if (toDate != null) {
            sql.append(" AND PaymentDate <= ?");
            params.add(new java.sql.Date(toDate.getTime()));
        }
    
        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
    
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int paymentId = rs.getInt("PaymentId");
                    int cardId = rs.getInt("CardId");
                    double amount = rs.getDouble("Amount");
                    int statusIndex = rs.getInt("PaymentStatus");
                    PaymentStatus paymentStatus = PaymentStatus.values()[statusIndex];
                    Date paymentDate = new Date(rs.getTimestamp("PaymentDate").getTime());

                    Card card = null;
                    String cardQuery = "SELECT * FROM Card WHERE CardId = ?";
                    try (PreparedStatement cardStmt = conn.prepareStatement(cardQuery)) {
                        cardStmt.setInt(1, cardId);
                        try (ResultSet cardRs = cardStmt.executeQuery()) {
                            if (cardRs.next()) {
                                String name = cardRs.getString("Name");
                                String number = cardRs.getString("Number");
                                YearMonth expiry = YearMonth.from(cardRs.getDate("Expiry").toLocalDate());
                                String cvc = cardRs.getString("CVC");
                                card = new Card(cardId, name, number, expiry, cvc);
                            }
                        }
                    }
    
                    if (card != null) {
                        results.add(new Payment(paymentId, amount, card, paymentStatus, paymentDate, userId));
                    }
                }
            }
        }
    
        return results;
    }

    public Card getCardById(int cardId) throws SQLException {
        String sql = "SELECT cardId, Name, Number, Expiry, CVC FROM Card WHERE card_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("Name");
                    String number = rs.getString("Number");
                    YearMonth expiry = YearMonth.parse(rs.getString("Expiry"));
                    String cvc = rs.getString("CVC");
                    return new Card(cardId, name, number, expiry, cvc);
                }
            }
        }
        return null;
    }

    public int getCardOwner(int cardId) throws SQLException {
        String sql = "SELECT userId FROM Payment WHERE cardId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("UserId");
                }
            }
        }
        return -1;
    }

    public List<Card> getCardsForUser(int userId) throws SQLException {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT card_id, name, number, expiry, cvc FROM cards WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int cardId = rs.getInt("card_id");
                    String name = rs.getString("name");
                    String number = rs.getString("number");
                    String expiryStr = rs.getString("expiry");
                    String cvc = rs.getString("cvc");
                    YearMonth expiry = YearMonth.parse(expiryStr);
                    
                    Card card = new Card(cardId, name, number, expiry, cvc);
                    cards.add(card);
                }
            }
        }
        return cards;
    }

    public void updateCard(Card card, int userId) throws SQLException {
        String sql = "UPDATE cards SET name = ?, number = ?, expiry = ?, cvc = ? WHERE card_id = ? AND user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, card.getName());
            ps.setString(2, card.getNumber());
            ps.setString(3, card.getExpiry().toString()); // yyyy-MM format
            ps.setString(4, card.getCvc());
            ps.setInt(5, card.getCardId());
            ps.setInt(6, userId);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No card updated, card might not exist or not belong to user.");
            }
        }
    }

    public void deleteCard(int cardId, int userId) throws SQLException {
        String sql = "DELETE FROM cards WHERE card_id = ? AND user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ps.setInt(2, userId);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("No card deleted, card might not exist or not belong to user.");
            }
        }
    }

    public void close() throws SQLException {
        dbConnector.closeConnection();
    }
}