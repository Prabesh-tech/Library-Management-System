package view;

import javax.swing.*;
import controller.LoanController;
import exception.InvalidInputException;
import model.User;
import service.AuthService;
import util.IDGenerator;

/**
 * IssueReturnView.java
 * Interface for librarians to issue and return books.
 */
public class IssueReturnView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTabbedPane tabbedPane;
    private LoanController loanController;
    private AuthService authService;
    private User currentUser;
    private String preselectedBookId;

    public IssueReturnView() {
        this(null, null);
    }

    public IssueReturnView(String preselectedBookId) {
        this(preselectedBookId, null);
    }

    public IssueReturnView(User currentUser) {
        this(null, currentUser);
    }

    public IssueReturnView(String preselectedBookId, User currentUser) {
        this.preselectedBookId = preselectedBookId;
        this.currentUser = currentUser;
        this.loanController = new LoanController();
        this.authService = new AuthService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Issue / Return Books");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        // Issue Tab
        JPanel issuePanel = createIssuePanel();
        tabbedPane.addTab("Issue Book", issuePanel);

        // Return Tab
        JPanel returnPanel = createReturnPanel();
        tabbedPane.addTab("Return Book", returnPanel);

        add(tabbedPane);
    }

    private JPanel createIssuePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel bookIdLabel = new JLabel("Book ID:");
        bookIdLabel.setBounds(50, 30, 100, 25);
        JTextField bookIdField = new JTextField();
        bookIdField.setBounds(150, 30, 200, 25);
        if (preselectedBookId != null) {
            bookIdField.setText(preselectedBookId);
        }
        panel.add(bookIdLabel);
        panel.add(bookIdField);

        JLabel userIdLabel = new JLabel("Student ID:");
        userIdLabel.setBounds(50, 70, 100, 25);
        JTextField userIdField = new JTextField();
        userIdField.setBounds(150, 70, 200, 25);
        panel.add(userIdLabel);
        panel.add(userIdField);

        JButton issueButton = new JButton("Issue Book");
        issueButton.setBounds(150, 120, 150, 40);
        issueButton.addActionListener(e -> {
            String bookId = bookIdField.getText().trim();
            String userId = userIdField.getText().trim();
            if (bookId.isEmpty() || userId.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Book ID and Student ID are required.",
                        "Invalid input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (currentUser != null && !authService.canBorrowForOtherUser(currentUser)) {
                String currentUserBorrowerId = currentUser.getLibraryId();
                if (currentUserBorrowerId == null || currentUserBorrowerId.trim().isEmpty()) {
                    currentUserBorrowerId = currentUser.getUserId();
                }
                if (!userId.equals(currentUserBorrowerId)) {
                    JOptionPane.showMessageDialog(this,
                            "Only librarians and admins can borrow books for another member.\nYour own ID will be used instead.",
                            "Permission Denied", JOptionPane.WARNING_MESSAGE);
                    userId = currentUserBorrowerId;
                }
            }

            try {
                loanController.issueBook(IDGenerator.generateLoanId(), bookId, userId, currentUser != null ? currentUser.getUserId() : "SYSTEM", currentUser);
                JOptionPane.showMessageDialog(this,
                        "Book issued successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                bookIdField.setText("");
                userIdField.setText("");
            } catch (InvalidInputException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Borrow failed", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Unable to issue book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(issueButton);

        return panel;
    }

    private JPanel createReturnPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel loanIdLabel = new JLabel("Loan ID:");
        loanIdLabel.setBounds(50, 30, 100, 25);
        JTextField loanIdField = new JTextField();
        loanIdField.setBounds(150, 30, 200, 25);
        panel.add(loanIdLabel);
        panel.add(loanIdField);

        JButton returnButton = new JButton("Return Book");
        returnButton.setBounds(150, 100, 150, 40);
        returnButton.addActionListener(e -> {
            String loanId = loanIdField.getText().trim();
            if (loanId.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Loan ID is required.",
                        "Invalid input", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                model.Loan loan = loanController.returnBook(loanId, "Return Desk");
                if (loan == null) {
                    JOptionPane.showMessageDialog(this,
                            "Loan not found.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String message = "Book returned successfully.";
                    if (loan.getFine() > 0) {
                        message += " Fine due: " + loan.getFine();
                    }
                    JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
                    loanIdField.setText("");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Unable to return book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(returnButton);

        return panel;
    }
}
