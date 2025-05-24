package model;

import java.util.Objects;

public class Staff extends User {
    private int staffCardId;
    private boolean isAdmin;
    private String position;

    public Staff(
        int userId,
        String firstName,
        String lastName,
        String email,
        String phone,
        String password,
        int staffCardId,
        boolean isAdmin,
        String position
    ) {
        super(
            userId,
            firstName,
            lastName,
            email,
            phone,
            password,
            isAdmin ? Role.ADMIN : Role.STAFF
        );
        this.staffCardId = staffCardId;
        this.isAdmin     = isAdmin;
        this.position    = position;
    }

    public Staff(
        int userId,
        String firstName,
        String lastName,
        String email,
        String phone,
        String password,
        int staffCardId,
        String position
    ) {
        this(
            userId,
            firstName,
            lastName,
            email,
            phone,
            password,
            staffCardId,
            false,
            position
        );
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Staff)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Staff that = (Staff) o;
        return staffCardId == that.staffCardId
            && isAdmin     == that.isAdmin
            && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            super.hashCode(),
            staffCardId,
            isAdmin,
            position
        );
    }
}

