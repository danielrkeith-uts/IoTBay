package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public abstract class User implements Serializable {
    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private boolean deactivated;
    private List<ApplicationAccessLog> applicationAccessLogs;
    private Role role; 

    public User(int userId, String firstName, String lastName, String email, String phone, String password, Role role) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.deactivated = false;
        this.role = role;
        this.applicationAccessLogs = new LinkedList<ApplicationAccessLog>();
    }

    public enum Role {
        ADMIN,
        STAFF,
        CUSTOMER
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isDeactivated() {
        return deactivated;
    }

    public void setDeactivated(boolean deactivated) {
        this.deactivated = deactivated;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ApplicationAccessLog> getApplicationAccessLogs() {
        return applicationAccessLogs;
    }

    public void setApplicationAccessLogs(List<ApplicationAccessLog> applicationAccessLogs) {
        this.applicationAccessLogs = applicationAccessLogs;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User user = (User) obj;

        return this.userId == user.userId
            && this.firstName.equals(user.firstName)
            && this.lastName.equals(user.lastName)
            && this.email.equals(user.email)
            && this.phone.equals(user.phone)
            && this.password.equals(user.password)
            && this.deactivated == user.deactivated
           && this.role == user.role;
    }
}
