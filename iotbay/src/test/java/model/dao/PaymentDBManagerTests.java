package model.dao;

import java.sql.SQLException;
import java.time.YearMonth;

import org.junit.Assert;
import org.junit.Test;
import model.*;
import model.Enums.PaymentStatus;

public class PaymentDBManagerTests {
    PaymentDBManager paymentDBManager;

    public PaymentDBManagerTests() throws ClassNotFoundException, SQLException {
        paymentDBManager = new PaymentDBManager(new DBConnector().openConnection());
    }

    @Test
    public void testGetPayment() {
        Payment payment;
        try {
            payment = paymentDBManager.getPayment(1);
        } catch (SQLException e) {
            Assert.fail();
            return;
        }

        Card card = payment.getCard();

        Assert.assertEquals(23.45, payment.getAmount(), 0.01);
        Assert.assertEquals(PaymentStatus.PENDING, payment.getPaymentStatus());
        
        //Check Card fields
        Assert.assertEquals("John Smith", card.getName());
        Assert.assertEquals("123456789", card.getNumber());
        Assert.assertEquals(YearMonth.of(2026, 8), card.getExpiry());
        Assert.assertEquals("123", card.getCvc());
    }
    
}
