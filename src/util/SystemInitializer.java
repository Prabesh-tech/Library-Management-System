package util;

import model.*;
import repository.BookRepository;
import repository.UserRepository;
import repository.LoanRepository;
import java.time.LocalDate;

/**
 * SystemInitializer.java
 * Initializes the system with demo data for testing and development.
 * Populates sample users, books, and loans.
 */
public class SystemInitializer {

    private BookRepository bookRepository;
    private UserRepository userRepository;
    private LoanRepository loanRepository;

    public SystemInitializer() {
        // Use the shared repositories from DataManager so demo data is available
        // throughout the application (including login/authentication).
        this.bookRepository = DataManager.getInstance().getBookRepository();
        this.userRepository = DataManager.getInstance().getUserRepository();
        this.loanRepository = DataManager.getInstance().getLoanRepository();
    }

    /**
     * Initializes the system with sample data.
     */
    public void initializeSystem() {
        System.out.println("Initializing Library Management System with demo data...");
        
        // Reset ID generators
        IDGenerator.resetCounters();
        
        // Add sample users
        addSampleUsers();
        
        // Add sample books
        addSampleBooks();
        
        // Add sample loans
        addSampleLoans();
        
        System.out.println("✓ System initialization complete!");
    }

    /**
     * Adds sample user accounts.
     */
    private void addSampleUsers() {
        // Super admin user
        SuperAdmin superAdmin = new SuperAdmin("USR-0000", "Prabesh SuperAdmin", "prabeshadmin@library.com", "super123", "+10000000000", "Library HQ");
        superAdmin.setLibraryId("SA-001");
        userRepository.save(superAdmin);

        // Admin user
        Admin admin = new Admin("USR-0001", "John Admin", "admin@library.com", "admin123", "555-0001", "123 Admin St");
        admin.setLibraryId("ADM-001");
        userRepository.save(admin);

        // Librarian user
        Librarian librarian = new Librarian("USR-0002", "Jane Librarian", "librarian@library.com", "lib123", "555-0002", "456 Staff Ave", "LIB-002", "Reference Section");
        librarian.setLibraryId("LIB-002");
        userRepository.save(librarian);

        // Sample students
        Student student1 = new Student("USR-0003", "Ahmed Student", "ahmed@university.edu", "pass123", "555-0003", "789 College Dr", "Computer Science", "Year 2", "STU-001");
        student1.setLibraryId("STU-001");
        userRepository.save(student1);

        Student student2 = new Student("USR-0004", "Fatima Khan", "fatima@university.edu", "pass456", "555-0004", "321 Campus Ln", "Engineering", "Year 3", "STU-002");
        student2.setLibraryId("STU-002");
        userRepository.save(student2);

        Student student3 = new Student("USR-0005", "Ali Hassan", "ali@university.edu", "pass789", "555-0005", "654 Dorm Way", "Business", "Year 1", "STU-003");
        student3.setLibraryId("STU-003");
        userRepository.save(student3);

        Teacher teacher = new Teacher("USR-0006", "Dr. Sara Teacher", "teacher@university.edu", "teach123", "555-0006", "987 Faculty Rd", "Computer Science", "TEA-001");
        teacher.setLibraryId("TEA-001");
        userRepository.save(teacher);

        Staff staff = new Staff("USR-0007", "Mina Staff", "staff@library.com", "staff123", "555-0007", "159 Service Blvd", "Library Support", "STF-001");
        staff.setLibraryId("STF-001");
        userRepository.save(staff);

        System.out.println("✓ Added 8 sample users (1 SuperAdmin, 1 Admin, 1 Librarian, 3 Students, 1 Teacher, 1 Staff)");
    }

    /**
     * Adds sample books to the catalogue.
     */
    private void addSampleBooks() {
        Book[] books = {
            new Book("BK-0001", "Clean Code", "Robert C. Martin", "978-0132350884", "Programming", 
                     "Prentice Hall", 2008, "How to write clean, maintainable code", "English", 464, 3, 2, "Section A-1"),
            
            new Book("BK-0002", "Design Patterns", "Gang of Four", "978-0201633610", "Programming",
                     "Addison-Wesley", 1994, "Reusable solutions to common design problems", "English", 395, 2, 1, "Section A-2"),
            
            new Book("BK-0003", "The Pragmatic Programmer", "David Thomas", "978-0201616224", "Programming",
                     "Addison-Wesley", 1999, "Your journey to mastery in software development", "English", 321, 4, 3, "Section A-3"),
            
            new Book("BK-0004", "Introduction to Algorithms", "Cormen", "978-0262033848", "Computer Science",
                     "MIT Press", 2009, "Comprehensive study of algorithms", "English", 1313, 2, 1, "Section B-1"),
            
            new Book("BK-0005", "The Art of Computer Programming", "Donald Knuth", "978-0201896831", "Computer Science",
                     "Addison-Wesley", 1997, "Fundamental algorithms and programming techniques", "English", 762, 1, 1, "Section B-2"),
            
            new Book("BK-0006", "Artificial Intelligence", "Stuart Russell", "978-0135492619", "Technology",
                     "Pearson", 2020, "Modern approach to artificial intelligence", "English", 1180, 3, 2, "Section C-1"),
            
            new Book("BK-0007", "Database Design", "Jennifer Widom", "978-0201616224", "Database",
                     "Academic Press", 2008, "Relational database design principles", "English", 520, 2, 2, "Section C-2"),
            
            new Book("BK-0008", "Web Development with Java", "Paul Deitel", "978-0132760263", "Web Development",
                     "Prentice Hall", 2012, "Building dynamic web applications with Java", "English", 1050, 3, 1, "Section D-1")
        };

        for (Book book : books) {
            bookRepository.save(book);
        }

        System.out.println("✓ Added 8 sample books to the catalogue");
    }

    /**
     * Adds sample loans (active and returned).
     */
    private void addSampleLoans() {
        // Active loans
        Loan loan1 = new Loan("LN-0001", "BK-0001", "USR-0003", "USR-0002");
        loanRepository.save(loan1);

        Loan loan2 = new Loan("LN-0002", "BK-0003", "USR-0004", "USR-0002");
        loanRepository.save(loan2);

        // Returned loan
        Loan loan3 = new Loan("LN-0003", "BK-0005", "USR-0005", "USR-0002", 
                              LocalDate.now().minusDays(20), 
                              LocalDate.now().minusDays(6),
                              LocalDate.now().minusDays(2),
                              Loan.STATUS_RETURNED, 
                              0.0, true, "Returned on time");
        loanRepository.save(loan3);

        System.out.println("✓ Added 3 sample loans (2 active, 1 returned)");
    }

    /**
     * Gets the populated repositories.
     */
    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public LoanRepository getLoanRepository() {
        return loanRepository;
    }
}
