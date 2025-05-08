package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import model.Enums.DeliveryStatus;

public class Delivery implements Serializable {
    private int deliveryId;
    private List<Order> orders;
    private Address source;
    private Address destination;
    private String courier;
    private int courierDeliveryId;
    private DeliveryStatus status;
    private Date estimatedDeliveryDate;
    private boolean finalized;

    public Delivery(int deliveryId, List<Order> orders, Address source, Address destination, String courier, int courierDeliveryId) {
        this.deliveryId = deliveryId;
        this.orders = orders;
        this.source = source;
        this.destination = destination;
        this.courier = courier;
        this.courierDeliveryId = courierDeliveryId;
        this.status = DeliveryStatus.PENDING;
        this.finalized = false;
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
    
    public DeliveryStatus getStatus() {
        return status;
    }
    
    public void setStatus(DeliveryStatus status) {
        this.status = status;
        // Update finalized status when status is changed
        this.finalized = (status == DeliveryStatus.FINALIZED);
    }
    
    public Date getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }
    
    public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }
    
    public boolean isFinalized() {
        return finalized;
    }
}