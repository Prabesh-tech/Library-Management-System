package view;

import javax.swing.*;
import model.User;

/**
 * DashboardView.java
 * Main dashboard interface after user login.
 */
public class DashboardView extends JFrame {

    private static final long serialVersionUID = 1L;

    public DashboardView(User user) {
        setTitle("Dashboard - " + user.getName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setContentPane(new DashboardPanel(user));
    }
}
