package model;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    private int cartId;
    private List<ProductListEntry> productList;
    private Date lastUpdated;

    public Cart() {
        this.cartId = 999;
        productList = new ArrayList<ProductListEntry>();
        lastUpdated = new Date();
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public void setLastUpdated(Date newDate) {
        this.lastUpdated = newDate;
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
