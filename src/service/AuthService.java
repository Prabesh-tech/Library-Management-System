package service;

import model.User;

/**
 * AuthService.java
 * Handles authentication and authorization logic.
 */
public class AuthService {

    private UserService userService;

    public AuthService() {
        this.userService = new UserService();
    }

    /**
     * Validates user credentials and authenticates them.
     *
     * @param email    The user's email.
     * @param password The plaintext password.
     * @return true if credentials are valid and user is active.
     */
    public boolean authenticate(String identifier, String password) {
        if (identifier == null || identifier.trim().isEmpty()) {
            return false;
        }
        String normalized = identifier.trim();
        User user = null;

        if (normalized.contains("@")) {
            user = userService.getUserByEmail(normalized);
        } else {
            user = userService.getUserByLibraryId(normalized);
            if (user == null) {
                user = userService.getUserByEmail(normalized);
            }
        }

        if (user != null && user.isActive()) {
            return user.authenticate(password);
        }
        return false;
    }

    /**
     * Checks whether the user's numeric access level meets or exceeds the required level.
     */
    public boolean hasAccessLevel(User user, int requiredLevel) {
        if (user == null || !user.isActive()) return false;
        return user instanceof User && user.getAccessLevel() >= requiredLevel;
    }

    /**
     * Determines whether the current user may borrow a book on behalf of another user.
     * Only librarians and admins (and higher) are allowed to do this.
     */
    public boolean canBorrowForOtherUser(User user) {
        return hasAccessLevel(user, config.AppConfig.ACCESS_LIBRARIAN);
    }

    /**
     * Checks if a user has permission to perform an action based on their role.
     *
     * @param user       The user to check.
     * @param permission The permission required.
     * @return true if user has the permission.
     */
    public boolean hasPermission(User user, String permission) {
        if (user == null || !user.isActive()) {
            return false;
        }

        // Define minimal access level required per permission
        java.util.Map<String, Integer> permissionMap = new java.util.HashMap<>();
        // Common
        permissionMap.put("view_books", 0);
        permissionMap.put("view_profile", 0);
        permissionMap.put("view_announcements", 0);

        // Student actions
        permissionMap.put("borrow_books", config.AppConfig.ACCESS_STUDENT);
        permissionMap.put("reserve_books", config.AppConfig.ACCESS_STUDENT);
        permissionMap.put("write_reviews", config.AppConfig.ACCESS_STUDENT);

        // Teacher (inherits student)
        permissionMap.put("recommend_books", config.AppConfig.ACCESS_TEACHER);

        // Librarian actions
        permissionMap.put("manage_books", config.AppConfig.ACCESS_LIBRARIAN);
        permissionMap.put("process_loans", config.AppConfig.ACCESS_LIBRARIAN);
        permissionMap.put("manage_fines", config.AppConfig.ACCESS_LIBRARIAN);

        // Admin actions
        permissionMap.put("manage_users", config.AppConfig.ACCESS_LIBRARIAN);
        permissionMap.put("system_settings", config.AppConfig.ACCESS_ADMIN);
        permissionMap.put("manage_staff", config.AppConfig.ACCESS_ADMIN);

        // Super admin
        permissionMap.put("full_system_control", config.AppConfig.ACCESS_SUPERADMIN);

        Integer required = permissionMap.get(permission);
        if (required == null) {
            // default: require librarian level for unknown sensitive actions
            required = config.AppConfig.ACCESS_LIBRARIAN;
        }

        return hasAccessLevel(user, required);
    }

    /**
     * Records a successful login for a user.
     *
     * @param user The user logging in.
     */
    public void recordLogin(User user) {
        if (user != null) {
            user.recordLogin();
            userService.updateUser(user);
        }
    }

    /**
     * Checks if a user can be modified by another user (admin check).
     *
     * @param adminUser The user attempting to modify.
     * @param targetUser The user being modified.
     * @return true if adminUser can modify targetUser.
     */
    public boolean canModifyUser(User adminUser, User targetUser) {
        if (adminUser == null || !adminUser.isActive()) {
            return false;
        }
        return adminUser.getAccessLevel() >= config.AppConfig.ACCESS_ADMIN;
    }
}
