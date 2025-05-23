package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    private int cartId;
    private List<ProductListEntry> productList;
    private Timestamp lastUpdated;

    public Cart() {
        productList = new ArrayList<ProductListEntry>();
        Date now = new Date();
        lastUpdated = new java.sql.Timestamp(now.getTime());
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public void setLastUpdated(Timestamp newDate) {
        this.lastUpdated = newDate;
    }

    public List<ProductListEntry> getProductList() {
        return productList;
    }

    public void addProduct(Product product, int quantity) {
        for (ProductListEntry item : productList) {
            if (item.getProduct().getName().equals(product.getName())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        // If product not found, add new entry
        productList.add(new ProductListEntry(product, quantity));
        Date now = new Date();
        lastUpdated = new java.sql.Timestamp(now.getTime());
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
