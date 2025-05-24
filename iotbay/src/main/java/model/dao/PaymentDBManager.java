package model.dao;

import model.Payment;
import model.Card;
import model.Enums.PaymentStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.sqlite.SQLiteDataSource;

public class PaymentDBManager {
    private static final String URL = "jdbc:sqlite:database.db";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");
    private static DataSource dataSource;

    private final Connection externalConnection;

    static {
        try {
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(URL);
            dataSource = ds;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PaymentDBManager() {
        this.externalConnection = null;
    }

    public PaymentDBManager(Connection conn) {
        this.externalConnection = conn;
    }

    private Connection connect() throws SQLException {
        if (externalConnection != null) return externalConnection;
        return dataSource.getConnection();
    }

    public void insertPayment(Payment p) throws SQLException {
        String sql = "INSERT INTO Payment(UserId, CardId, Amount, PaymentStatus) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getUserId());
            ps.setInt(2, p.getCard().getCardId());
            ps.setDouble(3, p.getAmount());
            ps.setInt(4, p.getPaymentStatus().ordinal());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setPaymentId(rs.getInt(1));
                }
            }
        }
    }

    public int insertPaymentMethod(int userId, Card card) throws SQLException {
        String sql = "INSERT INTO Card(UserId, Name, Number, Expiry, CVC) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setString(2, card.getName());
            ps.setString(3, card.getNumber());
            ps.setString(4, card.getExpiry().format(DATE_FORMAT));
            ps.setString(5, card.getCvc());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public Card getPaymentMethod(int cardId) throws SQLException {
        String sql = "SELECT * FROM Card WHERE CardId = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Card(
                        rs.getInt("CardId"),
                        rs.getString("Name"),
                        rs.getString("Number"),
                        YearMonth.parse(rs.getString("Expiry"), DATE_FORMAT),
                        rs.getString("CVC")
                    );
                }
            }
        }
        return null;
    }

    public List<Card> getMethodsByUser(int userId) throws SQLException {
        List<Card> methods = new ArrayList<>();
        String sql = "SELECT * FROM Card WHERE UserId = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    methods.add(new Card(
                        rs.getInt("CardId"),
                        rs.getString("Name"),
                        rs.getString("Number"),
                        YearMonth.parse(rs.getString("Expiry"), DATE_FORMAT),
                        rs.getString("CVC")
                    ));
                }
            }
        }
        return methods;
    }

    public void updatePaymentMethod(Card card) throws SQLException {
        String sql = "UPDATE Card SET Name = ?, Number = ?, Expiry = ?, CVC = ? WHERE CardId = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, card.getName());
            ps.setString(2, card.getNumber());
            ps.setString(3, card.getExpiry().format(DATE_FORMAT));
            ps.setString(4, card.getCvc());
            ps.setInt(5, card.getCardId());
            ps.executeUpdate();
        }
    }

    public void deletePaymentMethod(int cardId) throws SQLException {
        String sql = "DELETE FROM Card WHERE CardId = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ps.executeUpdate();
        }
    }

    public Payment getPayment(int paymentId) throws SQLException {
        String sql = "SELECT p.PaymentId, p.UserId, p.CardId, p.Amount, p.PaymentStatus, " +
                    "c.Name, c.Number, c.Expiry, c.CVC " +
                    "FROM Payment p " +
                    "JOIN Card c ON p.CardId = c.CardId " +
                    "WHERE p.PaymentId = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Card card = new Card(
                        rs.getInt("CardId"),
                        rs.getString("Name"),
                        rs.getString("Number"),
                        YearMonth.parse(rs.getString("Expiry"), DATE_FORMAT),
                        rs.getString("CVC")
                    );
                    return new Payment(
                        rs.getInt("PaymentId"),
                        rs.getInt("UserId"),
                        rs.getDouble("Amount"),
                        card,
                        PaymentStatus.values()[rs.getInt("PaymentStatus")]
                    );
                }
            }
        }
        return null;
    }

    public List<Payment> getAllPayments() throws SQLException {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT p.PaymentId, p.UserId, p.CardId, p.Amount, p.PaymentStatus, " +
                    "c.Name, c.Number, c.Expiry, c.CVC " +
                    "FROM Payment p " +
                    "LEFT JOIN Card c ON p.CardId = c.CardId";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Card card = null;
                String cardName = rs.getString("Name");
                if (cardName != null) {
                    card = new Card(
                        rs.getInt("CardId"),
                        cardName,
                        rs.getString("Number"),
                        YearMonth.parse(rs.getString("Expiry"), DATE_FORMAT),
                        rs.getString("CVC")
                    );
                }
                list.add(new Payment(
                    rs.getInt("PaymentId"),
                    rs.getInt("UserId"),
                    rs.getDouble("Amount"),
                    card,
                    PaymentStatus.values()[rs.getInt("PaymentStatus")]
                ));
            }
        }
        return list;
    }

    public void updatePayment(Payment p) throws SQLException {
        String sql = "UPDATE Payment SET CardId = ?, Amount = ?, PaymentStatus = ? WHERE PaymentId = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getCard().getCardId());
            ps.setDouble(2, p.getAmount());
            ps.setInt(3, p.getPaymentStatus().ordinal());
            ps.setInt(4, p.getPaymentId());
            ps.executeUpdate();
        }
    }

    public void deletePayment(int paymentId) throws SQLException {
        String sql = "DELETE FROM Payment WHERE PaymentId = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            ps.executeUpdate();
        }
    }
}
