package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Cart implements Serializable {
    private List<ProductQuantity> productQuantities;

    public Cart() {
        productQuantities = new LinkedList<ProductQuantity>();
    }

    public List<ProductQuantity> getProductQuantities() {
        return productQuantities;
    }

    public void addProduct(Product product) {
        productQuantities.add(new ProductQuantity(product));
    }

    public double totalCost() {
        double sum = 0;
        for (ProductQuantity productQuantity : productQuantities) {
            sum += productQuantity.totalCost();
        }
        return sum;
    }
}
