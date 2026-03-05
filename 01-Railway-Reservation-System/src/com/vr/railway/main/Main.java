package com.venkatram.oibsip.task1;

import javax.swing.*;

/**
 * Main launcher - starts the ModernReservation UI
 * Author: Venkatram
 */
public class Main {
    public static void main(String[] args) {
        // best-effort system look and feel
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

        // initialize theme (loads stored preference)
        try { ThemeManager.init(); } catch (Throwable ignored) {}

        SwingUtilities.invokeLater(() -> {
            ModernReservationUI ui = new ModernReservationUI();
            ui.showUI();
        });
    }
}
