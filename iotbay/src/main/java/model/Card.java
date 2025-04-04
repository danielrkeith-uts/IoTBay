package model;

import java.io.Serializable;
import java.time.YearMonth;

public class Card implements Serializable {
    private String name;
    private String number;
    private YearMonth expiry;
    private String cvc;

    public Card(String name, String number, YearMonth expiry, String cvc) {
        this.name = name;
        this.number = number;
        this.expiry = expiry;
        this.cvc = cvc;
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
