package view;

import javax.swing.*;
import controller.UserController;
import model.User;
import exception.UserNotFoundException;
import exception.InvalidInputException;

/**
 * LoginView.java
 * Provides the login interface for the Library Management System.
 */
public class LoginView extends JFrame {

    private static final long serialVersionUID = 1L;
    
    private UserController userController;
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton forgotButton;
    private JLabel statusLabel;

    public LoginView() {
        this.userController = new UserController();
        initializeUI();
    }

    /**
     * Initializes the login UI components.
     */
    private void initializeUI() {
        setTitle("Library Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Login identifier label and field (Email or Library ID)
        JLabel idLabel = new JLabel("Email or Library ID:");
        idLabel.setBounds(50, 30, 120, 25);
        idField = new JTextField();
        idField.setBounds(170, 30, 180, 25);
        panel.add(idLabel);
        panel.add(idField);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 70, 100, 25);
        passwordField = new JPasswordField();
        passwordField.setBounds(150, 70, 160, 25);
        panel.add(passwordLabel);
        panel.add(passwordField);

        JCheckBox showPasswordCheck = new JCheckBox("Show");
        showPasswordCheck.setBounds(320, 70, 70, 25);
        char defaultEchoChar = passwordField.getEchoChar();
        showPasswordCheck.addActionListener(e -> {
            if (showPasswordCheck.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar(defaultEchoChar);
            }
        });
        panel.add(showPasswordCheck);

        // Forgot password button
        forgotButton = new JButton("Forgot password?");
        forgotButton.setBounds(50, 120, 170, 30);
        forgotButton.addActionListener(e -> openPasswordReset());
        panel.add(forgotButton);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setBounds(230, 120, 100, 30);
        loginButton.addActionListener(e -> handleLogin());
        panel.add(loginButton);

        // Status label
        statusLabel = new JLabel("");
        statusLabel.setBounds(50, 160, 320, 20);
        statusLabel.setForeground(java.awt.Color.RED);
        panel.add(statusLabel);

        add(panel);
    }

    /**
     * Handles the login button click event.
     */
    private void handleLogin() {
        String identifier = idField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (identifier.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Email/Library ID and password are required");
            statusLabel.setForeground(java.awt.Color.RED);
            return;
        }

        try {
            User user = userController.loginByIdOrEmail(identifier, password);
            statusLabel.setText("Login successful!");
            statusLabel.setForeground(java.awt.Color.GREEN);
            
            // Open main application frame and close login window
            dispose();
            new MainFrame(user).setVisible(true);
            
        } catch (UserNotFoundException ex) {
            statusLabel.setText("Invalid email/Library ID or password");
            statusLabel.setForeground(java.awt.Color.RED);
        } catch (InvalidInputException ex) {
            statusLabel.setText(ex.getMessage());
            statusLabel.setForeground(java.awt.Color.RED);
        }
    }

    private void openPasswordReset() {
        new PasswordResetView(this, userController).setVisible(true);
    }
}
