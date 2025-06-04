package com.eventmanagement.ui.pages;

import javax.swing.*;
import java.awt.*;

/**
 * Base class for all pages in the application.
 * Provides common functionality and structure for all pages.
 */
public abstract class BasePage extends JPanel {
    protected final JFrame parentFrame;
    
    /**
     * Creates a new BasePage
     * @param parent The parent JFrame that contains this page
     */
    protected BasePage(JFrame parent) {
        this.parentFrame = parent;
        setLayout(new BorderLayout());
        initializeComponents();
    }
    
    /**
     * Initialize all UI components
     */
    protected abstract void initializeComponents();
    
    /**
     * Show an error message dialog
     * @param message The error message to display
     */
    protected void showError(String message) {
        JOptionPane.showMessageDialog(
            parentFrame,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Show an information message dialog
     * @param message The message to display
     */
    protected void showInfo(String message) {
        JOptionPane.showMessageDialog(
            parentFrame,
            message,
            "Information",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Show a confirmation dialog
     * @param message The message to display
     * @return true if the user clicked Yes, false otherwise
     */
    protected boolean confirm(String message) {
        return JOptionPane.showConfirmDialog(
            parentFrame,
            message,
            "Confirm",
            JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
    }
}