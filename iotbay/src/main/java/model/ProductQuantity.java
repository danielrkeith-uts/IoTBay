package model;

import java.io.Serializable;

public class ProductQuantity implements Serializable {
    private Product product;
    private int quantity;

    public ProductQuantity(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ProductQuantity(Product product) {
        this(product, 1);
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double totalCost() {
        return product.getCost() * quantity;
    }
}
