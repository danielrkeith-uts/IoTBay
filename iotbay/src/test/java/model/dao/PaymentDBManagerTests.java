package model.dao;

import model.Payment;
import model.Card;
import model.Enums.PaymentStatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import org.junit.Assert;
import org.junit.Test;

public class PaymentDBManagerTests {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");
    private final Connection conn;
    private final PaymentDBManager paymentDBManager;

    public PaymentDBManagerTests() throws ClassNotFoundException, SQLException {
        this.conn = new DBConnector().openConnection();
        conn.setAutoCommit(false);
        this.paymentDBManager = new PaymentDBManager(conn);
    }

    @Test
    public void testGetPayment() {
        try {
            Payment payment = paymentDBManager.getPayment(1);
            Assert.assertNotNull(payment);
            Assert.assertEquals(1, payment.getPaymentId());
            Assert.assertEquals(1, payment.getUserId());
            Assert.assertEquals(23.45, payment.getAmount(), 0.01);
            Assert.assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus());

            Card card = payment.getCard();
            Assert.assertNotNull(card);
            Assert.assertEquals(1, card.getCardId());
            Assert.assertEquals("John Smith", card.getName());
            Assert.assertEquals("123456789", card.getNumber());
            Assert.assertEquals(YearMonth.parse("2026-08", DATE_FORMAT), card.getExpiry());
            Assert.assertEquals("123", card.getCvc());
        } catch (SQLException e) {
            Assert.fail("SQLException: " + e.getMessage());
        } finally {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.err.println("Rollback failed: " + e.getMessage());
            }
        }
    }
}
