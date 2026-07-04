package model;

import config.AppConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Student.java
 * Represents a library member with borrowing privileges.
 *
 * A Student can borrow up to AppConfig.MAX_BORROW_LIMIT books simultaneously.
 * Tracks active loan IDs, accumulated fines, and department/year info.
 */
public class Student extends User {

    private static final long serialVersionUID = 1L;

    // ─── Student-specific Fields ──────────────────────────────────────────
    /** Academic department or faculty */
    private String department;
    /** Year of study, e.g. "Year 2" */
    private String yearOfStudy;
    /** Student registration / matriculation number */
    private String studentId;
    /** IDs of currently active loans (not yet returned) */
    private List<String> activeLoanIds;
    /** Accumulated unpaid fine amount */
    private double outstandingFine;
    /** Total books borrowed over account lifetime */
    private int totalBooksBorrowed;

    // ─── Constructors ─────────────────────────────────────────────────────

    /**
     * Full constructor – used when loading from file/DB.
     */
    public Student(String userId, String name, String email, String password,
                   String phone, String address, String department,
                   String yearOfStudy, String studentId) {
        super(userId, name, email, password, AppConfig.ROLE_STUDENT, phone, address);
        this.department        = department;
        this.yearOfStudy       = yearOfStudy;
        this.studentId         = studentId;
        this.activeLoanIds     = new ArrayList<>();
        this.outstandingFine   = 0.0;
        this.totalBooksBorrowed = 0;
    }

    /**
     * Convenience constructor (no phone/address/department details yet).
     */
    public Student(String userId, String name, String email, String password) {
        this(userId, name, email, password, "", "", "", "", userId);
    }

    // ─── Abstract Method Implementations ─────────────────────────────────

    @Override
    public String getRoleDescription() {
        return "Student – can search books, borrow up to "
                + AppConfig.MAX_BORROW_LIMIT + " books, and view their loan history.";
    }

    @Override
    public String getDashboardTitle() {
        return "Student Dashboard – Welcome, " + name;
    }

    // ─── Borrowing Logic ─────────────────────────────────────────────────

    /**
     * Checks if this student can borrow another book.
     *
     * @return true if they have not reached the borrow limit and have no
     *         outstanding fines blocking new loans.
     */
    public boolean canBorrow() {
        return activeLoanIds.size() < AppConfig.MAX_BORROW_LIMIT
                && outstandingFine == 0.0;
    }

    /**
     * Records a new loan against this student's account.
     *
     * @param loanId The loan ID to add.
     * @return true if added successfully; false if borrow limit reached.
     */
    public boolean addLoan(String loanId) {
        if (!canBorrow()) return false;
        activeLoanIds.add(loanId);
        totalBooksBorrowed++;
        return true;
    }

    /**
     * Removes a loan from the active list when a book is returned.
     *
     * @param loanId The loan ID to remove.
     * @return true if found and removed.
     */
    public boolean removeLoan(String loanId) {
        return activeLoanIds.remove(loanId);
    }

    /**
     * Adds to the student's outstanding fine total.
     *
     * @param amount Amount to add (must be ≥ 0).
     */
    public void addFine(double amount) {
        if (amount > 0) outstandingFine += amount;
    }

    /**
     * Clears outstanding fines (e.g., after payment).
     */
    public void clearFines() {
        outstandingFine = 0.0;
    }

    // ─── Getters & Setters ────────────────────────────────────────────────

    public String getDepartment()     { return department; }
    public void   setDepartment(String d) { this.department = d; }

    public String getYearOfStudy()    { return yearOfStudy; }
    public void   setYearOfStudy(String y) { this.yearOfStudy = y; }

    public String getStudentId()      { return studentId; }
    public void   setStudentId(String s) { this.studentId = s; }

    public List<String> getActiveLoanIds() {
        return Collections.unmodifiableList(activeLoanIds);
    }
    public void setActiveLoanIds(List<String> ids) {
        this.activeLoanIds = new ArrayList<>(ids);
    }

    public double getOutstandingFine()    { return outstandingFine; }
    public void   setOutstandingFine(double f) { this.outstandingFine = f; }

    public int  getTotalBooksBorrowed()   { return totalBooksBorrowed; }
    public void setTotalBooksBorrowed(int t) { this.totalBooksBorrowed = t; }

    // ─── Serialisation ────────────────────────────────────────────────────

    /**
     * Extends base toFileString() with student-specific fields.
     * Format: base_fields|department|yearOfStudy|studentId|outstandingFine|totalBorrowed
     */
    @Override
    public String toFileString() {
        return super.toFileString() + "|" + department + "|" + yearOfStudy
                + "|" + studentId + "|" + outstandingFine + "|" + totalBooksBorrowed;
    }

    @Override
    public String toString() {
        return "Student{id='" + userId + "', name='" + name
                + "', loans=" + activeLoanIds.size()
                + ", fine=" + outstandingFine + "}";
    }
}