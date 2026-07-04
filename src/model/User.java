package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import util.PasswordUtil;

/**
 * User.java
 * Abstract base class representing any user in the Library Management System.
 *
 * Design: Follows the Template Method pattern – concrete subclasses (Student,
 * Librarian, Admin) extend this with role-specific behaviour while sharing
 * common identity and authentication fields.
 *
 * Implements Serializable for potential object-stream persistence.
 */
public abstract class User implements Serializable {

    private static final long serialVersionUID = 1L;

    // ─── Core Identity Fields ──────────────────────────────────────────────
    /** Unique system-generated identifier, e.g. "USR-0001" */
    protected String userId;
    /** Library login identifier, e.g. "ADM-001", "LIB-002", "STU-001" */
    protected String libraryId;
    /** Full display name */
    protected String name;
    /** Unique email address (used as login credential) */
    protected String email;
    /** Hashed or plain password (plain for demo; hash in production) */
    protected String password;
    /** User role: ADMIN | LIBRARIAN | STUDENT */
    protected String role;
    /** Numeric access level (0..5) - new RBAC system uses accessLevel instead of role strings */
    protected int accessLevel = 1; // default to Student-level access
    /** Contact phone number */
    protected String phone;
    /** Mailing/home address */
    protected String address;
    /** Whether the account is currently active */
    protected boolean active;
    /** Timestamp when the account was created */
    protected LocalDateTime createdAt;
    /** Timestamp of the last successful login */
    protected LocalDateTime lastLogin;

    // ─── Constructors ──────────────────────────────────────────────────────

    /**
     * Full-parameter constructor used when loading existing users from storage.
     *
     * @param userId    Unique identifier
     * @param name      Full name
     * @param email     Email / login credential
     * @param password  Password (plain text for demo)
     * @param role      One of ADMIN | LIBRARIAN | STUDENT
     * @param phone     Contact number
     * @param address   Physical address
     */
    public User(String userId, String name, String email,
                String password, String role, String phone, String address) {
        this.userId    = userId;
        this.libraryId = userId;
        this.name      = name;
        this.email     = email;
        this.password  = password;
        this.role      = role;
        // derive default access level from role using AppConfig helper (if available)
        try {
            this.accessLevel = config.AppConfig.roleToAccessLevel(role);
        } catch (Throwable t) {
            this.accessLevel = 1; // fallback
        }
        this.phone     = phone;
        this.address   = address;
        this.active    = true;
        this.createdAt = LocalDateTime.now();
        this.lastLogin = null;
    }

    /**
     * Minimal constructor for quick construction (phone/address set later).
     */
    public User(String userId, String name, String email, String password, String role) {
        this(userId, name, email, password, role, "", "");
    }

    // ─── Abstract / Template Methods ─────────────────────────────────────

    /**
     * Returns a human-readable description of the user's role and permissions.
     * Subclasses must implement this to describe what they can do.
     *
     * @return Role description string.
     */
    public abstract String getRoleDescription();

    /**
     * Returns the dashboard title shown after successful login.
     * Subclasses customise the welcome message.
     *
     * @return Dashboard title string.
     */
    public abstract String getDashboardTitle();

    // ─── Authentication Methods ──────────────────────────────────────────

    /**
     * Validates a supplied password against this user's stored password.
     * In a production system this would compare hashed values.
     *
     * @param inputPassword Plain-text password to verify.
     * @return true if passwords match and account is active.
     */
    public boolean authenticate(String inputPassword) {
        return this.active && PasswordUtil.verifyPassword(inputPassword, this.password);
    }

    /**
     * Called on successful authentication to update the lastLogin timestamp.
     */
    public void recordLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    /**
     * Deactivates this account (soft-delete / suspend).
     */
    public void logout() {
        // In a stateful session model, session tokens would be invalidated here.
        // For this desktop app, we simply note the intent.
        System.out.println(name + " logged out at " + LocalDateTime.now());
    }

    // ─── Getters & Setters ───────────────────────────────────────────────

    public String getUserId()   { return userId; }
    public void   setUserId(String userId) { this.userId = userId; }

    public String getLibraryId() { return libraryId; }
    public void   setLibraryId(String libraryId) { this.libraryId = libraryId; }

    public String getName()     { return name; }
    public void   setName(String name) { this.name = name; }

    public String getEmail()    { return email; }
    public void   setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void   setPassword(String password) { this.password = password; }

    public String getRole()     { return role; }
    public void   setRole(String role) { this.role = role; }

    public int getAccessLevel() { return accessLevel; }
    public void setAccessLevel(int level) { this.accessLevel = level; }

    public String getPhone()    { return phone; }
    public void   setPhone(String phone) { this.phone = phone; }

    public String getAddress()  { return address; }
    public void   setAddress(String address) { this.address = address; }

    public boolean isActive()   { return active; }
    public void    setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    // ─── Utility ─────────────────────────────────────────────────────────

    /**
     * Serialises this user to a pipe-delimited string for flat-file storage.
     * Format: userId|name|email|password|role|phone|address|active
     *
     * @return Pipe-delimited string representation.
     */
    public String toFileString() {
        return userId + "|" + name + "|" + email + "|" + password + "|"
                + role   + "|" + phone + "|" + address + "|" + active;
    }

    @Override
    public String toString() {
        return "User{id='" + userId + "', name='" + name
                + "', role='" + role + "', active=" + active + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return userId != null && userId.equals(other.userId);
    }

    @Override
    public int hashCode() {
        return userId == null ? 0 : userId.hashCode();
    }
}
