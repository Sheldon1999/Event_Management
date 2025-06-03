package com.eventmanagement.ui.pages;

import com.eventmanagement.models.Participant;
import com.eventmanagement.ui.theme.AppTheme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.List;

/**
 * Page to display event registration details in a table format.
 */
public class EventDetailsPage extends BasePage {
    private JTable dataTable;
    private JButton backButton;
    private ActionListener buttonListener;

    public EventDetailsPage(JFrame parent) {
        super(parent);
    }

    @Override
    protected void initializeComponents() {
        setLayout(new BorderLayout(0, 20));
        setBackground(AppTheme.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create header panel with title
        JPanel headerPanel = createHeaderPanel();
        
        // Create table panel
        JPanel tablePanel = createTablePanel();
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Add components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(tablePanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("Event Registration Details");
        titleLabel.setFont(AppTheme.FONT_TITLE);
        titleLabel.setForeground(AppTheme.PRIMARY_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(titleLabel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        // Create table with empty model (data will be loaded later)
        dataTable = new JTable();
        dataTable.setFillsViewportHeight(true);
        dataTable.setAutoCreateRowSorter(true);
        dataTable.setFont(AppTheme.FONT_NORMAL);
        dataTable.getTableHeader().setFont(AppTheme.FONT_MEDIUM);
        dataTable.setRowHeight(30);
        dataTable.setShowGrid(true);
        dataTable.setGridColor(AppTheme.BORDER_LIGHT);
        dataTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Add subtle hover effect
        dataTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Highlight row on hover
                if (row == table.getSelectedRow()) {
                    c.setBackground(AppTheme.PRIMARY_COLOR.brighter());
                    c.setForeground(Color.WHITE);
                } else {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(250, 250, 250));
                    }
                    c.setForeground(AppTheme.TEXT_PRIMARY);
                }
                
                // Center align all cells
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                
                return c;
            }
        });
        
        // Style the table header
        JTableHeader header = dataTable.getTableHeader();
        header.setBackground(AppTheme.PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(AppTheme.FONT_MEDIUM);
        
        panel.add(dataTable.getTableHeader(), BorderLayout.NORTH);
        panel.add(dataTable, BorderLayout.CENTER);
        
        // Add subtle shadow
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createLineBorder(AppTheme.BORDER_LIGHT, 1, true)
        ));
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        backButton = new JButton("Back to Admin");
        styleButton(backButton, AppTheme.PRIMARY_COLOR);
        backButton.addActionListener(e -> {
            if (buttonListener != null) {
                buttonListener.actionPerformed(
                    new ActionEvent(backButton, ActionEvent.ACTION_PERFORMED, "back")
                );
            }
        });
        
        panel.add(backButton);
        return panel;
    }
    
    private void styleButton(JButton button, Color color) {
        button.setFont(AppTheme.FONT_MEDIUM);
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 40));
        
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
    
    /**
     * Loads data from a ResultSet into the table
     * @param rs The ResultSet containing the data to display
     */
    public void loadData(ResultSet rs) {
        try {
            // Get column names
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // Create column names array
            String[] columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = metaData.getColumnName(i + 1);
            }
            
            // Create data array
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            
            // Add data rows
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                model.addRow(row);
            }
            
            // Set the model to the table
            dataTable.setModel(model);
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                parentFrame,
                "Error loading data: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /**
     * Sets the event name in the title
     * @param eventName The name of the event
     */
    public void setEventName(String eventName) {
        // If you want to show the event name in the title
        // You can update the header panel here if needed
    }
    
    public void setButtonListener(ActionListener listener) {
        this.buttonListener = listener;
    }
    
    public JButton getBackButton() {
        return backButton;
    }

    public void loadParticipants(List<Participant> participants) {
    // Create column names
    String[] columnNames = {
        "ID", "Name", "Email", "Phone", "Department", "Registration Date"
    };
    
    // Create data array
    Object[][] data = new Object[participants.size()][columnNames.length];
    
    for (int i = 0; i < participants.size(); i++) {
        Participant p = participants.get(i);
        data[i][0] = p.getId();
        data[i][1] = p.getName();
        data[i][2] = p.getEmail();
        data[i][3] = p.getPhone();
        data[i][4] = p.getDepartment();
        data[i][5] = p.getRegistrationDate();
    }
    
    // Set the model to the table
    dataTable.setModel(new DefaultTableModel(data, columnNames) {
        @Override
        public boolean isCellEditable(int row, int column) {
            // Make all cells non-editable
            return false;
        }
    });
    
    // Auto-resize columns
    for (int i = 0; i < dataTable.getColumnCount(); i++) {
        dataTable.getColumnModel().getColumn(i).setPreferredWidth(150);
    }
}
}