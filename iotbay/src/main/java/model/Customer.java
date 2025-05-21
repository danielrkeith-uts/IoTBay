package model;

import java.util.LinkedList;
import java.util.List;
import model.Shipment;

public class Customer extends User {
    private Cart cart;
    private List<Order> orders;
    private List<Shipment> shipments;   // i added this

    public Customer(int userId, String firstName, String lastName, String email, String phone, String password) {
        super(userId, firstName, lastName, email, phone, password);

        cart = new Cart();
        orders = new LinkedList<Order>();
        shipments = new LinkedList<Shipment>();   // i added this
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
    
    //new methods
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