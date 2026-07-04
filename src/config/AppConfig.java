package config;

/**
 * AppConfig.java
 * Central configuration class holding system-wide constants and settings
 * for the Library Management System.
 */
public class AppConfig {

    // ─── User Role Constants ──────────────────────────────────────────────
    public static final String ROLE_STAFF      = "STAFF";
    public static final String ROLE_STUDENT    = "STUDENT";
    public static final String ROLE_TEACHER    = "TEACHER";
    public static final String ROLE_LIBRARIAN  = "LIBRARIAN";
    public static final String ROLE_ADMIN      = "ADMIN";
    public static final String ROLE_SUPERADMIN = "SUPERADMIN";

    // ─── Access Level Constants (higher number => more privileges) ──────
    public static final int ACCESS_STAFF      = 0;
    public static final int ACCESS_STUDENT    = 1;
    public static final int ACCESS_TEACHER    = 2;
    public static final int ACCESS_LIBRARIAN  = 3;
    public static final int ACCESS_ADMIN      = 4;
    public static final int ACCESS_SUPERADMIN = 5;

    /** Map a role string to its default access level. */
    public static int roleToAccessLevel(String role) {
        if (role == null) return ACCESS_STUDENT;
        switch (role.toUpperCase()) {
            case "ADMIN": return ACCESS_ADMIN;
            case "LIBRARIAN": return ACCESS_LIBRARIAN;
            case "TEACHER": return ACCESS_TEACHER;
            case "STAFF": return ACCESS_STAFF;
            case "SUPERADMIN": return ACCESS_SUPERADMIN;
            case "STUDENT":
            default: return ACCESS_STUDENT;
        }
    }

    // ─── Loan Configuration ───────────────────────────────────────────────
    /** Number of days a student can keep a borrowed book */
    public static final int LOAN_PERIOD_DAYS = 14;
    
    /** Fine amount per day for overdue books (in currency units) */
    public static final double FINE_PER_DAY = 5.0;
    
    /** Maximum number of books a student can borrow simultaneously */
    public static final int MAX_BORROW_LIMIT = 5;

    // ─── File Storage Paths ───────────────────────────────────────────────
    public static final String DATA_DIR = "data/";
    public static final String USERS_FILE = DATA_DIR + "users.txt";
    public static final String BOOKS_FILE = DATA_DIR + "books.txt";
    public static final String LOANS_FILE = DATA_DIR + "loans.txt";
    public static final String LOGS_FILE  = DATA_DIR + "logs.txt";

    // ─── Application Metadata ────────────────────────────────────────────
    public static final String APP_NAME    = "Library Management System";
    public static final String APP_VERSION = "1.0.0";
    public static final String APP_AUTHOR  = "Student";

    // ─── UI Configuration ────────────────────────────────────────────────
    public static final int WINDOW_WIDTH  = 900;
    public static final int WINDOW_HEIGHT = 600;
    public static final String WINDOW_TITLE = APP_NAME + " v" + APP_VERSION;

    private AppConfig() {
        // Utility class – prevent instantiation
    }
}