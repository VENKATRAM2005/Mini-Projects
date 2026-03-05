package com.vr.onlineexam.ui;
import java.awt.*;
import javax.swing.border.Border;

/**
 * Lightweight rounded border helper.
 */
public class RoundedBorder implements Border {
    private final int radius;
    public RoundedBorder(int r) { radius = r; }
    public Insets getBorderInsets(Component c) { return new Insets(radius, radius, radius, radius); }
    public boolean isBorderOpaque() { return false; }
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 0, 0, 30));
        g2.drawRoundRect(x + 1, y + 1, width - 3, height - 3, radius, radius);
        g2.dispose();
    }
}

