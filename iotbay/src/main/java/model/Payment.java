package model;

import java.io.Serializable;

import model.Enums.PaymentStatus;

public class Payment implements Serializable {
    private int paymentId;
    private double amount;
    private Card card;
    private PaymentStatus paymentStatus;

    public Payment(int paymentId, double amount, Card card, PaymentStatus paymentStatus) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.card = card;
        this.paymentStatus = paymentStatus;
    }

    public double getPaymentId() {
        return paymentId;
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
}
