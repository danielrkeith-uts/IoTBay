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
        String sql = "INSERT INTO Payment (Amount, CardId, PaymentStatus, PaymentDate, UserId) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDouble(1, payment.getAmount());
            stmt.setInt(2, payment.getCard().getCardId());
            stmt.setString(3, payment.getPaymentStatus().name());
            stmt.setDate(4, new java.sql.Date(payment.getDate().getTime()));
            stmt.setInt(5, payment.getUserId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating payment failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating payment failed, no ID obtained.");
                }
            }
        }
    }

    public Payment getPayment(int paymentId) throws SQLException {
        String query = "SELECT * FROM Payment WHERE PaymentId = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, paymentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int paymentIdNumber = rs.getInt("PaymentId");
                    int cardId = rs.getInt("CardId");
                    double amount = rs.getDouble("Amount");
                    int statusInt = rs.getInt("PaymentStatus");
                    PaymentStatus paymentStatus = PaymentStatus.fromInt(statusInt);

                    Date date = rs.getDate("PaymentDate");
                    int userId = rs.getInt("UserId");

                    Card card = getCardById(cardId);
                    if (card == null) {
                        throw new SQLException("Associated card not found for CardId " + cardId);
                    }

                    return new Payment(paymentIdNumber, amount, card, paymentStatus, date, userId);
                }
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
                    int statusInt = rs.getInt("PaymentStatus");
                    PaymentStatus paymentStatus = PaymentStatus.fromInt(statusInt);

                    Date paymentDate = rs.getDate("PaymentDate");

                    Card card = getCardById(cardId);
                    if (card != null) {
                        payments.add(new Payment(paymentId, amount, card, paymentStatus, paymentDate, userId));
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

        if (statusStr != null && !statusStr.isEmpty()) {
            try {
                PaymentStatus.valueOf(statusStr.toUpperCase()); 
                sql.append(" AND PaymentStatus = ?");
                params.add(statusStr.toUpperCase());
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
                    String paymentStatusStr = rs.getString("PaymentStatus");
                    PaymentStatus paymentStatus = PaymentStatus.valueOf(paymentStatusStr);
                    Date paymentDate = rs.getDate("PaymentDate");

                    Card card = getCardById(cardId);
                    if (card != null) {
                        results.add(new Payment(paymentId, amount, card, paymentStatus, paymentDate, userId));
                    }
                }
            }
        }
        return results;
    }

    public Card getCardById(int cardId) throws SQLException {
        String sql = "SELECT CardId, Name, Number, Expiry, CVC FROM Card WHERE CardId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("Name");
                    String number = rs.getString("Number");
                    Date expiryDate = rs.getDate("Expiry");
                    YearMonth expiry = YearMonth.from(expiryDate.toLocalDate());
                    String cvc = rs.getString("CVC");
                    return new Card(cardId, name, number, expiry, cvc);
                }
            }
        }
        return null;
    }

    public int getCardOwner(int cardId) throws SQLException {
        String sql = "SELECT UserId FROM Payment WHERE CardId = ?";
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
        String sql = "SELECT CardId, Name, Number, Expiry, CVC FROM Card WHERE UserId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int cardId = rs.getInt("CardId");
                    String name = rs.getString("Name");
                    String number = rs.getString("Number");
                    Date expiryDate = rs.getDate("Expiry");
                    YearMonth expiry = YearMonth.from(expiryDate.toLocalDate());
                    String cvc = rs.getString("CVC");

                    Card card = new Card(cardId, name, number, expiry, cvc);
                    cards.add(card);
                }
            }
        }
        return cards;
    }

    public void updateCard(Card card, int userId) throws SQLException {
        String sql = "UPDATE Card SET Name = ?, Number = ?, Expiry = ?, CVC = ? WHERE CardId = ? AND UserId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, card.getName());
            ps.setString(2, card.getNumber());
            ps.setDate(3, java.sql.Date.valueOf(card.getExpiry().atDay(1))); // Store expiry as first day of month
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
        String sql = "DELETE FROM Card WHERE CardId = ? AND UserId = ?";
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
        if (dbConnector != null) {
            dbConnector.closeConnection();
        } else if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
