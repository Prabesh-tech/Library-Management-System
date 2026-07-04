package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import service.AuthorService;
import service.AuthService;
import model.User;

public class AuthorView extends JFrame {

    private final AuthorService authorService;
    private final AuthService authService;
    private final DefaultTableModel tableModel;
    private final JTextField authorField;

    public AuthorView(User user) {
        this.authorService = new AuthorService();
        this.authService = new AuthService();
        boolean canManage = user != null && authService.hasPermission(user, "manage_books");

        setTitle("Author Manager");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Manage authors");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        content.add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Author", "Books"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        content.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        authorField = new JTextField(24);
        JButton addButton = new JButton("Add Author");
        addButton.setEnabled(canManage);
        addButton.setToolTipText(canManage ? "Add a new author" : "Only librarians and admins can add authors");
        addButton.addActionListener(e -> {
            String name = authorField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an author name", "Empty Author", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (authorService.authorExists(name)) {
                JOptionPane.showMessageDialog(this, "Author already exists", "Duplicate Author", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (authorService.addAuthor(name, "")) {
                authorField.setText("");
                refreshTable();
                JOptionPane.showMessageDialog(this, "Author added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add author", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshTable());

        JButton removeButton = new JButton("Remove Selected");
        removeButton.setEnabled(canManage);
        removeButton.setToolTipText(canManage ? "Remove selected author" : "Only librarians and admins can delete authors");
        removeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select an author to remove", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String author = (String) tableModel.getValueAt(selectedRow, 0);
            int bookCount = (int) tableModel.getValueAt(selectedRow, 1);
            
            if (bookCount > 0) {
                JOptionPane.showMessageDialog(this, "Cannot delete author with books. Please reassign books first.", "Author In Use", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (authorService.deleteAuthor(author)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Author deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete author", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(new JLabel("New author:"));
        formPanel.add(authorField);
        formPanel.add(addButton);
        formPanel.add(refreshButton);
        formPanel.add(removeButton);
        content.add(formPanel, BorderLayout.SOUTH);

        add(content);
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        LinkedHashMap<String, Integer> authors = authorService.getAllAuthorsWithCounts();
        for (Map.Entry<String, Integer> entry : authors.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }
}
