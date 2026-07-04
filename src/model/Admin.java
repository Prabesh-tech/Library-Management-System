package model;

import config.AppConfig;

/**
 * Admin.java
 * Represents a system administrator with full access to all features.
 *
 * An Admin can do everything a Librarian can, plus:
 *   - Create / suspend / delete any user account (including Librarians)
 *   - Change system configuration
 *   - View audit logs
 *   - Reset passwords
 */
public class Admin extends User {

    private static final long serialVersionUID = 1L;

    // ─── Admin-specific Fields ────────────────────────────────────────────
    /** Highest privilege flag – always true for Admin */
    private final boolean superAdmin;
    /** Number of accounts this admin has created */
    private int accountsCreated;
    /** Number of accounts this admin has suspended */
    private int accountsSuspended;

    // ─── Constructors ─────────────────────────────────────────────────────

    /**
     * Full constructor.
     *
     * @param userId    Unique identifier
     * @param name      Full name
     * @param email     Login email
     * @param password  Password
     * @param phone     Contact number
     * @param address   Physical address
     */
    public Admin(String userId, String name, String email, String password,
                 String phone, String address) {
        super(userId, name, email, password, AppConfig.ROLE_ADMIN, phone, address);
        this.superAdmin        = true;
        this.accountsCreated   = 0;
        this.accountsSuspended = 0;
    }

    /**
     * Quick constructor for creating a default admin account.
     */
    public Admin(String userId, String name, String email, String password) {
        this(userId, name, email, password, "", "");
    }

    // ─── Abstract Method Implementations ─────────────────────────────────

    @Override
    public String getRoleDescription() {
        return "Administrator – has full system access including user management, "
                + "book management, loan management, reports, and system configuration.";
    }

    @Override
    public String getDashboardTitle() {
        return "Admin Dashboard – " + name + " | Full Access";
    }

    // ─── Permission Checks (all true for Admin) ──────────────────────────

    public boolean canAddBooks()       { return true; }
    public boolean canUpdateBooks()    { return true; }
    public boolean canDeleteBooks()    { return true; }
    public boolean canIssueBooks()     { return true; }
    public boolean canReturnBooks()    { return true; }
    public boolean canManageStaff()    { return true; }
    public boolean canViewLogs()       { return true; }
    public boolean canResetPassword()  { return true; }
    public boolean canConfigureSystem(){ return true; }
    public boolean isSuperAdmin()      { return superAdmin; }

    // ─── Account Management Tracking ────────────────────────────────────

    /** Called each time this admin creates a user account. */
    public void recordAccountCreated()   { accountsCreated++; }

    /** Called each time this admin suspends a user account. */
    public void recordAccountSuspended() { accountsSuspended++; }

    // ─── Getters ─────────────────────────────────────────────────────────

    public int getAccountsCreated()   { return accountsCreated; }
    public void setAccountsCreated(int c) { this.accountsCreated = c; }

    public int getAccountsSuspended() { return accountsSuspended; }
    public void setAccountsSuspended(int s) { this.accountsSuspended = s; }

    // ─── Serialisation ────────────────────────────────────────────────────

    /**
     * Extends base serialisation with admin-specific counters.
     * Format: base_fields|accountsCreated|accountsSuspended
     */
    @Override
    public String toFileString() {
        return super.toFileString() + "|" + accountsCreated + "|" + accountsSuspended;
    }

    @Override
    public String toString() {
        return "Admin{id='" + userId + "', name='" + name
                + "', accountsCreated=" + accountsCreated + "}";
    }
}