package model;

import config.AppConfig;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Loan.java
 * Represents a book-borrowing transaction between a student and the library.
 *
 * Lifecycle:
 *   ACTIVE  → book has been issued; return date is in the future (or past = overdue)
 *   RETURNED → book has been returned; fine (if any) was calculated at return
 *   OVERDUE  → ACTIVE but return date has passed
 */
public class Loan implements Serializable {

    private static final long serialVersionUID = 1L;

    // ─── Loan Status Constants ────────────────────────────────────────────
    public static final String STATUS_ACTIVE   = "ACTIVE";
    public static final String STATUS_RETURNED = "RETURNED";
    public static final String STATUS_OVERDUE  = "OVERDUE";

    // ─── Fields ───────────────────────────────────────────────────────────
    /** Unique loan identifier, e.g. "LN-0001" */
    private String  loanId;
    /** ID of the book being borrowed */
    private String  bookId;
    /** ID of the student borrowing the book */
    private String  userId;
    /** ID of the librarian who processed the issue */
    private String  issuedById;
    /** Date the book was issued */
    private LocalDate issueDate;
    /** Expected return date = issueDate + LOAN_PERIOD_DAYS */
    private LocalDate dueDate;
    /** Actual date of return (null if not yet returned) */
    private LocalDate returnDate;
    /** Current loan status */
    private String  status;
    /** Fine amount calculated at return (or current overdue amount) */
    private double  fine;
    /** Whether the fine has been paid */
    private boolean finePaid;
    /** Optional remarks added by librarian */
    private String  remarks;

    // ─── Constructor ──────────────────────────────────────────────────────

    /**
     * Creates a new loan (book issuance).
     *
     * @param loanId     Unique identifier
     * @param bookId     The book being borrowed
     * @param userId     The borrowing student
     * @param issuedById Librarian/Admin processing the loan
     */
    public Loan(String loanId, String bookId, String userId, String issuedById) {
        this.loanId     = loanId;
        this.bookId     = bookId;
        this.userId     = userId;
        this.issuedById = issuedById;
        this.issueDate  = LocalDate.now();
        this.dueDate    = issueDate.plusDays(AppConfig.LOAN_PERIOD_DAYS);
        this.returnDate = null;
        this.status     = STATUS_ACTIVE;
        this.fine       = 0.0;
        this.finePaid   = false;
        this.remarks    = "";
    }

    /**
     * Full constructor – used when loading from storage.
     */
    public Loan(String loanId, String bookId, String userId, String issuedById,
                LocalDate issueDate, LocalDate dueDate, LocalDate returnDate,
                String status, double fine, boolean finePaid, String remarks) {
        this.loanId     = loanId;
        this.bookId     = bookId;
        this.userId     = userId;
        this.issuedById = issuedById;
        this.issueDate  = issueDate;
        this.dueDate    = dueDate;
        this.returnDate = returnDate;
        this.status     = status;
        this.fine       = fine;
        this.finePaid   = finePaid;
        this.remarks    = remarks;
    }

    // ─── Business Logic ───────────────────────────────────────────────────

    /**
     * Calculates the fine for this loan based on days overdue.
     * Fine = max(0, daysOverdue) × AppConfig.FINE_PER_DAY
     *
     * Uses returnDate if already returned; today's date otherwise.
     *
     * @return Fine amount in currency units.
     */
    public double calculateFine() {
        LocalDate checkDate = (returnDate != null) ? returnDate : LocalDate.now();
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, checkDate);
        if (daysOverdue > 0) {
            return daysOverdue * AppConfig.FINE_PER_DAY;
        }
        return 0.0;
    }

    /**
     * Processes the book return: sets the return date, computes the fine,
     * and updates the status.
     *
     * @param returnedBy Optional remark identifying who processed the return.
     */
    public void processReturn(String returnedBy) {
        this.returnDate = LocalDate.now();
        this.fine       = calculateFine();
        this.status     = STATUS_RETURNED;
        this.finePaid   = (fine == 0.0);  // no fine means automatically "paid"
        if (returnedBy != null && !returnedBy.isEmpty()) {
            this.remarks = "Returned via: " + returnedBy;
        }
    }

    /**
     * Refreshes the status to OVERDUE if the due date has passed
     * and the book is still active.
     */
    public void refreshStatus() {
        if (STATUS_ACTIVE.equals(status) && LocalDate.now().isAfter(dueDate)) {
            status = STATUS_OVERDUE;
        }
    }

    /**
     * @return Number of days remaining until due (negative if overdue).
     */
    public long getDaysUntilDue() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }

    /**
     * @return true if this loan is currently overdue.
     */
    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }

    // ─── Getters & Setters ────────────────────────────────────────────────

    public String    getLoanId()     { return loanId; }
    public void      setLoanId(String l) { this.loanId = l; }

    public String    getBookId()     { return bookId; }
    public void      setBookId(String b) { this.bookId = b; }

    public String    getUserId()     { return userId; }
    public void      setUserId(String u) { this.userId = u; }

    public String    getIssuedById() { return issuedById; }
    public void      setIssuedById(String i) { this.issuedById = i; }

    public LocalDate getIssueDate()  { return issueDate; }
    public void      setIssueDate(LocalDate d) { this.issueDate = d; }

    public LocalDate getDueDate()    { return dueDate; }
    public void      setDueDate(LocalDate d) { this.dueDate = d; }

    public LocalDate getReturnDate() { return returnDate; }
    public void      setReturnDate(LocalDate d) { this.returnDate = d; }

    public String    getStatus()     { return status; }
    public void      setStatus(String s) { this.status = s; }

    public double    getFine()       { return fine; }
    public void      setFine(double f) { this.fine = f; }

    public boolean   isFinePaid()    { return finePaid; }
    public void      setFinePaid(boolean fp) { this.finePaid = fp; }

    public String    getRemarks()    { return remarks; }
    public void      setRemarks(String r) { this.remarks = r; }

    // ─── Serialisation ────────────────────────────────────────────────────

    /**
     * Serialises the loan to a pipe-delimited string for flat-file storage.
     * Format: loanId|bookId|userId|issuedById|issueDate|dueDate|returnDate|
     *         status|fine|finePaid|remarks
     */
    public String toFileString() {
        return loanId + "|" + bookId + "|" + userId + "|" + issuedById + "|"
                + issueDate + "|" + dueDate + "|"
                + (returnDate != null ? returnDate : "null") + "|"
                + status + "|" + fine + "|" + finePaid + "|" + remarks;
    }

    @Override
    public String toString() {
        return "Loan{id='" + loanId + "', book='" + bookId
                + "', user='" + userId + "', status='" + status
                + "', fine=" + fine + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Loan)) return false;
        Loan other = (Loan) obj;
        return loanId != null && loanId.equals(other.loanId);
    }

    @Override
    public int hashCode() {
        return loanId == null ? 0 : loanId.hashCode();
    }
}