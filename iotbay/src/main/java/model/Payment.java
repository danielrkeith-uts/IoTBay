package model;

import java.io.Serializable;
import model.Enums.PaymentStatus;

public class Payment implements Serializable {
    private int paymentId;
    private int userId;
    private double amount;
    private Card card;
    private PaymentStatus paymentStatus;

    public Payment(int paymentId, int userId, double amount, Card card, PaymentStatus paymentStatus) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.amount = amount;
        this.card = card;
        this.paymentStatus = paymentStatus;
    }

    public int getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public Card getCard() {
        return card;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", amount=" + amount +
                ", card=" + card +
                ", paymentStatus=" + paymentStatus +
                ", userId=" + userId +
                '}';
    }
}
