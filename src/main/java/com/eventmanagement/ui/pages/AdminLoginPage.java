package com.eventmanagement.ui.pages;

import com.eventmanagement.models.Event;
import com.eventmanagement.repositories.EventRepository;
import com.eventmanagement.repositories.impl.JdbcEventRepository;
import com.eventmanagement.ui.theme.AppTheme;
import com.eventmanagement.ui.utils.ImageLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Admin login page for the Event Management System.
 * Provides authentication for administrators to access the system.
 */
public class AdminLoginPage extends BasePage {
    private JPasswordField passwordField;
    private JComboBox<String> eventComboBox;
    private JButton loginButton;
    private JButton backButton;
    private ActionListener buttonListener;
    private final EventRepository eventRepository;

    public AdminLoginPage(JFrame parent) {
        super(parent);
        this.eventRepository = new JdbcEventRepository();
        initializeComponents();
        loadEvents(); // Load events from database
    }

    private void loadEvents() {
        try {
            // Get all events from the repository
            List<Event> events = eventRepository.findAll();
            
            // Sort events by name (optional)
            events.sort((e1, e2) -> e1.getName().compareToIgnoreCase(e2.getName()));
            
            // Clear existing items and add "Select Event" as the first item
            eventComboBox.removeAllItems();
            eventComboBox.addItem("Select Event");
            
            // Add events to the combo box
            for (Event event : events) {
                eventComboBox.addItem(event.getName());
            }
            
            // Select the first item
            if (eventComboBox.getItemCount() > 1) {
                eventComboBox.setSelectedIndex(1); // Skip "Select Event"
            }
        } catch (Exception e) {
            showError("Error loading events: " + e.getMessage());
            // Fallback to empty combo box with just "Select Event"
            eventComboBox.removeAllItems();
            eventComboBox.addItem("Select Event");
        }
    }

    @Override
    protected void initializeComponents() {
        // Set up the main panel with app background
        setBackground(AppTheme.BACKGROUND_COLOR);
        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 40, 20));

        // Create a card panel for the login form
        JPanel cardPanel = createCardPanel();
        
        // Add components to the main panel
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(AppTheme.FONT_TITLE);
        titleLabel.setForeground(AppTheme.PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(titleLabel);
        return panel;
    }

    private JPanel createCardPanel() {
        // Create a card panel with shadow
        JPanel cardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shadow
                g2d.setColor(AppTheme.SHADOW_COLOR);
                g2d.fillRoundRect(
                    AppTheme.SHADOW_OFFSET, 
                    AppTheme.SHADOW_OFFSET, 
                    getWidth() - AppTheme.SHADOW_OFFSET * 2, 
                    getHeight() - AppTheme.SHADOW_OFFSET * 2, 
                    AppTheme.BORDER_RADIUS, 
                    AppTheme.BORDER_RADIUS
                );
                
                // Draw panel background
                g2d.setColor(AppTheme.PANEL_BACKGROUND);
                g2d.fillRoundRect(
                    0, 0, 
                    getWidth() - AppTheme.SHADOW_OFFSET * 2, 
                    getHeight() - AppTheme.SHADOW_OFFSET * 2, 
                    AppTheme.BORDER_RADIUS, 
                    AppTheme.BORDER_RADIUS
                );
                
                g2d.dispose();
            }
        };
        
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setOpaque(false);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(
            AppTheme.PADDING_LARGE, 
            AppTheme.PADDING_LARGE, 
            AppTheme.PADDING_LARGE, 
            AppTheme.PADDING_LARGE
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1.0;
        
        // Event selection
        JLabel eventLabel = new JLabel("Select Event");
        eventLabel.setFont(AppTheme.FONT_MEDIUM);
        eventLabel.setForeground(AppTheme.TEXT_PRIMARY);
        
        eventComboBox = new JComboBox<>();
        styleComboBox(eventComboBox);
        
        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(AppTheme.FONT_MEDIUM);
        passwordLabel.setForeground(AppTheme.TEXT_PRIMARY);
        
        passwordField = new JPasswordField(20);
        stylePasswordField(passwordField);
        
        // Login button
        loginButton = new JButton("LOGIN");
        styleButton(loginButton, AppTheme.PRIMARY_COLOR);
        loginButton.addActionListener(e -> {
            if (buttonListener != null) {
                buttonListener.actionPerformed(e);
            }
        });
        
        // Add components to card panel
        cardPanel.add(eventLabel, gbc);
        cardPanel.add(eventComboBox, gbc);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)), gbc); // Spacer
        cardPanel.add(passwordLabel, gbc);
        cardPanel.add(passwordField, gbc);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)), gbc); // Spacer
        cardPanel.add(loginButton, gbc);
        
        return cardPanel;
    }
    
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        backButton = new JButton("Back to Home");
        styleButton(backButton, AppTheme.SECONDARY_COLOR);
        backButton.setForeground(AppTheme.TEXT_PRIMARY);
        backButton.addActionListener(e -> {
            if (buttonListener != null) {
                buttonListener.actionPerformed(e);
            }
        });
        
        panel.add(backButton);
        return panel;
    }
    
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(AppTheme.FONT_NORMAL);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_LIGHT),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        comboBox.setPreferredSize(new Dimension(300, 40));
    }
    
    private void stylePasswordField(JPasswordField field) {
        field.setFont(AppTheme.FONT_NORMAL);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_LIGHT),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        field.setPreferredSize(new Dimension(300, 40));
    }
    
    private void styleButton(JButton button, Color color) {
        button.setFont(AppTheme.FONT_MEDIUM);
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(300, 45));
        
        // Add hover and press effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
    }
    
    // Getters for UI components
    public JButton getLoginButton() {
        return loginButton;
    }
    
    public JButton getBackButton() {
        return backButton;
    }
    
    public String getSelectedEvent() {
        return (String) eventComboBox.getSelectedItem();
    }
    
    public char[] getPassword() {
        return passwordField.getPassword();
    }
    
    public void setButtonListener(ActionListener listener) {
        this.buttonListener = listener;
    }
    
    public void clearPassword() {
        passwordField.setText("");
    }
}