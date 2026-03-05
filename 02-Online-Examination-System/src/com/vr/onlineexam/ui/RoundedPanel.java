package com.vr.onlineexam.ui;
import javax.swing.*;
import java.awt.*;

/**
 * Rounded panel with subtle shadow look used for modern cards.
 */
public class RoundedPanel extends JPanel {
    private final int radius;
    private final Color backgroundColor;

    public RoundedPanel(int radius, Color bg) {
        super();
        this.radius = radius;
        this.backgroundColor = bg;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int w = getWidth(), h = getHeight();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // subtle layered glass effect
        Color bg = backgroundColor != null ? backgroundColor : getBackground();
        g2.setColor(new Color(0,0,0,10));
        g2.fillRoundRect(4, 6, w-8, h-8, radius, radius); // shadow
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, w-6, h-6, radius, radius);
        g2.dispose();
        super.paintComponent(g);
    }
}

