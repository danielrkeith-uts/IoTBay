package model;

import java.util.LinkedList;
import java.util.List;

public class Customer extends User {
    private Cart cart;
    private Type type;
    private List<Order> orders;
    private List<Shipment> shipments;  

    public Customer(int userId, String firstName, String lastName, String email, String phone, String password, Type type) {
        super(userId, firstName, lastName, email, phone, password, Role.CUSTOMER);
       
        this.type = type;

        cart = new Cart();
        orders = new LinkedList<Order>();
        shipments = new LinkedList<Shipment>(); 
    }

    public enum Type {
        COMPANY,
        INDIVIDUAL
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void clearCart() {
        cart = new Cart();
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void placeOrder(Order order) {
        orders.add(order);
    }
    
    public List<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(List<Shipment> shipments) {
        this.shipments = shipments;
    }

    public void addShipment(Shipment shipment) {
        shipments.add(shipment);
    }
    
}