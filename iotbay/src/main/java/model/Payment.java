package model;

import java.io.Serializable;

import model.Enums.PaymentStatus;

public class Payment implements Serializable {
    private int paymentId;
    private double amount;
    private Card card;
    private PaymentStatus paymentStatus;

    public Payment(double amount, Card card, PaymentStatus paymentStatus) {
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
