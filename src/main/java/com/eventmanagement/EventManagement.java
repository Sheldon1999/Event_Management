package com.eventmanagement;

import com.eventmanagement.ui.pages.HomePage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventManagement extends JFrame {
    private JFrame mainFrame;
    private HomePage homePage;

    public EventManagement() {
        initialize();
    }

    private void initialize() {
        // Set up the main application window
        mainFrame = new JFrame("Event Management System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setMinimumSize(new Dimension(600, 500));
        
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
                    // Handle admin login
                    showAdminLogin();
                } else if (e.getSource() == homePage.getStudentButton()) {
                    // Handle student registration
                    showStudentRegistration();
                }
            }
        });
        
        mainFrame.add(homePage);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void showAdminLogin() {
        // TODO: Implement admin login page
        JOptionPane.showMessageDialog(mainFrame, 
            "Admin Login Clicked", 
            "Info", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void showStudentRegistration() {
        // TODO: Implement student registration page
        JOptionPane.showMessageDialog(mainFrame, 
            "Student Registration Clicked", 
            "Info", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Use the event dispatch thread for Swing components
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EventManagement();
            }
        });
    }
}