package model;

public class Staff extends User {
    private String staffCardId;
    public Staff(String firstName, String lastName, String email, String phone, String password, String staffCardId) {
        super(firstName, lastName, email, phone, password);

        this.staffCardId = staffCardId;
    }

    public String getStaffCardId() {
        return staffCardId;
    }
}
