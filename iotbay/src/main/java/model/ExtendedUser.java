package model;

public class ExtendedUser extends User {
    private boolean isCustomer;
    private Customer.Type customerType;
    private boolean isStaff;
    private int staffCardId;
    private boolean isAdmin;

    public ExtendedUser(int userId, String firstName, String lastName, String email, String phone, String password) {
        super(userId, firstName, lastName, email, phone, password, null); 
    }


    public boolean isCustomer() {
        return isCustomer;
    }

    public void setCustomer(boolean customer) {
        isCustomer = customer;
    }

    public Customer.Type getCustomerType() {
        return customerType;
    }

    public void setCustomerType(Customer.Type customerType) {
        this.customerType = customerType;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean staff) {
        isStaff = staff;
    }

    public int getStaffCardId() {
        return staffCardId;
    }

    public void setStaffCardId(int staffCardId) {
        this.staffCardId = staffCardId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
