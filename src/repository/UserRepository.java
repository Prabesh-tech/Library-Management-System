package repository;

import model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserRepository.java
 * Data access layer for User entities.
 * Currently uses in-memory storage; can be replaced with file/DB backend.
 */
public class UserRepository {

    private Map<String, User> userStore;

    public UserRepository() {
        this.userStore = new HashMap<>();
    }

    /**
     * Saves a new user to the repository.
     *
     * @param user The user to save.
     * @return true if successfully saved.
     */
    public boolean save(User user) {
        if (user != null && user.getUserId() != null) {
            userStore.put(user.getUserId(), user);
            return true;
        }
        return false;
    }

    /**
     * Finds a user by their ID.
     *
     * @param userId The user identifier.
     * @return The User or null if not found.
     */
    public User findById(String userId) {
        return userStore.get(userId);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email The user's email.
     * @return The User or null if not found.
     */
    public User findByEmail(String email) {
        if (email == null) {
            return null;
        }
        String normalized = email.trim().toLowerCase();
        for (User user : userStore.values()) {
            if (user.getEmail() != null && user.getEmail().trim().toLowerCase().equals(normalized)) {
                return user;
            }
        }
        return null;
    }

    public User findByLibraryId(String libraryId) {
        if (libraryId == null) {
            return null;
        }
        String normalized = libraryId.trim().toLowerCase();
        for (User user : userStore.values()) {
            if (user.getLibraryId() != null && user.getLibraryId().trim().toLowerCase().equals(normalized)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Updates an existing user.
     *
     * @param user The user with updated information.
     * @return true if successfully updated.
     */
    public boolean update(User user) {
        if (user != null && user.getUserId() != null && userStore.containsKey(user.getUserId())) {
            userStore.put(user.getUserId(), user);
            return true;
        }
        return false;
    }

    /**
     * Deletes a user from the repository.
     *
     * @param userId The user to delete.
     * @return true if successfully deleted.
     */
    public boolean delete(String userId) {
        return userStore.remove(userId) != null;
    }

    /**
     * Finds all users in the repository.
     *
     * @return List of all users.
     */
    public List<User> findAll() {
        return new ArrayList<>(userStore.values());
    }

    /**
     * Finds all users with a specific role.
     *
     * @param role The role to filter by (ADMIN, LIBRARIAN, STUDENT).
     * @return List of users with that role.
     */
    public List<User> findByRole(String role) {
        List<User> roleUsers = new ArrayList<>();
        for (User user : userStore.values()) {
            if (user.getRole().equals(role)) {
                roleUsers.add(user);
            }
        }
        return roleUsers;
    }

    /**
     * Finds all active users.
     *
     * @return List of active users only.
     */
    public List<User> findActive() {
        List<User> activeUsers = new ArrayList<>();
        for (User user : userStore.values()) {
            if (user.isActive()) {
                activeUsers.add(user);
            }
        }
        return activeUsers;
    }

    /**
     * Counts total users in the system.
     *
     * @return Number of users.
     */
    public int count() {
        return userStore.size();
    }
}
