import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class DataDisplay extends JDialog {
    private JTable table;
    private JLabel totalLabel;
    private DefaultTableModel tableModel;
    private JFrame mainFrame;
    public void showDataDisplay() {
    //this.setVisible(true);
    }
    public DataDisplay(JFrame parent, DefaultTableModel tableModel) {
        super(parent, "Ticket Data", true);

        this.tableModel = tableModel;
        this.mainFrame = parent; 

        table = new JTable(tableModel);
        table.setShowGrid(false);
    
        boolean deleteColumnExists = false;
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            if (tableModel.getColumnName(i).equals("Delete")) {
                deleteColumnExists = true;
                break;
            }
        }
    
        if (!deleteColumnExists) {
            tableModel.addColumn("Date and Time"); 
            tableModel.addColumn("Delete");
            tableModel.addColumn("Edit");
        }
        
        table.getColumn("Delete").setCellRenderer(new ButtonRenderer("Delete"));
        table.getColumn("Edit").setCellRenderer(new ButtonRenderer("Edit"));
        table.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox()));
        table.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox()));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(128, 0, 0));
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(Color.WHITE);

        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.setForeground(Color.BLACK);
        table.setSelectionBackground(new Color(255, 102, 102));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JButton backButton = new JButton("Back");

        backButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        backButton.setBackground(new Color(128, 0, 0));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                mainFrame.setVisible(true);
            }
        });
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(255, 204, 204));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonAndTotalPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(255, 204, 204));
        buttonPanel.add(backButton);
        buttonAndTotalPanel.add(buttonPanel, BorderLayout.CENTER);

        totalLabel = new JLabel("Total Income: ₱0.0 | Total Customers: 0");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(Color.BLACK);
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonAndTotalPanel.add(totalLabel, BorderLayout.SOUTH);

        contentPanel.add(buttonAndTotalPanel, BorderLayout.SOUTH);

        add(contentPanel);
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("Pics/logo.png")).getImage());
        setPreferredSize(new Dimension(1000, 400));
        setLocationRelativeTo(null);
        pack();
        setResizable(true);
        loadDataFromDatabase();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        table.setRowHeight(25); 

    }   
                public DataDisplay(Container parent, DefaultTableModel tableModel2) {
                }

                public DataDisplay() {
                }

                class ButtonRenderer extends JButton implements TableCellRenderer {
                public ButtonRenderer(String label) {
                    setText(label);
                    setOpaque(true);
                    setBackground(new Color(180, 0, 0)); 
                    setForeground(Color.WHITE); // Set text color
                    setFont(new Font("Segoe UI", Font.ITALIC, 18)); 
                    setBorder(BorderFactory.createRaisedBevelBorder());
                }
                
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    return this;
                }
            }

            class ButtonEditor extends DefaultCellEditor {
                private JButton button;
                private String label;
                private boolean isPushed;
            
                public ButtonEditor(JCheckBox jCheckBox) {
                    super(new JCheckBox());
                    button = new JButton();
                    button.setOpaque(true);
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            fireEditingStopped();
                            int selectedRow = table.getSelectedRow();
                            if (selectedRow != -1) {
                                int tkt_id = (int) tableModel.getValueAt(selectedRow, 0);
                                if (label.equals("Delete")) {
                                    int result = JOptionPane.showConfirmDialog(
                                        table,
                                        "Are you sure you want to delete this record?",
                                        "Confirmation",
                                        JOptionPane.YES_NO_OPTION
                                    );
                                    if (result == JOptionPane.YES_OPTION) {
                                        deleteRowFromDatabase(tkt_id);
                                        tableModel.removeRow(selectedRow);
                                        refreshTotal();
                                    }
                                } else if (label.equals("Edit")) {
                                    String C_nme = (String) tableModel.getValueAt(selectedRow, 1);
                                    String movie = (String) tableModel.getValueAt(selectedRow, 2);
                                    double prc = (double) tableModel.getValueAt(selectedRow, 3);
                                    String seat = (String) tableModel.getValueAt(selectedRow, 4);
                                    String prty = (String) tableModel.getValueAt(selectedRow, 5);
                                    tableModel.getValueAt(selectedRow, 6);
            
                                    setVisible(false);
                                    new modifica(DataDisplay.this, tkt_id, C_nme, movie, prc, seat, prty);
                                }
                            }
                        }
                    });
                }
            
                protected void refreshTotal() {
                }
                public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                    if (value == null) {
                        return null;
                    }
                    label = value.toString();
                    button.setText(label);
                    isPushed = true;
                    return button;
                }
                public Object getCellEditorValue() {
                    if (isPushed) {
                        System.out.println("edit button was pushed");
                    }
                    isPushed = false;
                    return label;
                }
                public boolean stopCellEditing() {
                    isPushed = false;
                    return super.stopCellEditing();
                }
            }

        private void deleteRowFromDatabase(int tkt_id) {
            Connection conn = null;
            PreparedStatement preparedStatement = null;
        
            try {
                final String DB_URL = "jdbc:mysql://localhost:3306/tiketa"; 
                final String USERNAME = "root";
                final String PASSWORD = "";
        
                conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        
                String sql = "DELETE FROM registrazione WHERE tkt_id = ?"; 
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, tkt_id);
                preparedStatement.executeUpdate();
        
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            } finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
    void loadDataFromDatabase() {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        double totalTicketPrice = 0;
        int totalRowCount = 0;
        tableModel.setRowCount(0);
    
        try {
            final String DB_URL = "jdbc:mysql://localhost:3306/tiketa";
            final String USERNAME = "root";
            final String PASSWORD = "";
    
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    
            String sql = "SELECT * FROM registrazione"; 
            preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            while (resultSet.next()) {
                int tkt_id = resultSet.getInt("tkt_id");
                String customerName = resultSet.getString("C_nme");
                String movieTitle = resultSet.getString("movie");
                double price = resultSet.getDouble("prc");
                String seatNumber = resultSet.getString("seat");
                String priority = resultSet.getString("prty");
                Timestamp regDateTime = resultSet.getTimestamp("RegDateTime"); // Retrieve the date and time
                
                tableModel.addRow(new Object[]{tkt_id, customerName, movieTitle, price, seatNumber, priority, regDateTime, "Delete", "Edit"});
                
                totalTicketPrice += price;
                totalRowCount++;
            }
    
            calculateAndDisplayTotal(totalTicketPrice, totalRowCount);
    
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    } 
    public void calculateAndDisplayTotal(double totalTicketPrice, int totalRowCount) {
        totalLabel.setText("Total Income: $" + totalTicketPrice + " | Total Customers " + totalRowCount);
    }
    public void refreshTable(DefaultTableModel newTableModel) {
        tableModel = newTableModel;
        table.setModel(tableModel);
        tableModel.fireTableDataChanged();
    }
    public JTable getTable() {
        return table;
    }
    public void setTable(JTable table) {
        this.table = table;
    }
    public JLabel getTotalLabel() {
        return totalLabel;
    }
    public void setTotalLabel(JLabel totalLabel) {
        this.totalLabel = totalLabel;
    }
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }
    public void updateRowInDatabase(int tkt_id, String editedC_nme, String editedmovie, double editedprc, String editedseat, String editedprty, Timestamp regDateTime) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
    
        try {
            final String DB_URL = "jdbc:mysql://localhost:3306/tiketa"; 
            final String USERNAME = "root";
            final String PASSWORD = "";
    
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    
            String sql = "UPDATE registrazione SET C_nme=?, movie=?, prc=?, seat=?, prty=?, RegDateTime=? WHERE tkt_id=?"; 
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, editedC_nme);
            preparedStatement.setString(2, editedmovie);
            preparedStatement.setDouble(3, editedprc);
            preparedStatement.setString(4, editedseat);
            preparedStatement.setString(5, editedprty);
            preparedStatement.setTimestamp(6, regDateTime);
            preparedStatement.setInt(7, tkt_id);
    
            preparedStatement.executeUpdate();
    
            loadDataFromDatabase(); 
    
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public JFrame getMainFrame() {
        return mainFrame;
    }
}