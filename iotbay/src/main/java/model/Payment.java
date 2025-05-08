package model;

import java.io.Serializable;
import java.time.LocalDate;

import model.Enums.PaymentStatus;

public class Payment implements Serializable {
    private double amount;
    private Card card;
    private PaymentStatus paymentStatus;
    private int orderId;
    private LocalDate date;

    public Payment(double amount, Card card, PaymentStatus paymentStatus, int orderId, LocalDate date) {
        this.amount = amount;
        this.card = card;
        this.paymentStatus = paymentStatus;
        this.orderId = orderId;
        this.date = date;
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

    public int getOrderId() {
        return orderId;
    }

    public LocalDate getDate() {
        return date;
    }


}
