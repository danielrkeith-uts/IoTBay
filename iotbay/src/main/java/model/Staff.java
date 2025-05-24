package model;

public class Staff extends User {
    private int staffCardId;
    private boolean isAdmin;

    private boolean isAdmin;
    private String position;

    public Staff(int userId, String firstName, String lastName, String email, String phone, String password, int deactivated, boolean isAdmin, boolean isAdmin, String position) {
        super(userId, firstName, lastName, email, phone, password, Role.STAFF, isAdmin ? Role.ADMIN : Role.STAFF);

        this.staffCardId = deactivated;
        this.isAdmin = isAdmin;

    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
        this.isAdmin = isAdmin;
        this.position = position;
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

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

        return super.equals(staff)
            && this.staffCardId == staff.staffCardId
            && this.isAdmin == staff.isAdmin
            && this.position.equals(staff.position);
    }
}

