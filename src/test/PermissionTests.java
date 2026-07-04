package test;

import service.AuthService;
import model.User;
import model.Admin;
import model.Librarian;
import model.Student;
import config.AppConfig;

public class PermissionTests {
    public static void main(String[] args) {
        AuthService auth = new AuthService();

        User admin = new Admin("ADM-001","Admin User","admin@x","pass");
        admin.setAccessLevel(AppConfig.ACCESS_ADMIN);

        User librarian = new Librarian("LIB-002","Lib User","lib@x","pass");
        librarian.setAccessLevel(AppConfig.ACCESS_LIBRARIAN);

        User student = new Student("STU-001","Stu User","stu@x","pass");
        student.setAccessLevel(AppConfig.ACCESS_STUDENT);

        System.out.println("Admin can manage_users: " + auth.hasPermission(admin, "manage_users"));
        System.out.println("Librarian can manage_books: " + auth.hasPermission(librarian, "manage_books"));
        System.out.println("Librarian can manage_users: " + auth.hasPermission(librarian, "manage_users"));
        System.out.println("Student can borrow_books: " + auth.hasPermission(student, "borrow_books"));
        System.out.println("Student cannot manage_books: " + auth.hasPermission(student, "manage_books"));

        if (!auth.hasPermission(librarian, "manage_users")) {
            throw new IllegalStateException("Librarian should be allowed to manage users");
        }
    }
}
