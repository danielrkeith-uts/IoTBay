package model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Cart implements Serializable {
    private List<ProductListEntry> productList;
    private Date lastUpdated;

    public Cart() {
        productList = new LinkedList<ProductListEntry>();
        lastUpdated = new Date();
    }

    public List<ProductListEntry> getProductList() {
        return productList;
    }

    public void addProduct(Product product) {
        productList.add(new ProductListEntry(product));
        lastUpdated = new Date();
    }

    public double totalCost() {
        double sum = 0;
        for (ProductListEntry productQuantity : productList) {
            sum += productQuantity.totalCost();
        }
        return sum;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
}
