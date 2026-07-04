package util;

import javax.swing.*;
import java.awt.*;

/**
 * StyleUtil.java
 * Provides common styling constants and theme setup for the Swing UI.
 */
public class StyleUtil {

    public static final Color BACKGROUND_COLOR = new Color(245, 248, 255);
    public static final Color CARD_COLOR = new Color(255, 255, 255);
    public static final Color PRIMARY_COLOR = new Color(37, 99, 235);
    public static final Color SECONDARY_COLOR = new Color(51, 65, 85);
    public static final Color TEXT_COLOR = new Color(15, 23, 42);
    public static final Color ERROR_COLOR = new Color(220, 38, 38);

    public static final Font DEFAULT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public static void applyGlobalTheme() {
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("Label.font", DEFAULT_FONT);
        UIManager.put("Label.foreground", TEXT_COLOR);
        UIManager.put("Button.font", BUTTON_FONT);
        UIManager.put("Button.background", PRIMARY_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.border", BorderFactory.createEmptyBorder(8, 16, 8, 16));
        UIManager.put("TextField.font", DEFAULT_FONT);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", TEXT_COLOR);
        UIManager.put("PasswordField.font", DEFAULT_FONT);
        UIManager.put("PasswordField.background", Color.WHITE);
        UIManager.put("PasswordField.foreground", TEXT_COLOR);
        UIManager.put("CheckBox.font", DEFAULT_FONT);
        UIManager.put("CheckBox.background", BACKGROUND_COLOR);
        UIManager.put("CheckBox.foreground", TEXT_COLOR);
    }

    public static void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }
}
