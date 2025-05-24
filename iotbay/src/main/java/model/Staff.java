package model;

public class Staff extends User {
    private int staffCardId;
    private boolean isAdmin;

    public Staff(int userId, String firstName, String lastName, String email, String phone, String password, int deactivated, boolean isAdmin) {
        super(userId, firstName, lastName, email, phone, password, Role.STAFF);

        this.staffCardId = deactivated;
        this.isAdmin = isAdmin;

    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }


    public int getStaffCardId() {
        return staffCardId;
    }

    public void setStaffCardId(int staffCardId) {
        this.staffCardId = staffCardId;
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
