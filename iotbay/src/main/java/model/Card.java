package model;

import java.io.Serializable;
import java.time.YearMonth;

public class Card implements Serializable {
    private int cardId;
    private String name;
    private String number;
    private YearMonth expiry;
    private String cvc;

    public Card(int cardId, String name, String number, YearMonth expiry, String cvc) {
        this.cardId = cardId;
        this.name = name;
        this.number = number;
        this.expiry = expiry;
        this.cvc = cvc;
    }

    public int getCardId() {
        return cardId;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public YearMonth getExpiry() {
        return expiry;
    }

    public String getCvc() {
        return cvc;
    }
}
