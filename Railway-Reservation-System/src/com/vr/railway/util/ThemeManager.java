package com.venkatram.oibsip.task1;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Properties;

/**
 * ThemeManager - simple theme toggling with small persistence.
 * Stores user choice in .theme.properties (working directory)
 */
public class ThemeManager {

    private static boolean dark = false;
    private static final File CFG = new File(".theme.properties");

    public static void init() {
        // load saved choice if exists
        try {
            if (CFG.exists()) {
                Properties p = new Properties();
                try (FileInputStream fis = new FileInputStream(CFG)) {
                    p.load(fis);
                    dark = Boolean.parseBoolean(p.getProperty("dark", "false"));
                }
            }
        } catch (Exception ignored) {}
        apply();
    }

    public static boolean isDark() { return dark; }
    public static void toggle() {
        dark = !dark;
        save();
    }
    public static void setDark(boolean d) {
        dark = d;
        save();
    }

    private static void save() {
        try {
            Properties p = new Properties();
            p.setProperty("dark", Boolean.toString(dark));
            try (FileOutputStream fos = new FileOutputStream(CFG)) {
                p.store(fos, "Theme settings");
            }
        } catch (Exception ignored) {}
    }

    public static void apply() {
        try {
            if (dark) {
                UIManager.put("Panel.background", new Color(28,30,34));
                UIManager.put("Label.foreground", Color.WHITE);
                UIManager.put("Button.background", new Color(50,50,50));
                UIManager.put("Button.foreground", Color.WHITE);
                UIManager.put("TextField.background", new Color(45,47,51));
                UIManager.put("TextField.foreground", Color.WHITE);
            } else {
                UIManager.put("Panel.background", Color.WHITE);
                UIManager.put("Label.foreground", Color.DARK_GRAY);
                UIManager.put("Button.background", new Color(240,240,240));
                UIManager.put("Button.foreground", Color.BLACK);
                UIManager.put("TextField.background", Color.WHITE);
                UIManager.put("TextField.foreground", Color.BLACK);
            }
        } catch (Throwable ignored) {}
    }

    public static void applyToComponentTree(Component root) {
        try {
            SwingUtilities.updateComponentTreeUI(root);
        } catch (Throwable ignored) {}
    }

    public static Font uiFont(float size, int style) {
        return new Font("SansSerif", style, Math.round(size));
    }
}
