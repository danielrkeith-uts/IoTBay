package model;

import java.io.Serializable;
import java.util.Date;
import model.Enums.PaymentStatus;

public class Payment implements Serializable {
    private final int paymentId;
    private final double amount;
    private final Card card;
    private final PaymentStatus paymentStatus;
    private final Date date;
    private final int userId;

    public Payment(double amount, Card card, PaymentStatus paymentStatus, Date date, int userId) {
        this.paymentId = 0;
        this.amount = amount;
        this.card = card;
        this.paymentStatus = paymentStatus;
        this.date = (date == null) ? null : new Date(date.getTime());
        this.userId = userId;
    }

    public Payment(int paymentId, double amount, Card card, PaymentStatus paymentStatus, Date date, int userId) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.card = card;
        this.paymentStatus = paymentStatus;
        this.date = (date == null) ? null : new Date(date.getTime());
        this.userId = userId;
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
        return (date == null) ? null : new Date(date.getTime());
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", amount=" + amount +
                ", card=" + card +
                ", paymentStatus=" + paymentStatus +
                ", date=" + date +
                ", userId=" + userId +
                '}';
    }
}
