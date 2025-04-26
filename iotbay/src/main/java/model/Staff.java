package model;

public class Staff extends User {
    private int staffCardId;
    public Staff(String firstName, String lastName, String email, String phone, String password, int staffCardId) {
        super(firstName, lastName, email, phone, password);

        this.staffCardId = staffCardId;
    }

    public int getStaffCardId() {
        return staffCardId;
    }
}
