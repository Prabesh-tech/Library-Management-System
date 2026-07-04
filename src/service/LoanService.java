package service;

import model.Loan;
import repository.LoanRepository;
import util.DataManager;
import java.util.List;

/**
 * LoanService.java
 * Handles business logic for book loans, returns, and fine management.
 */
public class LoanService {

    private LoanRepository loanRepository;

    public LoanService() {
        this.loanRepository = DataManager.getInstance().getLoanRepository();
    }

    /**
     * Creates a new loan.
     *
     * @param loan The loan to create.
     * @return true if successfully created.
     */
    public boolean createLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    /**
     * Retrieves a loan by ID.
     *
     * @param loanId The loan identifier.
     * @return The Loan object or null if not found.
     */
    public Loan getLoan(String loanId) {
        return loanRepository.findById(loanId);
    }

    /**
     * Updates loan details.
     *
     * @param loan The loan with updated information.
     * @return true if successfully updated.
     */
    public boolean updateLoan(Loan loan) {
        return loanRepository.update(loan);
    }

    /**
     * Retrieves all loans for a specific user.
     *
     * @param userId The user identifier.
     * @return List of loans for that user.
     */
    public List<Loan> getLoansByUser(String userId) {
        return loanRepository.findByUserId(userId);
    }

    /**
     * Retrieves all overdue loans.
     *
     * @return List of overdue loans.
     */
    public List<Loan> getOverdueLoans() {
        return loanRepository.findOverdue();
    }

    /**
     * Retrieves all loans with unpaid fines.
     *
     * @return List of loans with outstanding fines.
     */
    public List<Loan> getLoansWithUnpaidFines() {
        return loanRepository.findWithUnpaidFines();
    }

    /**
     * Retrieves all loans in the system.
     *
     * @return List of all loans.
     */
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
}
