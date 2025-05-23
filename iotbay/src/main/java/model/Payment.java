package model;

import java.io.Serializable;
import java.util.Date;

import model.Enums.PaymentStatus;

public class Payment implements Serializable {
    private int paymentId;
    private double amount;
    private Card card;
    private PaymentStatus paymentStatus;
    private Date date;
    private int userId;

<<<<<<< HEAD
    public Payment(double amount, Card card, PaymentStatus paymentStatus, Date date, int userId) {
        this.paymentId = 0;
=======
    public Payment(int paymentId, double amount, Card card, PaymentStatus paymentStatus) {
        this.paymentId = paymentId;
>>>>>>> origin/main
        this.amount = amount;
        this.card = card;
        this.paymentStatus = paymentStatus;
        this.date = date;
        this.userId = userId;
    }

    public Payment(int paymentId, double amount, Card card, PaymentStatus paymentStatus, Date date, int userId) {
        this.paymentId = 0;
        this.amount = amount;
        this.card = card;
        this.paymentStatus = paymentStatus;
        this.date = date;
        this.userId = userId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public int getPaymentId() {
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

    public Date getDate() { 
        return date; 
    }

    public int getUserId() {
        return userId;
    }
}
