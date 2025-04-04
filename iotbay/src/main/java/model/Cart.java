package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Cart implements Serializable {
    private List<ProductListEntry> productList;

    public Cart() {
        productList = new LinkedList<ProductListEntry>();
    }

    public List<ProductListEntry> getProductList() {
        return productList;
    }

    public void addProduct(Product product) {
        productList.add(new ProductListEntry(product));
    }

    public double totalCost() {
        double sum = 0;
        for (ProductListEntry productQuantity : productList) {
            sum += productQuantity.totalCost();
        }
        return sum;
    }
}
