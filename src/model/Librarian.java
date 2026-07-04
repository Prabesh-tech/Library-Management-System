package model;

import config.AppConfig;
import java.time.LocalDate;

/**
 * Librarian.java
 * Represents a library staff member who manages books and loans.
 *
 * A Librarian can add, update, and delete books, issue and receive returns,
 * and manage student accounts. They cannot alter system-level settings
 * (that is Admin territory).
 */
public class Librarian extends User {

    private static final long serialVersionUID = 1L;

    // ─── Librarian-specific Fields ────────────────────────────────────────
    /** Employee ID badge number */
    private String employeeId;
    /** The section / wing of the library this librarian manages */
    private String section;
    /** Date the librarian joined the library */
    private LocalDate joiningDate;
    /** Total number of transactions (issues + returns) handled */
    private int totalTransactionsHandled;

    // ─── Constructors ─────────────────────────────────────────────────────

    /**
     * Full constructor used when loading from storage.
     */
    public Librarian(String userId, String name, String email, String password,
                     String phone, String address, String employeeId, String section) {
        super(userId, name, email, password, AppConfig.ROLE_LIBRARIAN, phone, address);
        this.employeeId                = employeeId;
        this.section                   = section;
        this.joiningDate               = LocalDate.now();
        this.totalTransactionsHandled  = 0;
    }

    /**
     * Quick constructor – section and employee ID can be set later.
     */
    public Librarian(String userId, String name, String email, String password) {
        this(userId, name, email, password, "", "", userId, "General");
    }

    // ─── Abstract Method Implementations ─────────────────────────────────

    @Override
    public String getRoleDescription() {
        return "Librarian – can manage the book catalogue, issue and receive "
                + "book returns, view student records, and calculate fines.";
    }

    @Override
    public String getDashboardTitle() {
        return "Librarian Dashboard – " + name + " | Section: " + section;
    }

    // ─── Permission Checks ────────────────────────────────────────────────

    /** @return true – Librarians can add new books to the catalogue. */
    public boolean canAddBooks()    { return true; }

    /** @return true – Librarians can update existing book details. */
    public boolean canUpdateBooks() { return true; }

    /** @return true – Librarians can remove books from the catalogue. */
    public boolean canDeleteBooks() { return true; }

    /** @return true – Librarians can issue books to students. */
    public boolean canIssueBooks()  { return true; }

    /** @return true – Librarians can receive book returns. */
    public boolean canReturnBooks() { return true; }

    /** @return false – Only Admins can manage staff accounts. */
    public boolean canManageStaff() { return false; }

    // ─── Transaction Tracking ────────────────────────────────────────────

    /**
     * Increments the transaction counter when the librarian processes
     * an issue or return.
     */
    public void recordTransaction() {
        this.totalTransactionsHandled++;
    }

    // ─── Getters & Setters ────────────────────────────────────────────────

    public String getEmployeeId()  { return employeeId; }
    public void   setEmployeeId(String e) { this.employeeId = e; }

    public String getSection()     { return section; }
    public void   setSection(String s) { this.section = s; }

    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate d) { this.joiningDate = d; }

    public int  getTotalTransactionsHandled() { return totalTransactionsHandled; }
    public void setTotalTransactionsHandled(int t) { this.totalTransactionsHandled = t; }

    // ─── Serialisation ────────────────────────────────────────────────────

    /**
     * Extends base serialisation with librarian-specific fields.
     * Format: base_fields|employeeId|section|joiningDate|totalTransactions
     */
    @Override
    public String toFileString() {
        return super.toFileString() + "|" + employeeId + "|" + section
                + "|" + joiningDate + "|" + totalTransactionsHandled;
    }

    @Override
    public String toString() {
        return "Librarian{id='" + userId + "', name='" + name
                + "', section='" + section + "', transactions=" + totalTransactionsHandled + "}";
    }
}