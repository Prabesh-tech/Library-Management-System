package model;

import config.AppConfig;

/**
 * Teacher.java
 * Represents a teacher user with additional academic department metadata.
 */
public class Teacher extends User {

    private static final long serialVersionUID = 1L;

    private String department;
    private String teacherId;

    public Teacher(String userId, String name, String email, String password,
                   String phone, String address, String department, String teacherId) {
        super(userId, name, email, password, AppConfig.ROLE_TEACHER, phone, address);
        this.department = department;
        this.teacherId = teacherId;
    }

    public Teacher(String userId, String name, String email, String password) {
        this(userId, name, email, password, "", "", "General", userId);
    }

    @Override
    public String getRoleDescription() {
        return "Teacher – can borrow and reserve books, and recommend titles to students.";
    }

    @Override
    public String getDashboardTitle() {
        return "Teacher Dashboard – Welcome, " + name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return "Teacher{id='" + userId + "', name='" + name + "', department='" + department + "'}";
    }
}
