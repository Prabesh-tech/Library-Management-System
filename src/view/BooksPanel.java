package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import controller.BookController;
import model.Book;
import model.User;
import service.AuthService;

public class BooksPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final User currentUser;
    private final BookController bookController;
    private final AuthService authService;
    private final JTable booksTable;
    private final DefaultTableModel tableModel;
    private final JTextField searchField;

    public BooksPanel(User user) {
        this.currentUser = user;
        this.bookController = new BookController();
        this.authService = new AuthService();

        setLayout(new BorderLayout(12, 12));

        JLabel lbl = new JLabel("Books");
        lbl.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lbl, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel(new BorderLayout(8, 8));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));

        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(24);
        searchPanel.add(searchField);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchBooks());
        searchPanel.add(searchButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshBooks());
        searchPanel.add(refreshButton);

        controlPanel.add(searchPanel, BorderLayout.WEST);

        JButton openManagerButton = new JButton("Open Book Manager");
        openManagerButton.setEnabled(authService.hasPermission(currentUser, "manage_books"));
        openManagerButton.setToolTipText(authService.hasPermission(currentUser, "manage_books")
                ? "Open the book manager for add/update/delete operations"
                : "Open book search and borrowing options"
        );
        openManagerButton.addActionListener(e -> openBookManager());
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actionPanel.add(openManagerButton);
        controlPanel.add(actionPanel, BorderLayout.EAST);

        add(controlPanel, BorderLayout.PAGE_START);

        tableModel = new DefaultTableModel(
            new Object[] { "ID", "Title", "Author", "Genre", "ISBN", "Available", "Total", "Location" }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        booksTable = new JTable(tableModel);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(booksTable), BorderLayout.CENTER);

        JTextArea footer = new JTextArea(
            "Use Search or Refresh to load books. Click Open Book Manager to manage catalogue entries."
        );
        footer.setEditable(false);
        footer.setBackground(getBackground());
        footer.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        add(footer, BorderLayout.SOUTH);

        refreshBooks();
    }

    private void refreshBooks() {
        loadBooks(bookController.getAllBooks());
    }

    private void searchBooks() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            refreshBooks();
            return;
        }
        loadBooks(bookController.searchBooks(query));
    }

    private void loadBooks(List<Book> books) {
        tableModel.setRowCount(0);
        if (books == null) {
            return;
        }

        for (Book book : books) {
            tableModel.addRow(new Object[] {
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getIsbn(),
                book.getAvailableCopies(),
                book.getTotalCopies(),
                book.getLocation()
            });
        }
    }

    private void openBookManager() {
        SwingUtilities.invokeLater(() -> new BookView(currentUser).setVisible(true));
    }
}
