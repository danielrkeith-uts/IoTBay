package model;

import java.util.LinkedList;
import java.util.List;

public class Customer extends User {
    private Cart cart;
    private List<Order> orders;

    public Customer(int userId, String firstName, String lastName, String email, String phone, String password) {
        super(userId, firstName, lastName, email, phone, password);

        cart = new Cart();
        orders = new LinkedList<Order>();
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
}
