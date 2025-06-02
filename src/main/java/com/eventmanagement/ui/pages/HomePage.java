package com.eventmanagement.ui.pages;

import com.eventmanagement.ui.utils.ImageLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The home page of the Event Management System.
 * Provides navigation to admin login and student registration.
 */
public class HomePage extends BasePage {
    private JButton adminButton;
    private JButton studentButton;
    private ActionListener buttonListener;

    /**
     * Creates a new HomePage
     * @param parent The parent JFrame that contains this page
     */
    public HomePage(JFrame parent) {
        super(parent);
    }

    @Override
    protected void initializeComponents() {
        // Set up the main panel with a nice background
        setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout(0, 20));
        
        // Create a panel for the logo
        JPanel logoPanel = createLogoPanel();
        
        // Create a panel for the title and subtitle
        JPanel titlePanel = createTitlePanel();
        
        // Create a panel for the buttons
        JPanel buttonPanel = createButtonPanel();
        
        // Add components to the main panel with proper spacing
        add(logoPanel, BorderLayout.NORTH);
        add(titlePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createLogoPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        
        // Load and scale the logo (assuming it's in resources/images/logo.png)
        ImageIcon logoIcon = ImageLoader.loadAndScaleImage("images/logo.png", 150, 150);
        JLabel logoLabel;
        
        if (logoIcon != null) {
            logoLabel = new JLabel(logoIcon);
        } else {
            // Fallback to text if image not found
            logoLabel = new JLabel("EVENT MANAGER");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
            logoLabel.setForeground(new Color(63, 81, 181));
        }
        
        panel.add(logoLabel);
        return panel;
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setOpaque(false);
        
        // Title
        JLabel titleLabel = new JLabel("Event Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 33, 33));
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Please select an option to continue", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(117, 117, 117));
        
        // Add components with some padding
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        
        JPanel content = new JPanel(new BorderLayout(0, 10));
        content.setOpaque(false);
        content.add(titleLabel, BorderLayout.NORTH);
        content.add(subtitleLabel, BorderLayout.SOUTH);
        
        wrapper.add(content, BorderLayout.NORTH);
        panel.add(wrapper, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        // Create buttons with icons
        ImageIcon adminIcon = ImageLoader.loadAndScaleImage("images/admin_logo.png", 24, 24);
        ImageIcon studentIcon = ImageLoader.loadAndScaleImage("images/student_logo.png", 24, 24);
        
        adminButton = createButton("ADMIN LOGIN", new Color(63, 81, 181), adminIcon);
        studentButton = createButton("STUDENT REGISTER", new Color(33, 150, 243), studentIcon);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 30, 50);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        buttonPanel.add(adminButton, gbc);
        buttonPanel.add(studentButton, gbc);
        
        return buttonPanel;
    }
    
    private JButton createButton(String text, Color color, Icon icon) {
        JButton button = new JButton(text, icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw background
                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(color);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Draw icon and text
                super.paintComponent(g);
                
                g2.dispose();
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                // No border
            }
        };
        
        // Button styling
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setIconTextGap(15);
        button.setPreferredSize(new Dimension(300, 50));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Add action listener
        button.addActionListener(e -> {
            if (buttonListener != null) {
                buttonListener.actionPerformed(e);
            }
        });
        
        return button;
    }
    
    /**
     * Sets the action listener for the buttons
     * @param listener The action listener to be called when a button is clicked
     */
    public void setButtonListener(ActionListener listener) {
        this.buttonListener = listener;
    }
    
    /**
     * Gets the admin button
     */
    public JButton getAdminButton() {
        return adminButton;
    }
    
    /**
     * Gets the student button
     */
    public JButton getStudentButton() {
        return studentButton;
    }
}