package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
    private int orderId;
    private List<ProductListEntry> productList;
    private Payment payment;
    private Date datePlaced;
    private Delivery delivery;
    private int userId;

    // Constructor without orderId for backward compatibility
    public Order(List<ProductListEntry> productList, Payment payment, Date datePlaced) {
        this.productList = productList;
        this.payment = payment;
        this.datePlaced = datePlaced;
    }

    // Constructor with orderId for new implementations
    public Order(int orderId, List<ProductListEntry> productList, Payment payment, Date datePlaced) {
        this.orderId = orderId;
        this.productList = productList;
        this.payment = payment;
        this.datePlaced = datePlaced;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public List<ProductListEntry> getProductList() {
        return productList;
    }

    public Payment getPayment() {
        return payment;
    }

    public Date getDatePlaced() {
        return datePlaced;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}