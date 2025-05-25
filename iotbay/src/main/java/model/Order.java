package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import model.Enums.*;

public class Order implements Serializable {
    private int orderId;
    private List<ProductListEntry> productList;
    private Payment payment;
    private Timestamp datePlaced;
    private OrderStatus status;
    private int userId;
    private int cartId;
    private int paymentId;

    public Order(int orderId, List<ProductListEntry> productList, Payment payment, Timestamp datePlaced, OrderStatus status) {
        this.orderId = orderId;
        this.productList = productList;
        this.payment = payment;
        this.datePlaced = datePlaced;
        this.status = status;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public List<ProductListEntry> getProductList() { return productList; }
    public void setProductList(List<ProductListEntry> productList) { this.productList = productList; }


    public Payment getPayment() { return payment; }
    public void setPayment(Payment payment) { this.payment = payment; }


    public Timestamp getDatePlaced() { return datePlaced; }
    public void setDatePlaced(Timestamp datePlaced) { this.datePlaced = datePlaced; }


    public OrderStatus getOrderStatus() { return status; }
    public void setOrderStatus(OrderStatus status) { this.status = status; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Order order = (Order) obj;

        return this.orderId == order.orderId
            && checkListEqual(this.productList, order.productList)
            && this.payment.equals(order.payment)
            && this.datePlaced.equals(order.datePlaced)
            && this.status.equals(order.status);
    }

    private boolean checkListEqual(List<ProductListEntry> list1, List<ProductListEntry> list2) {
        for(ProductListEntry entry : list1) {
            if (!list2.contains(entry)){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "{" +
            " orderId='" + getOrderId() + "'" +
            ", productList='" + getProductList() + "'" +
            ", payment='" + getPayment() + "'" +
            ", datePlaced='" + getDatePlaced() + "'" +
            ", status='" + getOrderStatus() + "'" +
            "}";
    }
    
}