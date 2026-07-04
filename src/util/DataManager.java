package util;

import repository.BookRepository;
import repository.UserRepository;
import repository.LoanRepository;
import repository.ReservationRepository;
import repository.FineRepository;

/**
 * DataManager.java
 * Singleton that provides global access to repositories.
 * Ensures all services use the same in-memory data store.
 */
public class DataManager {

    private static DataManager instance;
    private BookRepository bookRepository;
    private UserRepository userRepository;
    private LoanRepository loanRepository;
    private ReservationRepository reservationRepository;
    private FineRepository fineRepository;

    private DataManager() {
        this.bookRepository = new BookRepository();
        this.userRepository = new UserRepository();
        this.loanRepository = new LoanRepository();
        this.reservationRepository = new ReservationRepository();
        this.fineRepository = new FineRepository();
    }

    /**
     * Gets the singleton instance of DataManager.
     *
     * @return The DataManager instance.
     */
    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    /**
     * Gets the book repository.
     *
     * @return BookRepository instance.
     */
    public BookRepository getBookRepository() {
        return bookRepository;
    }

    /**
     * Gets the user repository.
     *
     * @return UserRepository instance.
     */
    public UserRepository getUserRepository() {
        return userRepository;
    }

    /**
     * Gets the loan repository.
     *
     * @return LoanRepository instance.
     */
    public LoanRepository getLoanRepository() {
        return loanRepository;
    }

    /**
     * Gets the reservation repository.
     *
     * @return ReservationRepository instance.
     */
    public ReservationRepository getReservationRepository() {
        return reservationRepository;
    }

    /**
     * Gets the fine repository.
     *
     * @return FineRepository instance.
     */
    public FineRepository getFineRepository() {
        return fineRepository;
    }

    /**
     * Initializes the system with demo data.
     */
    public void initializeWithDemoData() {
        SystemInitializer initializer = new SystemInitializer();
        initializer.initializeSystem();
    }
}
