package test;

import config.AppConfig;
import model.Admin;
import model.Librarian;
import model.Staff;
import model.Student;
import model.Teacher;
import model.User;
import service.AuthService;

public class BorrowPermissionTests {
    public static void main(String[] args) {
        AuthService auth = new AuthService();

        User librarian = new Librarian("LIB-002", "Lib User", "lib@x", "pass");
        librarian.setAccessLevel(AppConfig.ACCESS_LIBRARIAN);

        User admin = new Admin("ADM-001", "Admin User", "admin@x", "pass");
        admin.setAccessLevel(AppConfig.ACCESS_ADMIN);

        User student = new Student("STU-001", "Student User", "stu@x", "pass");
        student.setAccessLevel(AppConfig.ACCESS_STUDENT);

        User teacher = new Teacher("TEA-001", "Teacher User", "tea@x", "pass");
        teacher.setAccessLevel(AppConfig.ACCESS_TEACHER);

        User staff = new Staff("STA-001", "Staff User", "staff@x", "pass");
        staff.setAccessLevel(AppConfig.ACCESS_STAFF);

        if (!auth.canBorrowForOtherUser(librarian)) {
            throw new AssertionError("Librarian should be allowed to borrow on behalf of another user");
        }
        if (!auth.canBorrowForOtherUser(admin)) {
            throw new AssertionError("Admin should be allowed to borrow on behalf of another user");
        }
        if (auth.canBorrowForOtherUser(student)) {
            throw new AssertionError("Student should not be allowed to borrow on behalf of another user");
        }
        if (auth.canBorrowForOtherUser(teacher)) {
            throw new AssertionError("Teacher should not be allowed to borrow on behalf of another user");
        }
        if (auth.canBorrowForOtherUser(staff)) {
            throw new AssertionError("Staff should not be allowed to borrow on behalf of another user");
        }

        System.out.println("Borrow permission tests passed.");
    }
}
