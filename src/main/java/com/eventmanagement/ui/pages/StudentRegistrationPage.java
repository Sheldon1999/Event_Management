package com.eventmanagement.ui.pages;

import com.eventmanagement.models.Event;
import com.eventmanagement.models.Participant;
import com.eventmanagement.repositories.EventRepository;
import com.eventmanagement.repositories.ParticipantRepository;
import com.eventmanagement.repositories.impl.JdbcEventRepository;
import com.eventmanagement.repositories.impl.JdbcParticipantRepository;
import com.eventmanagement.ui.theme.AppTheme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentRegistrationPage extends BasePage {
    private JTextField nameField;
    private JTextField deptField;
    private JTextField semField;
    private JTextField contactField;
    private JTextField emailField;
    private JComboBox<String> eventComboBox;
    private JButton registerButton;
    private JButton backButton;
    private ActionListener buttonListener;
    private final EventRepository eventRepository;

    public StudentRegistrationPage(JFrame parent) {
        super(parent);
        this.eventRepository = new JdbcEventRepository();
        initializeComponents();
        loadEvents();
    }

    private void loadEvents() {
        try {
            List<Event> events = eventRepository.findAll();
            events.sort((e1, e2) -> e1.getName().compareToIgnoreCase(e2.getName()));
            
            eventComboBox.removeAllItems();
            for (Event event : events) {
                eventComboBox.addItem(event.getName());
            }
            
            if (eventComboBox.getItemCount() > 0) {
                eventComboBox.setSelectedIndex(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading events: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    protected void initializeComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(AppTheme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // Create main panel with card styling
        JPanel cardPanel = new JPanel(new BorderLayout(0, 20));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER_NORMAL, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Add title
        JLabel titleLabel = new JLabel("STUDENT REGISTRATION", JLabel.CENTER);
        titleLabel.setFont(AppTheme.FONT_TITLE);
        titleLabel.setForeground(AppTheme.PRIMARY_COLOR);
        cardPanel.add(titleLabel, BorderLayout.NORTH);

        // Add form panel
        cardPanel.add(createFormPanel(), BorderLayout.CENTER);
        add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5, 5, 5, 10);

        // Name field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(AppTheme.FONT_MEDIUM);
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel deptLabel = new JLabel("Department:");
        deptLabel.setFont(AppTheme.FONT_MEDIUM);
        formPanel.add(deptLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        deptField = new JTextField(20);
        formPanel.add(deptField, gbc);

        // Semester field
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel semLabel = new JLabel("Semester:");
        semLabel.setFont(AppTheme.FONT_MEDIUM);
        formPanel.add(semLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        semField = new JTextField(20);
        formPanel.add(semField, gbc);

        // Contact No field
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel contactLabel = new JLabel("Contact No:");
        contactLabel.setFont(AppTheme.FONT_MEDIUM);
        formPanel.add(contactLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contactField = new JTextField(20);
        formPanel.add(contactField, gbc);

        // Email field
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(AppTheme.FONT_MEDIUM);
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        emailField = new JTextField(20);
        formPanel.add(emailField, gbc);

        // Event selection
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel eventLabel = new JLabel("Select Event:");
        eventLabel.setFont(AppTheme.FONT_MEDIUM);
        formPanel.add(eventLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        eventComboBox = new JComboBox<>();
        eventComboBox.setFont(AppTheme.FONT_NORMAL);
        formPanel.add(eventComboBox, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setOpaque(false);
        
        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            if (buttonListener != null) {
                buttonListener.actionPerformed(e);
            }
        });
        
        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> handleRegistration());
        buttonPanel.add(backButton);
        buttonPanel.add(registerButton);
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        return formPanel;
    }

    public void setButtonListener(ActionListener listener) {
        this.buttonListener = listener;
    }

    public JButton getBackButton() {
        return backButton;
    }

    private void clearForm() {
        nameField.setText("");
        deptField.setText("");
        semField.setText("");
        contactField.setText("");
        emailField.setText("");
        if (eventComboBox.getItemCount() > 0) {
            eventComboBox.setSelectedIndex(0);
        }
        nameField.requestFocus();
    }



    // private void handleRegistration() {
    //     // Get form data
    //     String name = nameField.getText().trim();
    //     String department = deptField.getText().trim();
    //     String semester = semField.getText().trim();
    //     String contact = contactField.getText().trim();
    //     String email = emailField.getText().trim();
    //     String eventName = (String) eventComboBox.getSelectedItem();
    
    //     // Validate inputs
    //     if (name.isEmpty() || department.isEmpty() || semester.isEmpty() || 
    //         contact.isEmpty() || email.isEmpty() || eventName == null) {
    //         JOptionPane.showMessageDialog(this,
    //             "All fields are required!",
    //             "Validation Error",
    //             JOptionPane.ERROR_MESSAGE);
    //         return;
    //     }
    
    //     // Validate email format
    //     if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
    //         JOptionPane.showMessageDialog(this,
    //             "Please enter a valid email address",
    //             "Validation Error",
    //             JOptionPane.ERROR_MESSAGE);
    //         return;
    //     }
    
    //     // Show confirmation dialog
    //     int confirm = JOptionPane.showConfirmDialog(this,
    //         "Register for " + eventName + "?",
    //         "Confirm Registration",
    //         JOptionPane.YES_NO_OPTION);
    
    //     if (confirm == JOptionPane.YES_OPTION) {
    //         // TODO: Save to database
    //         JOptionPane.showMessageDialog(this,
    //             "Registration successful!",
    //             "Success",
    //             JOptionPane.INFORMATION_MESSAGE);
                
    //         // Clear form
    //         clearForm();
    //     }
    // }

    private void handleRegistration() {
        // Get form data
        String name = nameField.getText().trim();
        String department = deptField.getText().trim();
        String semester = semField.getText().trim();
        String contact = contactField.getText().trim();
        String email = emailField.getText().trim();
        String eventName = (String) eventComboBox.getSelectedItem();

        // Validate inputs
        if (name.isEmpty() || department.isEmpty() || semester.isEmpty() || 
            contact.isEmpty() || email.isEmpty() || eventName == null) {
            JOptionPane.showMessageDialog(this,
                "All fields are required!",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid email address",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Show confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(this,
            "Register for " + eventName + "?",
            "Confirm Registration",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Get the selected event
                List<Event> events = eventRepository.findByNameContaining(eventName);
                if (events.isEmpty()) {
                    throw new RuntimeException("Selected event not found");
                }
                Event selectedEvent = events.get(0);
                
                // Create participant
                Participant participant = new Participant();
                participant.setName(name);
                participant.setDepartment(department);
                participant.setSemester(Integer.parseInt(semester));
                participant.setPhone(contact);
                participant.setEmail(email);
                participant.setEventId(selectedEvent.getId());
                
                // Save to database
                ParticipantRepository participantRepo = new JdbcParticipantRepository();
                participantRepo.save(participant);
                
                JOptionPane.showMessageDialog(this,
                    "Registration successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                    
                // Clear form
                clearForm();
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Error saving registration: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}