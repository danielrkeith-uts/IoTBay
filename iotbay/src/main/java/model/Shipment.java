package model;

import java.io.Serializable;
import java.util.Date;

public class Shipment implements Serializable {
    private int shipmentId;
    private Order order;
    private Address address;
    private String method;
    private Date shipmentDate;

    public Shipment(int shipmentId, Order order, Address address, String method, Date shipmentDate) {
        this.shipmentId = shipmentId;
        this.order = order;
        this.address = address;
        this.method = method;
        this.shipmentDate = shipmentDate;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public Order getOrder() {
        return order;
    }

    public Address getAddress() {
        return address;
    }

    public String getMethod() {
        return method;
    }

    public Date getShipmentDate() {
        return shipmentDate;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setShipmentDate(Date shipmentDate) {
        this.shipmentDate = shipmentDate;
    }
}