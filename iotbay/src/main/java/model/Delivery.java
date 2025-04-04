package model;

import java.io.Serializable;
import java.util.List;

public class Delivery implements Serializable {
    private List<Order> orders;
    private Address source;
    private Address destination;
    private String courier;
    private String courierDeliveryId;

    public Delivery(List<Order> orders, Address source, Address destination, String courier, String courierDeliveryId) {
        this.orders = orders;
        this.source = source;
        this.destination = destination;
        this.courier = courier;
        this.courierDeliveryId = courierDeliveryId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Address getSource() {
        return source;
    }

    public Address getDestination() {
        return destination;
    }

    public String getCourier() {
        return courier;
    }

    public String getCourierDeliveryId() {
        return courierDeliveryId;
    }
}
