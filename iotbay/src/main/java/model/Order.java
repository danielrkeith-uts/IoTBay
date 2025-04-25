package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
    private List<ProductListEntry> productList;
    private Payment payment;
    private Delivery delivery;
    private Date datePlaced;

    public Order(List<ProductListEntry> productList, Payment payment, Date datePlaced) {
        this.productList = productList;
        this.payment = payment;
        this.datePlaced = datePlaced;
    }

    public void setDelivery(Delivery delivery) {
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

    public Date getDatePlaced() {
        return datePlaced;
    }
}
