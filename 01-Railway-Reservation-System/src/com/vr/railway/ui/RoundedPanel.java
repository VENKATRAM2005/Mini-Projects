package com.venkatram.oibsip.task1;

import javax.swing.*;
import java.awt.*;

/**
 * RoundedPanel - simple rounded background panel used for cards.
 */
public class RoundedPanel extends JPanel {

    private final int radius;

    public RoundedPanel(int radius) {
        super();
        this.radius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color bg = getBackground();
        if (bg == null) {
            bg = ThemeManager.isDark() ? new Color(40, 42, 46) : Color.WHITE;
        }
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);

        // subtle border
        Color border = ThemeManager.isDark() ? new Color(70,70,70) : new Color(220,220,220);
        g2.setColor(border);
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }
}
