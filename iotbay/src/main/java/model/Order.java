package model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private List<ProductListEntry> productList;
    private Payment payment;
    private Address address;

    public Order(List<ProductListEntry> productList, Payment payment, Address address) {
        this.productList = productList;
        this.payment = payment;
        this.address = address;
    }

    public List<ProductListEntry> getProductList() {
        return productList;
    }

    public Payment getPayment() {
        return payment;
    }

    public Address getAddress() {
        return address;
    }
}
