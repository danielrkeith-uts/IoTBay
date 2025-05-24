package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public abstract class User implements Serializable {
    public enum Role {
        CUSTOMER,
        STAFF,
        ADMIN
    }

    private int userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private boolean deactivated = false;
    private Role role;
    private List<ApplicationAccessLog> applicationAccessLogs;

    public User(int userId, String firstName, String lastName, String email, String phone, String password, Role role) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.deactivated = false;
        this.role = role;
        this.applicationAccessLogs = new LinkedList<>();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String candidate) {
        return password.equals(candidate);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDeactivated() {
        return deactivated;
    }

    public void setDeactivated(boolean deactivated) {
        this.deactivated = deactivated;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<ApplicationAccessLog> getApplicationAccessLogs() {
        return applicationAccessLogs;
    }

    public void setApplicationAccessLogs(List<ApplicationAccessLog> logs) {
        this.applicationAccessLogs = logs;
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
        return userId == user.userId
            && deactivated == user.deactivated
            && role == user.role
            && firstName.equals(user.firstName)
            && lastName.equals(user.lastName)
            && email.equals(user.email)
            && phone.equals(user.phone)
            && password.equals(user.password);
    }
}

