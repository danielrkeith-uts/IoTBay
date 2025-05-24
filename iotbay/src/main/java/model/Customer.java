package model;

import java.util.LinkedList;
import java.util.List;

public class Customer extends User {
    private Cart cart;
    private List<Order> orders;
    private List<Shipment> shipments;  

    public Customer(
        int userId,
        String firstName,
        String lastName,
        String email,
        String phone,
        String password
    ) {
        super(
            userId,
            firstName,
            lastName,
            email,
            phone,
            password,
            Role.CUSTOMER
        );
        this.cart      = new Cart();
        this.orders    = new LinkedList<>();
        this.shipments = new LinkedList<>();
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void clearCart() {
        this.cart = new Cart();
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void placeOrder(Order order) {
        this.orders.add(order);
    }

    public List<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(List<Shipment> shipments) {
        this.shipments = shipments;
    }

    public void addShipment(Shipment shipment) {
        this.shipments.add(shipment);
    }
}

