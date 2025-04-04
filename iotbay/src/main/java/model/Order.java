package model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private List<ProductListEntry> productList;
    private Payment payment;

    public Order(List<ProductListEntry> productList, Payment payment) {
        this.productList = productList;
        this.payment = payment;
    }

    public List<ProductListEntry> getProductList() {
        return productList;
    }

    public Payment getPayment() {
        return payment;
    }
}
