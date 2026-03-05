// ThemeManager.java
//
// Expanded compatibility ThemeManager for OIBSIP_JAVA DEVELOPMENT_TASK-4.
// This file intentionally provides the utility methods that the existing
// ModernExamUI implementation expects, so we can compile your project
// without touching many UI sources at once.
//
// Save as UTF-8 without BOM.
package com.vr.onlineexam.util;
import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;

/**
 * Backwards-compatible ThemeManager.
 *
 * Provides:
 *  - init()
 *  - isDark(), setDark(boolean), toggle()
 *  - apply() to re-apply theme defaults
 *  - applyToComponentTree(JFrame) to refresh a window
 *  - uiFont(float, int) convenience
 *
 * This file intentionally avoids advanced dependencies and is defensive:
 * any exceptions are caught and printed but won't stop the app.
 */
public class ThemeManager {

    private static boolean dark = false;
    private static final String BASE_FONT_FAMILY = "Segoe UI";

    /** Safe init called from Main. */
    public static void init() {
        try {
            // Base defaults
            UIManager.put("Label.font", uiFont(13f, Font.PLAIN));
            UIManager.put("Button.font", uiFont(13f, Font.PLAIN));
            UIManager.put("TextField.font", uiFont(13f, Font.PLAIN));
            UIManager.put("TextArea.font", uiFont(13f, Font.PLAIN));
            UIManager.put("List.font", uiFont(13f, Font.PLAIN));
            UIManager.put("Table.font", uiFont(13f, Font.PLAIN));
            UIManager.put("Menu.font", uiFont(13f, Font.PLAIN));
            UIManager.put("PopupMenu.border", BorderFactory.createEmptyBorder(6, 6, 6, 6));
            UIManager.put("Button.margin", new Insets(6, 12, 6, 12));
            dark = false; // default
            apply(); // apply initial values
        } catch (Exception ex) {
            System.err.println("ThemeManager.init() warning: " + ex.getMessage());
        }
    }

    /** Return whether dark mode is enabled. */
    public static boolean isDark() {
        return dark;
    }

    /** Set dark mode on/off. */
    public static void setDark(boolean on) {
        dark = on;
    }

    /** Toggle dark mode state. */
    public static void toggle() {
        dark = !dark;
    }

    /** Re-apply theme values to UIManager defaults. Call before repainting frames. */
    public static void apply() {
        try {
            if (dark) {
                UIManager.put("Panel.background", new Color(28, 30, 33));
                UIManager.put("Label.foreground", Color.WHITE);
                UIManager.put("Button.background", new Color(48, 50, 54));
                UIManager.put("Button.foreground", Color.WHITE);
                UIManager.put("TextField.background", new Color(44, 46, 50));
                UIManager.put("TextField.foreground", Color.WHITE);
                UIManager.put("TextArea.background", new Color(44, 46, 50));
                UIManager.put("TextArea.foreground", Color.WHITE);
                UIManager.put("List.background", new Color(44, 46, 50));
                UIManager.put("List.foreground", Color.WHITE);
                UIManager.put("Table.background", new Color(44, 46, 50));
                UIManager.put("Table.foreground", Color.WHITE);
                UIManager.put("ScrollBar.background", new Color(38, 40, 44));
            } else {
                UIManager.put("Panel.background", Color.WHITE);
                UIManager.put("Label.foreground", Color.BLACK);
                UIManager.put("Button.background", new Color(235, 240, 245));
                UIManager.put("Button.foreground", Color.BLACK);
                UIManager.put("TextField.background", Color.WHITE);
                UIManager.put("TextField.foreground", Color.BLACK);
                UIManager.put("TextArea.background", Color.WHITE);
                UIManager.put("TextArea.foreground", Color.BLACK);
                UIManager.put("List.background", Color.WHITE);
                UIManager.put("List.foreground", Color.BLACK);
                UIManager.put("Table.background", Color.WHITE);
                UIManager.put("Table.foreground", Color.BLACK);
                UIManager.put("ScrollBar.background", new Color(240, 240, 240));
            }
        } catch (Exception ex) {
            System.err.println("ThemeManager.apply() warning: " + ex.getMessage());
        }
    }

    /**
     * Walk the component tree and update backgrounds/foregrounds for immediate effect.
     * Use after calling ThemeManager.apply() when you want a frame to refresh.
     */
    public static void applyToComponentTree(JFrame frame) {
        if (frame == null) return;
        try {
            SwingUtilities.invokeLater(() -> {
                updateComponentRecursively(frame.getContentPane());
                frame.invalidate();
                frame.validate();
                frame.repaint();
            });
        } catch (Exception ex) {
            System.err.println("ThemeManager.applyToComponentTree() warning: " + ex.getMessage());
        }
    }

    /** Helper: recursively update component colors based on dark flag. */
    private static void updateComponentRecursively(Component c) {
        if (c == null) return;
        try {
            if (dark) {
                c.setBackground(getDarkBackground(c));
                c.setForeground(getDarkForeground(c));
            } else {
                c.setBackground(getLightBackground(c));
                c.setForeground(getLightForeground(c));
            }
        } catch (Exception ignored) {
        }

        if (c instanceof Container) {
            for (Component child : ((Container) c).getComponents()) {
                updateComponentRecursively(child);
            }
        }
    }

    /** Resolve dark background for the component type. */
    private static Color getDarkBackground(Component c) {
        if (c instanceof JButton) return new Color(60, 63, 65);
        if (c instanceof JTextArea || c instanceof JTextField) return new Color(44, 46, 50);
        if (c instanceof JList) return new Color(44, 46, 50);
        if (c instanceof JTable) return new Color(44, 46, 50);
        return new Color(34, 36, 38);
    }

    private static Color getDarkForeground(Component c) {
        return Color.WHITE;
    }

    private static Color getLightBackground(Component c) {
        if (c instanceof JButton) return new Color(235, 240, 245);
        if (c instanceof JTextArea || c instanceof JTextField) return Color.WHITE;
        if (c instanceof JList) return Color.WHITE;
        if (c instanceof JTable) return Color.WHITE;
        return Color.WHITE;
    }

    private static Color getLightForeground(Component c) {
        return Color.BLACK;
    }

    /** Convenience method to get a Font with requested size and style. */
    public static Font uiFont(float size, int style) {
        try {
            return new Font(BASE_FONT_FAMILY, style, Math.max(12, Math.round(size)));
        } catch (Exception ex) {
            // fallback
            return new Font("SansSerif", style, Math.max(12, Math.round(size)));
        }
    }
}
