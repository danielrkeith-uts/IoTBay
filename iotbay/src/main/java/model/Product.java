package model;

import java.io.Serializable;
import model.Enums.ProductType;

public class Product implements Serializable {
    private int productId;
    private String name;
    private String description;
    private ProductType type; 
    private double cost;
    private int stock;
    private String imageUrl;

    public Product(String name, String description, ProductType type, double cost, int stock, String imageUrl) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.cost = cost;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }
    public Product(String name, String description, ProductType type, double cost, int stock) {
        this(name, description, type, cost, stock, null); 
    }

    public String getName() {
        return name;
    }

    public int getProductId() {
        return productId;
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

    public ProductType getType() { 
        return type;
    }

    public void setType(ProductType type) { 
        this.type = type;
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
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Product product = (Product) obj;

        return this.name.equals(product.name)
            && this.description.equals(product.description)
            && this.cost == product.cost
            && this.stock == product.stock
            && ((this.type == null && product.type == null) || (this.type != null && this.type.equals(product.type)));
    }


    @Override
    public String toString() {
        return getName();
    }
}