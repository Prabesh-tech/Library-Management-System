package view;

import javax.swing.*;
import config.AppConfig;
import controller.UserController;
import model.User;
import exception.InvalidInputException;
import service.AuthService;
import util.IDGenerator;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * UserView.java
 * Interface for managing user accounts (admin only).
 */
public class UserView extends JFrame {

    private static final long serialVersionUID = 1L;
    
    private UserController userController;
    private User currentUser;
    private JTable usersTable;

    public UserView(User currentUser) {
        this.currentUser = currentUser;
        this.userController = new UserController();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("User Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Filter by role
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(50, 20, 100, 25);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"ALL", "ADMIN", "LIBRARIAN", "TEACHER", "STAFF", "STUDENT"});
        roleCombo.setBounds(150, 20, 150, 25);
        panel.add(roleLabel);
        panel.add(roleCombo);

        // Users table (placeholder)
        usersTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setBounds(50, 70, 900, 330);
        panel.add(scrollPane);

        // Action buttons
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBounds(50, 420, 100, 30);
        refreshButton.addActionListener(e -> {
            try {
                loadUsers((String) roleCombo.getSelectedItem());
                JOptionPane.showMessageDialog(this, "Users refreshed.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error refreshing: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(refreshButton);

        JButton addButton = new JButton("Add User");
        addButton.setBounds(160, 420, 100, 30);
        addButton.addActionListener(e -> {
            try {
                showAddUserDialog();
                loadUsers((String) roleCombo.getSelectedItem());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(addButton);

        JButton editButton = new JButton("Edit User");
        editButton.setBounds(270, 420, 100, 30);
        editButton.addActionListener(e -> {
            try {
                showEditUserDialog();
                loadUsers((String) roleCombo.getSelectedItem());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error editing user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(editButton);

        JButton deleteButton = new JButton("Delete User");
        deleteButton.setBounds(380, 420, 110, 30);
        deleteButton.addActionListener(e -> {
            try {
                showDeleteUserDialog();
                loadUsers((String) roleCombo.getSelectedItem());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(deleteButton);

        JButton closeButton = new JButton("Close");
        closeButton.setBounds(850, 420, 100, 30);
        closeButton.addActionListener(e -> dispose());
        panel.add(closeButton);

        add(panel);

        // initial load
        loadUsers("ALL");
    }

    /** Load users into the table, optionally filtering by role string 'ALL' to include everyone. */
    private void loadUsers(String roleFilter) {
        List<User> users = userController.getAllUsers();
        String[] cols = new String[]{"Library ID", "Name", "Email", "Role", "Access Level", "Active"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        for (User u : users) {
            if (!"ALL".equalsIgnoreCase(roleFilter) && roleFilter != null) {
                if (!u.getRole().equalsIgnoreCase(roleFilter)) continue;
            }
            model.addRow(new Object[]{u.getLibraryId(), u.getName(), u.getEmail(), u.getRole(), u.getAccessLevel(), u.isActive()});
        }
        usersTable.setModel(model);
    }

    private void showEditUserDialog() {
        int row = usersTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String libId = (String) usersTable.getValueAt(row, 0);
        User user = userController.getUserByLibraryId(libId);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Selected user not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel form = new JPanel(new GridLayout(0,2,8,8));
        JTextField nameField = new JTextField(user.getName() != null ? user.getName() : "");
        JTextField emailField = new JTextField(user.getEmail() != null ? user.getEmail() : "");
        JTextField phoneField = new JTextField(user.getPhone() != null ? user.getPhone() : "");
        JTextField addressField = new JTextField(user.getAddress() != null ? user.getAddress() : "");
        java.util.List<String> roles = new ArrayList<>(Arrays.asList("STUDENT", "TEACHER", "STAFF"));
        boolean isAdminOrSuperAdmin = currentUser != null && currentUser.getAccessLevel() >= AppConfig.ACCESS_ADMIN;
        if (isAdminOrSuperAdmin) {
            roles.add("LIBRARIAN");
            roles.add("ADMIN");
            roles.add("SUPERADMIN");
        }
        if (!roles.contains(user.getRole())) {
            roles.add(user.getRole());
        }
        JComboBox<String> roleField = new JComboBox<>(roles.toArray(new String[0]));
        roleField.setSelectedItem(user.getRole());
        JComboBox<Integer> accessField = new JComboBox<>(new Integer[]{0,1,2,3,4,5});
        accessField.setSelectedItem(user.getAccessLevel());
        JCheckBox activeField = new JCheckBox("Active", user.isActive());

        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Email:")); form.add(emailField);
        form.add(new JLabel("Phone:")); form.add(phoneField);
        form.add(new JLabel("Address:")); form.add(addressField);
        form.add(new JLabel("Role:")); form.add(roleField);
        form.add(new JLabel("Access Level:")); form.add(accessField);
        form.add(new JLabel("")); form.add(activeField);

        int result = JOptionPane.showConfirmDialog(this, form, "Edit User",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;

        String newName = nameField.getText().trim();
        String newEmail = emailField.getText().trim();
        
        if (newName.isEmpty() || newEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Email cannot be empty.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        user.setName(newName);
        user.setEmail(newEmail);
        user.setPhone(phoneField.getText().trim());
        user.setAddress(addressField.getText().trim());
        user.setRole((String) roleField.getSelectedItem());
        user.setAccessLevel((Integer) accessField.getSelectedItem());
        user.setActive(activeField.isSelected());

        try {
            boolean ok = userController.updateUser(user);
            if (ok) {
                JOptionPane.showMessageDialog(this, "User updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void showAddUserDialog() {
        if (currentUser != null && !new AuthService().hasPermission(currentUser, "manage_users")) {
            JOptionPane.showMessageDialog(this, "Only librarians and admins can add new users.", "Permission Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField libraryIdField = new JTextField();
        java.util.List<String> roles = new ArrayList<>(Arrays.asList("STUDENT", "TEACHER", "STAFF"));
        boolean isAdminOrSuperAdmin = currentUser != null && currentUser.getAccessLevel() >= AppConfig.ACCESS_ADMIN;
        if (isAdminOrSuperAdmin) {
            roles.add("LIBRARIAN");
            roles.add("ADMIN");
            roles.add("SUPERADMIN");
        }
        JComboBox<String> roleField = new JComboBox<>(roles.toArray(new String[0]));

        char defaultEchoChar = new JPasswordField().getEchoChar();
        JPasswordField passwordField = new JPasswordField();
        JToggleButton showPassword = new JToggleButton("Show");
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char)0);
                showPassword.setText("Hide");
            } else {
                passwordField.setEchoChar(defaultEchoChar);
                showPassword.setText("Show");
            }
        });
        JPanel passwordPanel = new JPanel(new BorderLayout(4, 0));
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(showPassword, BorderLayout.EAST);

        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Email:")); form.add(emailField);
        form.add(new JLabel("Library ID:")); form.add(libraryIdField);
        form.add(new JLabel("Password:")); form.add(passwordPanel);
        form.add(new JLabel("Role:")); form.add(roleField);

        int result = JOptionPane.showConfirmDialog(this, form, "Add New User",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String libraryId = libraryIdField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleField.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || libraryId.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.",
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String userId = IDGenerator.generateUserId();
            User newUser = null;
            
            if ("ADMIN".equals(role) || "SUPERADMIN".equals(role)) {
                if (!isAdminOrSuperAdmin) {
                    JOptionPane.showMessageDialog(this, "Only admins and superadmins can create admin accounts.", "Permission Denied", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                newUser = userController.createAdmin(userId, libraryId, name, email, password);
                JOptionPane.showMessageDialog(this, role.toLowerCase() + " account created successfully: " + libraryId,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else if ("LIBRARIAN".equals(role)) {
                if (!isAdminOrSuperAdmin) {
                    JOptionPane.showMessageDialog(this, "Only admins and superadmins can create librarian accounts.", "Permission Denied", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                newUser = userController.createLibrarian(userId, name, email, password);
                if (newUser != null) {
                    newUser.setLibraryId(libraryId);
                    userController.updateUser(newUser);
                }
                JOptionPane.showMessageDialog(this, "Librarian account created successfully: " + libraryId,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else if ("TEACHER".equals(role)) {
                newUser = userController.createTeacher(userId, name, email, password);
                if (newUser != null) {
                    newUser.setLibraryId(libraryId);
                    newUser.setRole("TEACHER");
                    newUser.setAccessLevel(AppConfig.ACCESS_TEACHER);
                    userController.updateUser(newUser);
                }
                JOptionPane.showMessageDialog(this, "Teacher account created successfully: " + libraryId,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else if ("STAFF".equals(role)) {
                newUser = userController.createStaff(userId, name, email, password);
                if (newUser != null) {
                    newUser.setLibraryId(libraryId);
                    newUser.setRole("STAFF");
                    newUser.setAccessLevel(AppConfig.ACCESS_STAFF);
                    userController.updateUser(newUser);
                }
                JOptionPane.showMessageDialog(this, "Staff account created successfully: " + libraryId,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                newUser = userController.createStudent(userId, name, email, password);
                if (newUser != null) {
                    newUser.setLibraryId(libraryId);
                    newUser.setAccessLevel(AppConfig.ACCESS_STUDENT);
                    userController.updateUser(newUser);
                }
                JOptionPane.showMessageDialog(this, "Student account created successfully: " + libraryId,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error creating user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showDeleteUserDialog() {
        int row = usersTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String libId = (String) usersTable.getValueAt(row, 0);
        String name = (String) usersTable.getValueAt(row, 1);
        String role = (String) usersTable.getValueAt(row, 3);

        // Prevent deleting super admin or current user
        if ("SUPERADMIN".equalsIgnoreCase(role)) {
            JOptionPane.showMessageDialog(this, "Cannot delete SuperAdmin accounts.", "Permission Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (currentUser != null && currentUser.getLibraryId().equals(libId)) {
            JOptionPane.showMessageDialog(this, "Cannot delete your own account.", "Invalid Operation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to permanently delete user:\n\n" + name + " (" + libId + ")\n\nThis cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            User user = userController.getUserByLibraryId(libId);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean deleted = userController.deleteUser(user.getUserId());
            if (deleted) {
                JOptionPane.showMessageDialog(this, "User '" + name + "' deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete user. User may not exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
