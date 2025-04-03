package model;

public class Staff extends User {
    private String staffId;
    public Staff(String firstName, String lastName, String email, String phone, String password, String staffId) {
        super(firstName, lastName, email, phone, password);

        this.staffId = staffId;
    }

    public String getStaffId() {
        return staffId;
    }
}
