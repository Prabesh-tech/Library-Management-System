package view;

import javax.swing.*;
import controller.UserController;
import exception.InvalidInputException;
import exception.UserNotFoundException;

/**
 * PasswordResetView.java
 * Dialog for user password recovery with a verification code.
 */
public class PasswordResetView extends JDialog {

    private static final long serialVersionUID = 1L;

    private final UserController userController;
    private final JFrame parent;
    private JTextField identifierField;
    private JTextField codeField;
    private JPasswordField newPasswordField;
    private JButton sendCodeButton;
    private JButton resetButton;
    private JLabel statusLabel;

    public PasswordResetView(JFrame parent, UserController userController) {
        super(parent, "Password Reset", true);
        this.parent = parent;
        this.userController = userController;
        initializeUI();
    }

    private void initializeUI() {
        setSize(420, 300);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel identifierLabel = new JLabel("Email address:");
        identifierLabel.setBounds(30, 20, 140, 25);
        identifierField = new JTextField();
        identifierField.setBounds(180, 20, 200, 25);
        panel.add(identifierLabel);
        panel.add(identifierField);

        sendCodeButton = new JButton("Send verification code");
        sendCodeButton.setBounds(100, 55, 220, 30);
        sendCodeButton.addActionListener(e -> sendVerificationCode());
        panel.add(sendCodeButton);

        JLabel codeLabel = new JLabel("Verification code:");
        codeLabel.setBounds(30, 100, 140, 25);
        codeField = new JTextField();
        codeField.setBounds(180, 100, 200, 25);
        panel.add(codeLabel);
        panel.add(codeField);

        JLabel passwordLabel = new JLabel("New password:");
        passwordLabel.setBounds(30, 145, 140, 25);
        newPasswordField = new JPasswordField();
        newPasswordField.setBounds(180, 145, 200, 25);
        panel.add(passwordLabel);
        panel.add(newPasswordField);

        resetButton = new JButton("Reset password");
        resetButton.setBounds(120, 190, 180, 30);
        resetButton.addActionListener(e -> resetPassword());
        panel.add(resetButton);

        statusLabel = new JLabel("");
        statusLabel.setBounds(30, 230, 360, 25);
        statusLabel.setForeground(java.awt.Color.RED);
        panel.add(statusLabel);

        add(panel);
    }

    private void sendVerificationCode() {
        String identifier = identifierField.getText().trim();
        if (identifier.isEmpty()) {
            statusLabel.setText("Email address is required.");
            return;
        }

        try {
            String code = userController.sendResetCode(identifier);
            statusLabel.setText("Verification code sent. Check your email.");
            statusLabel.setForeground(java.awt.Color.GREEN);
            JOptionPane.showMessageDialog(this,
                    "Verification code generated:\n" + code,
                    "Reset Code",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (UserNotFoundException ex) {
            statusLabel.setText("No account found for that email.");
            statusLabel.setForeground(java.awt.Color.RED);
        } catch (InvalidInputException ex) {
            statusLabel.setText(ex.getMessage());
            statusLabel.setForeground(java.awt.Color.RED);
        }
    }

    private void resetPassword() {
        String identifier = identifierField.getText().trim();
        String code = codeField.getText().trim();
        String newPassword = new String(newPasswordField.getPassword()).trim();

        if (identifier.isEmpty() || code.isEmpty() || newPassword.isEmpty()) {
            statusLabel.setText("All fields are required.");
            statusLabel.setForeground(java.awt.Color.RED);
            return;
        }

        try {
            if (userController.resetPasswordWithCode(identifier, code, newPassword)) {
                statusLabel.setText("Password successfully reset.");
                statusLabel.setForeground(java.awt.Color.GREEN);
                JOptionPane.showMessageDialog(this,
                        "Your password has been reset successfully.",
                        "Password Reset",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                statusLabel.setText("Password reset failed. Try again.");
                statusLabel.setForeground(java.awt.Color.RED);
            }
        } catch (UserNotFoundException ex) {
            statusLabel.setText("No account found for that email.");
            statusLabel.setForeground(java.awt.Color.RED);
        } catch (InvalidInputException ex) {
            statusLabel.setText(ex.getMessage());
            statusLabel.setForeground(java.awt.Color.RED);
        }
    }
}
