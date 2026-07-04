package repository;

import model.Loan;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LoanRepository.java
 * Data access layer for Loan entities.
 * Currently uses in-memory storage; can be replaced with file/DB backend.
 */
public class LoanRepository {

    private Map<String, Loan> loanStore;

    public LoanRepository() {
        this.loanStore = new HashMap<>();
    }

    /**
     * Saves a new loan to the repository.
     *
     * @param loan The loan to save.
     * @return true if successfully saved.
     */
    public boolean save(Loan loan) {
        if (loan != null && loan.getLoanId() != null) {
            loanStore.put(loan.getLoanId(), loan);
            return true;
        }
        return false;
    }

    /**
     * Finds a loan by its ID.
     *
     * @param loanId The loan identifier.
     * @return The Loan or null if not found.
     */
    public Loan findById(String loanId) {
        return loanStore.get(loanId);
    }

    /**
     * Updates an existing loan.
     *
     * @param loan The loan with updated information.
     * @return true if successfully updated.
     */
    public boolean update(Loan loan) {
        if (loan != null && loan.getLoanId() != null && loanStore.containsKey(loan.getLoanId())) {
            loanStore.put(loan.getLoanId(), loan);
            return true;
        }
        return false;
    }

    /**
     * Deletes a loan from the repository.
     *
     * @param loanId The loan to delete.
     * @return true if successfully deleted.
     */
    public boolean delete(String loanId) {
        return loanStore.remove(loanId) != null;
    }

    /**
     * Finds all loans in the repository.
     *
     * @return List of all loans.
     */
    public List<Loan> findAll() {
        return new ArrayList<>(loanStore.values());
    }

    /**
     * Finds all loans for a specific user.
     *
     * @param userId The user identifier.
     * @return List of loans for that user.
     */
    public List<Loan> findByUserId(String userId) {
        List<Loan> userLoans = new ArrayList<>();
        for (Loan loan : loanStore.values()) {
            if (loan.getUserId().equals(userId)) {
                userLoans.add(loan);
            }
        }
        return userLoans;
    }

    /**
     * Finds all active (not yet returned) loans.
     *
     * @return List of active loans.
     */
    public List<Loan> findActive() {
        List<Loan> activeLoans = new ArrayList<>();
        for (Loan loan : loanStore.values()) {
            if (Loan.STATUS_ACTIVE.equals(loan.getStatus())) {
                activeLoans.add(loan);
            }
        }
        return activeLoans;
    }

    /**
     * Finds all overdue loans.
     *
     * @return List of overdue loans.
     */
    public List<Loan> findOverdue() {
        List<Loan> overdueLoans = new ArrayList<>();
        for (Loan loan : loanStore.values()) {
            loan.refreshStatus();
            if (loan.isOverdue()) {
                overdueLoans.add(loan);
            }
        }
        return overdueLoans;
    }

    /**
     * Finds all loans with unpaid fines.
     *
     * @return List of loans with outstanding fines.
     */
    public List<Loan> findWithUnpaidFines() {
        List<Loan> fineLoans = new ArrayList<>();
        for (Loan loan : loanStore.values()) {
            if (loan.getFine() > 0 && !loan.isFinePaid()) {
                fineLoans.add(loan);
            }
        }
        return fineLoans;
    }
}
