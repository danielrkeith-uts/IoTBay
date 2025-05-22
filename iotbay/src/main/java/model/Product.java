package model;

import java.io.Serializable;

public class Product implements Serializable {
    private int productId;
    private String name;
    private String description;
    private double cost;
    private int stock;
    private String imageUrl;

    public Product(String name, String description, double cost, int stock, String imageUrl) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Product product = (Product) obj;

        return this.name.equals(product.name)
            && this.description.equals(product.description)
            && this.cost == product.cost
            && this.stock == product.stock;
    }
}
