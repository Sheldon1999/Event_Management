package com.eventmanagement.ui.theme;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Application-wide theme constants and styles.
 */
public class AppTheme {
    // Private constructor to prevent instantiation
    private AppTheme() {}
    
    // Color palette
    public static final Color PRIMARY_COLOR = new Color(63, 81, 181);    // Indigo
    public static final Color SECONDARY_COLOR = new Color(255, 255, 255); // White
    public static final Color ACCENT_COLOR = new Color(255, 167, 38);     // Amber
    public static final Color BACKGROUND_COLOR = new Color(245, 247, 250); // Light Gray Blue
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);       // Dark Gray
    public static final Color TEXT_SECONDARY = new Color(117, 117, 117);  // Gray
    public static final Color ERROR_COLOR = new Color(244, 67, 54);       // Red
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80);     // Green
    
    // Button colors
    public static final Color BUTTON_NORMAL = PRIMARY_COLOR;
    public static final Color BUTTON_HOVER = new Color(57, 73, 171);     // Darker Indigo
    public static final Color BUTTON_PRESSED = new Color(48, 63, 159);    // Even Darker Indigo
    public static final Color BUTTON_TEXT = Color.WHITE;
    
    // Panel colors
    public static final Color PANEL_BACKGROUND = Color.WHITE;
    public static final Color CARD_BACKGROUND = new Color(250, 250, 250); // Light Gray
    
    // Border colors
    public static final Color BORDER_LIGHT = new Color(224, 224, 224);    // Light Gray
    public static final Color BORDER_NORMAL = new Color(189, 189, 189);   // Medium Gray
    
    // Fonts
    public static final Font FONT_NORMAL = new Font("Arial", Font.PLAIN, 14);
    public static final Font FONT_MEDIUM = new Font("Arial", Font.BOLD, 16);
    public static final Font FONT_LARGE = new Font("Arial", Font.BOLD, 24);
    public static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 28);
    
    // Sizes and spacing
    public static final int PADDING_SMALL = 8;
    public static final int PADDING_MEDIUM = 16;
    public static final int PADDING_LARGE = 24;
    public static final int BORDER_RADIUS = 8;
    
    // Shadows
    public static final Color SHADOW_COLOR = new Color(0, 0, 0, 26); // 10% opacity black
    public static final int SHADOW_OFFSET = 2;
    
    /**
     * Applies the default styling to a button.
     */
    public static void styleButton(JButton button) {
        button.setFont(AppTheme.FONT_MEDIUM);
        button.setForeground(AppTheme.BUTTON_TEXT);
        button.setBackground(AppTheme.BUTTON_NORMAL);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Applies card styling to a panel.
     */
    public static void styleCard(JPanel panel) {
        panel.setBackground(CARD_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_LIGHT, 1, true),
            BorderFactory.createEmptyBorder(PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM, PADDING_MEDIUM)
        ));
    }
    
    /**
     * Creates a styled panel with a shadow effect.
     */
    public static JPanel createShadowPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2d.setColor(SHADOW_COLOR);
                g2d.fillRoundRect(
                    SHADOW_OFFSET, 
                    SHADOW_OFFSET, 
                    getWidth() - SHADOW_OFFSET * 2, 
                    getHeight() - SHADOW_OFFSET * 2, 
                    BORDER_RADIUS, 
                    BORDER_RADIUS
                );
                
                // Draw panel background
                g2d.setColor(getBackground());
                g2d.fillRoundRect(
                    0, 
                    0, 
                    getWidth() - SHADOW_OFFSET * 2, 
                    getHeight() - SHADOW_OFFSET * 2, 
                    BORDER_RADIUS, 
                    BORDER_RADIUS
                );
                
                g2d.dispose();
            }
        };
    }
}