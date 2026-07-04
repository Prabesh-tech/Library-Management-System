<<<<<<< HEAD
# Library Management System

A complete Java-based Library Management System with GUI, featuring user authentication, book catalogue management, and loan tracking.

## Project Structure

```
Library Management System/
├── src/
│   ├── app/
│   │   └── Main.java                 # Application entry point
│   ├── config/
│   │   └── AppConfig.java            # System configuration constants
│   ├── model/                        # Data entities
│   │   ├── User.java                 # Abstract user base class
│   │   ├── Admin.java                # Admin role
│   │   ├── Librarian.java            # Librarian role
│   │   ├── Student.java              # Student role
│   │   ├── Book.java                 # Book entity
│   │   └── Loan.java                 # Loan/transaction entity
│   ├── controller/                   # Request handlers
│   │   ├── BookController.java       # Book operations
│   │   ├── LoanController.java       # Loan operations
│   │   └── UserController.java       # User management & auth
│   ├── service/                      # Business logic
│   │   ├── BookService.java
│   │   ├── LoanService.java
│   │   ├── UserService.java
│   │   └── AuthService.java
│   ├── repository/                   # Data access layer
│   │   ├── BookRepository.java
│   │   ├── LoanRepository.java
│   │   └── UserRepository.java
│   ├── view/                         # Swing GUI
│   │   ├── LoginView.java            # Login screen
│   │   ├── DashboardView.java        # Main dashboard
│   │   ├── BookView.java             # Book catalogue
│   │   ├── IssueReturnView.java      # Issue/return interface
│   │   └── UserView.java             # User management
│   ├── util/                         # Utilities
│   │   ├── IDGenerator.java          # Unique ID generation
│   │   ├── ValidationUtil.java       # Input validation
│   │   ├── FileHandler.java          # File I/O
│   │   ├── DBConnection.java         # DB placeholder
│   │   ├── DataManager.java          # Singleton repository manager
│   │   └── SystemInitializer.java    # Demo data setup
│   └── exception/                    # Custom exceptions
│       ├── BookNotFoundException.java
│       ├── UserNotFoundException.java
│       └── InvalidInputException.java
├── data/                             # Data storage (for file-based persistence)
├── docs/                             # Documentation
└── README.md                         # This file
```

## System Architecture

### Three-Layer Architecture
1. **Presentation Layer** (View) - Swing GUI components
2. **Business Logic Layer** (Service/Controller) - Core operations
3. **Data Access Layer** (Repository) - In-memory storage

### Key Features

#### User Roles
- **Admin**: Full system access (user/book management, reports)
- **Librarian**: Catalogue and loan management
- **Student**: Search books, borrow, view loans

#### Book Management
- Add/Update/Delete books
- Track inventory (total vs available copies)
- Search by title, author, ISBN
- Soft-delete support

#### Loan System
- Issue books with automatic due date (14 days)
- Return books with fine calculation
- Track overdue loans
- Fine management ($5/day)
- Borrowing limit: 5 books per student

#### Authentication
- Email-based login
- Role-based access control
- Session tracking

## Default Credentials

### Demo Accounts (auto-generated on startup)
```
Admin:
  Email: admin@library.com
  Password: admin123

Librarian:
  Email: librarian@library.com
  Password: lib123

Student 1:
  Email: ahmed@university.edu
  Password: pass123

Student 2:
  Email: fatima@university.edu
  Password: pass456

Student 3:
  Email: ali@university.edu
  Password: pass789
```

## Configuration

Edit `src/config/AppConfig.java` to modify:
- `LOAN_PERIOD_DAYS` - Borrowing period (default: 14 days)
- `FINE_PER_DAY` - Overdue fine amount (default: $5)
- `MAX_BORROW_LIMIT` - Max books per student (default: 5)
- File storage paths and UI window size

## Building, Running & Testing

### Compile
```bash
javac -d bin src/**/*.java
```

### Run
```bash
java -cp bin app.Main
```

### Run tests
```bash
java -cp bin test.PasswordResetTests
java -cp bin test.PermissionTests
java -cp bin test.BorrowPermissionTests
```

### Or use IDE
- Open in IntelliJ, Eclipse, or VS Code
- Right-click `Main.java` → Run
- Or run the test classes directly from the IDE

## Data Storage

Currently uses **in-memory storage** via repositories:
- Data persists during runtime only
- Automatically loads demo data on startup
- Can be replaced with file I/O or database backend

### Future: File-Based Storage
```
data/
├── users.txt      # Serialized User objects (pipe-delimited)
├── books.txt      # Serialized Book objects
├── loans.txt      # Serialized Loan objects
└── logs.txt       # System logs
```

### Future: Database Storage
- Replace repositories with JDBC/Hibernate implementation
- Connect `DBConnection.java` to actual database

## Usage Examples

### Login as Admin
1. Email: `admin@library.com`
2. Password: `admin123`
3. Access: Full system admin panel

### Borrow a Book (as Student)
1. Login with student credentials
2. Search books by title/author
3. Click "Borrow" → System auto-calculates due date
4. Book marked as issued in student's loan list

### Issue Book as Librarian
1. Login: `librarian@library.com`
2. Use "Issue/Return" tab
3. Enter Book ID and Student ID
4. System updates inventory automatically

### Calculate Fines
- Fines calculated automatically on book return
- Overdue days × $5 per day
- Students cannot borrow until fines are cleared

## Class Responsibilities

| Class | Responsibility |
|-------|-----------------|
| `User`, `Admin`, `Librarian`, `Student` | Data models with role-specific behavior |
| `Book` | Book catalogue with inventory tracking |
| `Loan` | Loan transaction with fine calculation |
| `*Controller` | Handle view requests, validate input |
| `*Service` | Apply business rules, call repositories |
| `*Repository` | CRUD operations on data |
| `DataManager` | Provide shared repository access (singleton) |
| `IDGenerator` | Generate unique IDs (USR-XXXX, BK-XXXX, LN-XXXX) |
| `ValidationUtil` | Validate email, password, ISBN, etc. |
| `FileHandler` | Read/write file operations |
| `SystemInitializer` | Load demo data |

## Exception Handling

Three custom exceptions for clear error reporting:
- `BookNotFoundException` - When book ID not found
- `UserNotFoundException` - When user/email not found
- `InvalidInputException` - Validation failures

## Testing Demo Workflow

1. **Start App** → Auto-loads 5 demo users, 8 books, 3 loans
2. **Login as Ahmed (Student)** → Search and borrow books
3. **Login as Jane (Librarian)** → Issue/return books, manage fines
4. **Login as Admin** → View all users, manage system

## Future Enhancements

- [ ] Persistent file/database storage
- [ ] Email notifications for overdue books
- [ ] Reports and analytics
- [ ] Fine payment processing
- [ ] Book reservations
- [ ] ISBN barcode scanning
- [ ] Admin audit logs
- [ ] Multi-library support
- [ ] Advanced search (filters, sorting)
- [ ] Member card generation

## Technical Stack

- **Language**: Java 8+
- **UI**: Swing
- **Data Storage**: In-memory (HashMap) - replaceable
- **Architecture**: MVC (Model-View-Controller)
- **Design Pattern**: Singleton (DataManager)

## Notes

- All times are in local timezone
- IDs auto-generated (never manually entered)
- Soft-delete supported (books/users marked inactive)
- No external dependencies (pure Java)
- Thread-safe ID generation via AtomicInteger

## Support

For issues or enhancements, refer to:
- Exception messages for error details
- System console output for debug info
- `src/util/SystemInitializer.java` for modifying demo data
=======
# Library-Management-System
A simple, library management system with java and MySQL
>>>>>>> 9606a2622c594c49a19d3d0bbc11ffbe655cfb8c
