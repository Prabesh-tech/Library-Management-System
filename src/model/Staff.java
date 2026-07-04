package model;

import config.AppConfig;

/**
 * Staff.java
 * Represents a staff member with support responsibilities.
 */
public class Staff extends User {

    private static final long serialVersionUID = 1L;

    private String department;
    private String staffId;

    public Staff(String userId, String name, String email, String password,
                 String phone, String address, String department, String staffId) {
        super(userId, name, email, password, AppConfig.ROLE_STAFF, phone, address);
        this.department = department;
        this.staffId = staffId;
    }

    public Staff(String userId, String name, String email, String password) {
        this(userId, name, email, password, "", "", "Support", userId);
    }

    @Override
    public String getRoleDescription() {
        return "Staff – assists with library operations and can access basic system features.";
    }

    @Override
    public String getDashboardTitle() {
        return "Staff Dashboard – Welcome, " + name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    @Override
    public String toString() {
        return "Staff{id='" + userId + "', name='" + name + "', department='" + department + "'}";
    }
}
