package app;

import view.LoginView;
import util.DataManager;
import javax.swing.UIManager;


/**
 * Main.java
 * Entry point for the Library Management System application.
 * Launches the login interface on startup.
 */
public class Main {
    
    public static void main(String[] args) {
        // Try to initialize FlatLaf look-and-feel (fall back to system LAF)
        // Try to initialize FlatLaf via reflection; if not available use system LAF
        try {
            Class<?> light = Class.forName("com.formdev.flatlaf.FlatLightLaf");
            java.lang.reflect.Method m = light.getMethod("setup");
            m.invoke(null);
        } catch (ClassNotFoundException cnf) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // ignore and continue
            }
        } catch (Exception ex) {
            // ignore other reflection errors and continue
        }

        // Initialize system with demo data
        DataManager.getInstance().initializeWithDemoData();

        // Launch the application on the Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}
