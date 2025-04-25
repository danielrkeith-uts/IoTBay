package model;

import java.io.Serializable;
import java.util.List;

public class Delivery implements Serializable {
    private int deliveryId;
    private List<Order> orders;
    private Address source;
    private Address destination;
    private String courier;
    private int courierDeliveryId;

    public Delivery(int deliveryId, List<Order> orders, Address source, Address destination, String courier, int courierDeliveryId) {
        this.deliveryId =deliveryId;
        this.orders = orders;
        this.source = source;
        this.destination = destination;
        this.courier = courier;
        this.courierDeliveryId = courierDeliveryId;
    }

    public int getDeliveryId() {
        return deliveryId;
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

    public int getCourierDeliveryId() {
        return courierDeliveryId;
    }

    public void setCourierDeliveryId(int courierDeliveryId) {
        this.courierDeliveryId = courierDeliveryId;
    }
}
