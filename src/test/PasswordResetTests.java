package test;

import controller.UserController;
import exception.InvalidInputException;
import exception.UserNotFoundException;
import model.Admin;
import util.DataManager;

public class PasswordResetTests {
    public static void main(String[] args) {
        DataManager.getInstance().getUserRepository().save(new Admin("TEST-USER", "Test Admin", "test-admin@example.com", "pass123"));
        UserController controller = new UserController();

        try {
            controller.sendResetCode("TEST-USER");
            throw new AssertionError("Library ID should not be accepted for password reset");
        } catch (InvalidInputException expected) {
            System.out.println("Library ID rejected as expected: " + expected.getMessage());
        } catch (UserNotFoundException unexpected) {
            throw new AssertionError("Unexpected user not found for library ID test: " + unexpected.getMessage());
        }

        try {
            String code = controller.sendResetCode("test-admin@example.com");
            System.out.println("Registered email accepted for password reset.");

            boolean resetSuccess = controller.resetPasswordWithCode("test-admin@example.com", code, "newPass456");
            if (!resetSuccess) {
                throw new AssertionError("Password reset should succeed and persist.");
            }

            if (!controller.getUserByEmail("test-admin@example.com").authenticate("newPass456")) {
                throw new AssertionError("Updated password was not persisted correctly.");
            }
            System.out.println("Password was reset and persisted successfully.");
        } catch (Exception ex) {
            throw new AssertionError("Registered email should be accepted for password reset: " + ex.getMessage());
        }
    }
}
