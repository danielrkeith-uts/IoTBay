package model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class User implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private List<ApplicationAccessLog> applicationAccessLogs;

    public User(String firstName, String lastName, String email, String phone, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;

        this.applicationAccessLogs = new LinkedList<ApplicationAccessLog>();
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

    public boolean checkPassword(String password) {
        return password == this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ApplicationAccessLog> getApplicationAccessLogs() {
        return applicationAccessLogs;
    }
}
