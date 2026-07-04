package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import controller.LoanController;
import model.Loan;
import model.User;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * LoanHistoryView.java
 * Displays the loan history for the current student or member.
 */
public class LoanHistoryView extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final LoanController loanController;
    private final User currentUser;
    private final DefaultTableModel tableModel;

    public LoanHistoryView(User currentUser) {
        this.currentUser = currentUser;
        this.loanController = new LoanController();

        setTitle("My Loan History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 520);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("My Loan History");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        content.add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"Loan ID", "Book ID", "Issue Date", "Due Date", "Return Date", "Status", "Fine", "Fine Paid"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        content.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshLoanHistory());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(refreshButton);
        content.add(bottomPanel, BorderLayout.SOUTH);

        add(content);
        refreshLoanHistory();
    }

    private void refreshLoanHistory() {
        tableModel.setRowCount(0);
        if (currentUser == null) {
            return;
        }

        List<Loan> loans = loanController.getStudentLoans(currentUser.getUserId());
        if (loans == null) {
            return;
        }

        for (Loan loan : loans) {
            tableModel.addRow(new Object[]{
                    loan.getLoanId(),
                    loan.getBookId(),
                    loan.getIssueDate() != null ? loan.getIssueDate().format(DATE_FORMATTER) : "-",
                    loan.getDueDate() != null ? loan.getDueDate().format(DATE_FORMATTER) : "-",
                    loan.getReturnDate() != null ? loan.getReturnDate().format(DATE_FORMATTER) : "-",
                    loan.getStatus(),
                    String.format("%.2f", loan.getFine()),
                    loan.isFinePaid() ? "Yes" : "No"
            });
        }
    }
}
