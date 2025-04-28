package model;

public class Staff extends User {
    private int staffCardId;
    public Staff(int userId, String firstName, String lastName, String email, String phone, String password, int staffCardId) {
        super(userId, firstName, lastName, email, phone, password);

        this.staffCardId = staffCardId;
    }

    public int getStaffCardId() {
        return staffCardId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Staff staff = (Staff) obj;

        return super.equals(staff) && this.staffCardId == staff.staffCardId;
    }
}
