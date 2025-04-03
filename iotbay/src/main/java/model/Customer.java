package model;

public class Customer extends User {
    private Cart cart;

    public Customer(String firstName, String lastName, String email, String phone, String password) {
        super(firstName, lastName, email, phone, password);
        cart = new Cart();
    }

    public Cart getCart() {
        return cart;
    }

    public void clearCart() {
        cart = new Cart();
    }
}
