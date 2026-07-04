package dao;

import model.User;
import util.DataBaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JdbcUserDao.java
 * Simple JDBC-based implementation for the `users` table.
 * Uses `DataBaseConnection` for obtaining a Connection.
 */
public class JdbcUserDao implements UserDao {

    private Connection conn;

    public JdbcUserDao() {
        this.conn = DataBaseConnection.getConnection();
    }

    private User mapRow(ResultSet rs) throws SQLException {
        String id = rs.getString("libraryId");
        String username = rs.getString("username");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String role = rs.getString("role");
        int accessLevel = 1;
        try { accessLevel = rs.getInt("access_level"); } catch (SQLException e) { /* ignore */ }

        // Instantiate concrete subclass based on role
        User u;
        if ("SUPERADMIN".equalsIgnoreCase(role)) {
            u = new model.SuperAdmin(id, username, email, password);
        } else if ("ADMIN".equalsIgnoreCase(role)) {
            u = new model.Admin(id, username, email, password);
        } else if ("LIBRARIAN".equalsIgnoreCase(role)) {
            u = new model.Librarian(id, username, email, password);
        } else if ("STAFF".equalsIgnoreCase(role)) {
            u = new model.Admin(id, username, email, password); // fallback to Admin-like behavior for staff login
            u.setRole("STAFF");
            u.setAccessLevel(config.AppConfig.ACCESS_STAFF);
        } else {
            u = new model.Student(id, username, email, password);
        }
        try { u.setAccessLevel(accessLevel); } catch (Throwable t) { /* ignore */ }
        return u;
    }

    @Override
    public boolean addUser(User user) {
        if (conn == null) return false;
        String sql = "INSERT INTO users (username, libraryId, email, password, role, access_level, phone, address) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getLibraryId());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getRole());
            ps.setInt(6, user.getAccessLevel());
            ps.setString(7, user.getPhone());
            ps.setString(8, user.getAddress());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User getUserById(String userId) {
        if (conn == null) return null;
        String sql = "SELECT * FROM users WHERE libraryId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        if (conn == null) return null;
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public User getUserByLibraryId(String libraryId) {
        return getUserById(libraryId);
    }

    @Override
    public boolean updateUser(User user) {
        if (conn == null) return false;
        String sql = "UPDATE users SET username=?, email=?, password=?, role=?, access_level=?, phone=?, address=? WHERE libraryId=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.setInt(5, user.getAccessLevel());
            ps.setString(6, user.getPhone());
            ps.setString(7, user.getAddress());
            ps.setString(8, user.getLibraryId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean deleteUser(String userId) {
        if (conn == null) return false;
        String sql = "DELETE FROM users WHERE libraryId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        if (conn == null) return list;
        String sql = "SELECT * FROM users";
        try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<User> findByRole(String role) {
        List<User> list = new ArrayList<>();
        if (conn == null) return list;
        String sql = "SELECT * FROM users WHERE role = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<User> findActive() {
        // schema doesn't have active flag; return all for now
        return findAll();
    }

    @Override
    public int count() {
        if (conn == null) return 0;
        String sql = "SELECT COUNT(*) FROM users";
        try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
}
