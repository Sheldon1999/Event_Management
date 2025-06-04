package com.eventmanagement.ui.components.buttons;

import javax.swing.*;
import java.awt.*;

/**
 * A custom-styled button with hover and click effects.
 */
public class StyledButton extends JButton {
    private Color normalColor;
    private Color hoverColor;
    private Color pressedColor;
    private Color textColor = Color.WHITE;
    private int cornerRadius = 8;
    private boolean isHovered = false;
    private boolean isPressed = false;

    /**
     * Creates a new StyledButton with the specified text and colors.
     */
    public StyledButton(String text, Color normalColor, Color hoverColor) {
        super(text);
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;
        this.pressedColor = hoverColor.darker();
        
        setOpaque(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setForeground(textColor);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Add mouse listener for hover and press effects
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                isHovered = true;
                repaint();
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                isHovered = false;
                isPressed = false;
                repaint();
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                isPressed = true;
                repaint();
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                isPressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw the button background
        if (isPressed) {
            g2.setColor(pressedColor);
        } else if (isHovered) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(normalColor);
        }
        
        // Draw rounded rectangle background
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        
        // Draw the text
        FontMetrics fm = g2.getFontMetrics();
        Rectangle stringBounds = fm.getStringBounds(getText(), g2).getBounds();
        int textX = (getWidth() - stringBounds.width) / 2;
        int textY = (getHeight() - stringBounds.height) / 2 + fm.getAscent();
        
        g2.setColor(getForeground());
        g2.drawString(getText(), textX, textY);
        
        // Draw icon if present
        if (getIcon() != null) {
            Icon icon = getIcon();
            int iconX = textX - icon.getIconWidth() - 5; // 5px gap between icon and text
            int iconY = (getHeight() - icon.getIconHeight()) / 2;
            icon.paintIcon(this, g2, iconX, iconY);
        }
        
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No border - we're handling the border in paintComponent
    }

    // Getters and setters for customization
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        setForeground(textColor);
    }
}