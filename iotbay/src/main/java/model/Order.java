package model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private List<ProductListEntry> productList;
    private Payment payment;
    private Delivery delivery;

    public Order(List<ProductListEntry> productList, Payment payment, Delivery delivery) {
        this.productList = productList;
        this.payment = payment;
        this.delivery = delivery;
    }

    public List<ProductListEntry> getProductList() {
        return productList;
    }

    public Payment getPayment() {
        return payment;
    }

    public Delivery getDelivery() {
        return delivery;
    }
}
