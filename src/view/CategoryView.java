package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import service.CategoryService;
import service.AuthService;
import model.User;

public class CategoryView extends JFrame {

    private final CategoryService categoryService;
    private final AuthService authService;
    private final DefaultTableModel tableModel;
    private final JTextField categoryField;

    public CategoryView(User user) {
        this.categoryService = new CategoryService();
        this.authService = new AuthService();
        boolean canManage = user != null && authService.hasPermission(user, "manage_books");

        setTitle("Category Manager");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel title = new JLabel("Manage categories");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        content.add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Category", "Books"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        content.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        categoryField = new JTextField(20);
        JButton addButton = new JButton("Add Category");
        addButton.setEnabled(canManage);
        addButton.setToolTipText(canManage ? "Add a new category" : "Only librarians and admins can add categories");
        addButton.addActionListener(e -> {
            String name = categoryField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a category name", "Empty Category", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (categoryService.categoryExists(name)) {
                JOptionPane.showMessageDialog(this, "Category already exists", "Duplicate Category", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (categoryService.addCategory(name, "")) {
                categoryField.setText("");
                refreshTable();
                JOptionPane.showMessageDialog(this, "Category added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add category", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshTable());

        JButton removeButton = new JButton("Remove Selected");
        removeButton.setEnabled(canManage);
        removeButton.setToolTipText(canManage ? "Remove selected category" : "Only librarians and admins can delete categories");
        removeButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select a category to remove", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String category = (String) tableModel.getValueAt(selectedRow, 0);
            int bookCount = (int) tableModel.getValueAt(selectedRow, 1);
            
            if (bookCount > 0) {
                JOptionPane.showMessageDialog(this, "Cannot delete category with books. Please reassign books first.", "Category In Use", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (categoryService.deleteCategory(category)) {
                refreshTable();
                JOptionPane.showMessageDialog(this, "Category deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete category", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        formPanel.add(new JLabel("New category:"));
        formPanel.add(categoryField);
        formPanel.add(addButton);
        formPanel.add(refreshButton);
        formPanel.add(removeButton);
        content.add(formPanel, BorderLayout.SOUTH);

        add(content);
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        LinkedHashMap<String, Integer> categories = categoryService.getAllCategoriesWithCounts();
        for (Map.Entry<String, Integer> entry : categories.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }
}
