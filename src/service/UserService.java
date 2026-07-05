package service;

import dao.JdbcUserDao;
import dao.UserDao;
import model.User;
import repository.UserRepository;
import util.DataManager;
import util.DataBaseConnection;

import java.sql.Connection;
import java.util.List;

/**
 * UserService.java
 * Provides business logic for user account management.
 * Prefers a live JDBC-backed DAO if a DB connection is available,
 * otherwise falls back to the in-memory repositories.
 */
public class UserService {

    private UserRepository userRepository;
    private UserDao jdbcUserDao;
    private boolean useJdbc;

    public UserService() {
        this.userRepository = DataManager.getInstance().getUserRepository();
        ensureJdbcAvailability();
    }

    private synchronized void ensureJdbcAvailability() {
        Connection conn = DataBaseConnection.getConnection();
        if (conn != null) {
            this.useJdbc = true;
            if (this.jdbcUserDao == null) {
                this.jdbcUserDao = new JdbcUserDao();
            }
        } else {
            this.useJdbc = false;
        }
    }

    /**
     * Adds a new user to the system.
     *
     * @param user The user to add.
     * @return true if successfully added.
     */
    public boolean addUser(User user) {
        ensureJdbcAvailability();
        if (useJdbc) return jdbcUserDao.addUser(user);
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param userId The user identifier.
     * @return The User object or null if not found.
     */
    public User getUser(String userId) {
        ensureJdbcAvailability();
        if (useJdbc) return jdbcUserDao.getUserById(userId);
        return userRepository.findById(userId);
    }

    /**
     * Retrieves a user by email.
     *
     * @param email The user's email address.
     * @return The User object or null if not found.
     */
    public User getUserByEmail(String email) {
        ensureJdbcAvailability();
        if (useJdbc) return jdbcUserDao.getUserByEmail(email);
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves a user by library ID.
     *
     * @param libraryId The user's library login identifier.
     * @return The User object or null if not found.
     */
    public User getUserByLibraryId(String libraryId) {
        ensureJdbcAvailability();
        if (useJdbc) return jdbcUserDao.getUserByLibraryId(libraryId);
        return userRepository.findByLibraryId(libraryId);
    }

    public User getUserById(String userId) {
        ensureJdbcAvailability();
        if (useJdbc) return jdbcUserDao.getUserById(userId);
        return userRepository.findById(userId);
    }

    /**
     * Updates user information.
     *
     * @param user The user with updated information.
     * @return true if successfully updated.
     */
    public boolean updateUser(User user) {
        ensureJdbcAvailability();
        if (useJdbc) return jdbcUserDao.updateUser(user);
        return userRepository.update(user);
    }

    /**
     * Deletes a user from the system.
     *
     * @param userId The user to delete.
     * @return true if successfully deleted.
     */
    public boolean deleteUser(String userId) {
        ensureJdbcAvailability();
        if (useJdbc) return jdbcUserDao.deleteUser(userId);
        return userRepository.delete(userId);
    }

    /**
     * Retrieves all users of a specific role.
     *
     * @param role The role to filter by (ADMIN, LIBRARIAN, STUDENT).
     * @return List of users with that role.
     */
    public List<User> getUsersByRole(String role) {
        ensureJdbcAvailability();
        if (useJdbc) return jdbcUserDao.findByRole(role);
        return userRepository.findByRole(role);
    }

    /**
     * Retrieves all users in the system.
     *
     * @return List of all users.
     */
    public List<User> getAllUsers() {
        ensureJdbcAvailability();
        if (useJdbc) return jdbcUserDao.findAll();
        return userRepository.findAll();
    }
}
