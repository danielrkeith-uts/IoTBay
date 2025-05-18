package model;

import java.io.Serializable;

public class IoTDevice implements Serializable{
    private String name;
    private String type;
    private double unitPrice;
    private int stock;

    public IoTDevice(String name, String type, double unitPrice, int stock) {
        this.name = name;
        this.type = type;
        this.unitPrice = unitPrice;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
