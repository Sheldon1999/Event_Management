package com.eventmanagement;

import com.eventmanagement.models.Participant;
import com.eventmanagement.models.Event;
import com.eventmanagement.repositories.EventRepository;
import com.eventmanagement.repositories.ParticipantRepository;
import com.eventmanagement.repositories.impl.JdbcEventRepository;
import com.eventmanagement.repositories.impl.JdbcParticipantRepository;
import com.eventmanagement.ui.pages.AdminLoginPage;
import com.eventmanagement.ui.pages.EventDetailsPage;
import com.eventmanagement.ui.pages.HomePage;
import com.eventmanagement.ui.pages.StudentRegistrationPage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class EventManagement extends JFrame {
    private JFrame mainFrame;
    private HomePage homePage;
    private AdminLoginPage adminLoginPage;

    // Add these fields to your EventManagement class
    private final ParticipantRepository participantRepository;
    private final EventRepository eventRepository;
    
    public EventManagement() {
        // Initialize repositories
        this.participantRepository = new JdbcParticipantRepository();
        this.eventRepository = new JdbcEventRepository();
        initialize();
    }

    private void initialize() {
        // Set up the main application window
        mainFrame = new JFrame("Event Management System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 700);  // Slightly larger to accommodate the admin login
        mainFrame.setMinimumSize(new Dimension(800, 600));
        
        // Center the window on screen
        mainFrame.setLocationRelativeTo(null);
        
        // Show the home page
        showHomePage();
        
        // Make the window visible
        mainFrame.setVisible(true);
    }

    private void showHomePage() {
        // Clear the current content
        mainFrame.getContentPane().removeAll();
        
        // Create and add the home page
        homePage = new HomePage(mainFrame);
        homePage.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == homePage.getAdminButton()) {
                    showAdminLogin();
                } else if (e.getSource() == homePage.getStudentButton()) {
                    showStudentRegistration();
                }
            }
        });
        
        mainFrame.add(homePage);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void showAdminLogin() {
        // Clear the current content
        mainFrame.getContentPane().removeAll();
        
        // Create and add the admin login page
        adminLoginPage = new AdminLoginPage(mainFrame);
        adminLoginPage.setButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == adminLoginPage.getLoginButton()) {
                    handleAdminLogin();
                } else if (e.getSource() == adminLoginPage.getBackButton()) {
                    // Go back to home page
                    showHomePage();
                }
            }
        });
        
        mainFrame.add(adminLoginPage);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    

    private void handleAdminLogin() {
        String selectedEvent = adminLoginPage.getSelectedEvent();
        char[] password = adminLoginPage.getPassword();
        
        // Verify password (replace with your actual password verification logic)
        if (Arrays.equals(password, "Super@1999".toCharArray())) {
            // Password is correct, show event details
            showEventDetails(selectedEvent);
        } else {
            // Show error message
            JOptionPane.showMessageDialog(
                mainFrame,
                "Invalid password. Please try again.",
                "Authentication Failed",
                JOptionPane.ERROR_MESSAGE
            );
            // Clear the password field
            adminLoginPage.clearPassword();
        }
    }

    private void showEventDetails(String eventName) {
        mainFrame.getContentPane().removeAll();
        
        // Show loading state
        JPanel loadingPanel = new JPanel(new BorderLayout());
        loadingPanel.add(new JLabel("Loading registrations...", JLabel.CENTER), BorderLayout.CENTER);
        mainFrame.add(loadingPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    
        // Load data in a background thread
        new SwingWorker<List<Participant>, Void>() {
            @Override
            protected List<Participant> doInBackground() throws Exception {
                // Find the event by name
                List<Event> events = eventRepository.findByNameContaining(eventName);
                if (events.isEmpty()) {
                    throw new RuntimeException("Event not found: " + eventName);
                }
                
                // Get participants for the first matching event
                return participantRepository.findByEventId(events.get(0).getId());
            }
    
            @Override
            protected void done() {
                try {
                    List<Participant> participants = get();
                    
                    // Remove loading panel
                    mainFrame.remove(loadingPanel);
                    
                    // Create and show the event details page
                    EventDetailsPage eventDetailsPage = new EventDetailsPage(mainFrame);
                    eventDetailsPage.setEventName(eventName);
                    
                    // Convert participants to a format the table can display
                    eventDetailsPage.loadParticipants(participants);
                    
                    eventDetailsPage.setButtonListener(e -> {
                        if (e.getSource() == eventDetailsPage.getBackButton()) {
                            showAdminLogin();
                        }
                    });
                    
                    mainFrame.add(eventDetailsPage);
                    mainFrame.revalidate();
                    mainFrame.repaint();
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                        mainFrame,
                        "Error loading event data: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    showAdminLogin();
                }
            }
        }.execute();
    }

    private void showStudentRegistration() {
        mainFrame.getContentPane().removeAll();
        
        StudentRegistrationPage registrationPage = new StudentRegistrationPage(mainFrame);
        registrationPage.setButtonListener(e -> {
            if (e.getSource() == registrationPage.getBackButton()) {
                showHomePage(); // Go back to home
            }
        });
        
        mainFrame.add(registrationPage);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public static void main(String[] args) {
        // Set the system look and feel for a more modern appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Use the event dispatch thread for Swing components
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EventManagement();
            }
        });
    }
}