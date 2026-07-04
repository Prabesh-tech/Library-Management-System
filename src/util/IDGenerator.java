package util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * IDGenerator.java
 * Generates unique IDs for entities (Users, Books, Loans, Reservations, Fines).
 */
public class IDGenerator {

    private static final AtomicInteger userCounter        = new AtomicInteger(1);
    private static final AtomicInteger bookCounter        = new AtomicInteger(1);
    private static final AtomicInteger loanCounter        = new AtomicInteger(1);
    private static final AtomicInteger reservationCounter = new AtomicInteger(1);
    private static final AtomicInteger fineCounter        = new AtomicInteger(1);

    private IDGenerator() {
        // Utility class – prevent instantiation
    }

    /**
     * Generates a unique user ID.
     *
     * @return User ID in format "USR-XXXX"
     */
    public static String generateUserId() {
        return String.format("USR-%04d", userCounter.getAndIncrement());
    }

    /**
     * Generates a unique book ID.
     *
     * @return Book ID in format "BK-XXXX"
     */
    public static String generateBookId() {
        return String.format("BK-%04d", bookCounter.getAndIncrement());
    }

    /**
     * Generates a unique loan ID.
     *
     * @return Loan ID in format "LN-XXXX"
     */
    public static String generateLoanId() {
        return String.format("LN-%04d", loanCounter.getAndIncrement());
    }

    /**
     * Generates a unique reservation ID.
     *
     * @return Reservation ID in format "RSV-XXXX"
     */
    public static String generateReservationId() {
        return String.format("RSV-%04d", reservationCounter.getAndIncrement());
    }

    /**
     * Generates a unique fine ID.
     *
     * @return Fine ID in format "FNE-XXXX"
     */
    public static String generateFineId() {
        return String.format("FNE-%04d", fineCounter.getAndIncrement());
    }

    /**
     * Resets all counters (for testing purposes).
     */
    public static void resetCounters() {
        userCounter.set(1);
        bookCounter.set(1);
        loanCounter.set(1);
        reservationCounter.set(1);
        fineCounter.set(1);
    }
}
