import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class modifica extends JDialog {
    private JTextField customerNameField;
    private JTextField movieField;
    private JTextField priceField;
    private JTextField seatField;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton printout;
    private Font mainFont = new Font("Cancun", Font.BOLD, 20 );
    private Color backgroundColor = new Color(128, 0, 0);

    public modifica(DataDisplay dataDisplay, int tkt_id, String customerName, String movie, double price, String seat, String priority) {
        setTitle("Edit Ticket");
        setLayout(new BorderLayout());
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("Pics/logo.png")).getImage());

        JPanel centerPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        JPanel buttonPanel = new JPanel();
        centerPanel.setBackground(backgroundColor);

        JLabel lblTicketID = new JLabel("Ticket ID");
        JLabel ID = new JLabel(Integer.toString(tkt_id));
         ID.setForeground(Color.green);
         lblTicketID.setFont(mainFont);
         ID.setFont(mainFont);

        JLabel lblName = new JLabel("Customer Name");
        customerNameField = new JTextField(customerName);
        lblName.setFont(mainFont);


        JLabel lblMovie = new JLabel("Movie Title");
        movieField = new JTextField(movie);
        lblMovie.setFont(mainFont);

        JLabel lblPrice = new JLabel("Price");
        priceField = new JTextField(String.valueOf(price));
        lblPrice.setFont(mainFont);

        JLabel lblSeat = new JLabel("Seat Number");
        seatField = new JTextField(seat);
        lblSeat.setFont(mainFont);

        JLabel lblPriority = new JLabel("Priority");
        String[] priorityChoices = {"Regular", "VIP", "Student", "Senior", "PWD", "Child"};
        JComboBox<String> priorityComboBox = new JComboBox<>(priorityChoices);
        priorityComboBox.setSelectedItem(priority); 
        priorityComboBox.setFont(mainFont);

        printout = new JButton("Print");
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        printout.setBackground(new Color(0, 128, 0));
        saveButton.setBackground(new Color(0, 0, 128));
        cancelButton.setBackground(new Color(128, 0, 0));
        Color textColor = Color.WHITE;
        printout.setForeground(textColor);
        saveButton.setForeground(textColor);
        cancelButton.setForeground(textColor);

        lblTicketID.setForeground(Color.WHITE);
        lblName.setForeground(Color.WHITE);
        lblMovie.setForeground(Color.WHITE);
        lblPrice.setForeground(Color.WHITE);
        lblSeat.setForeground(Color.WHITE);
        lblPriority.setForeground(Color.WHITE);

        customerNameField.setFont(mainFont);
        movieField.setFont(mainFont);
        priceField.setFont(mainFont);
        seatField.setFont(mainFont);

        centerPanel.add(lblTicketID);
        centerPanel.add(ID);
        centerPanel.add(lblName);
        centerPanel.add(customerNameField);
        centerPanel.add(lblMovie);
        centerPanel.add(movieField);
        centerPanel.add(lblPrice);
        centerPanel.add(priceField);
        centerPanel.add(lblSeat);
        centerPanel.add(seatField);
        centerPanel.add(lblPriority);
        centerPanel.add(priorityComboBox);

        buttonPanel.add(printout);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.setBackground(new Color(180,0,0));
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String editedCustomerName = customerNameField.getText();
                String editedMovie = movieField.getText();
                double editedPrice = Double.parseDouble(priceField.getText());
                String editedSeat = seatField.getText();
                String editedPriority = (String) priorityComboBox.getSelectedItem(); // Get the selected priority
        
                boolean updateSuccessful = updateDatabase(tkt_id, editedCustomerName, editedMovie, editedPrice, editedSeat, editedPriority);
        
                if (updateSuccessful) {
                    JOptionPane.showMessageDialog(modifica.this, "Ticket updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    if (dataDisplay != null) {
                        dataDisplay.loadDataFromDatabase();
                        dataDisplay.showDataDisplay();
                        dataDisplay.setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(modifica.this, "Error updating ticket.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (dataDisplay != null) {
                    dataDisplay.showDataDisplay();
                    dataDisplay.setVisible(true);
                }
            }
        });

        printout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerName = customerNameField.getText().trim();
                String movieTitle = movieField.getText().trim();
                double price;
                
                try {
                    price = Double.parseDouble(priceField.getText());
                } catch (NumberFormatException ex) {
                    price = 0.0; 
                }
                
                String seatNumber = seatField.getText().trim();
                String priority = (String) priorityComboBox.getSelectedItem(); 

                if (customerName.isEmpty() || movieTitle.isEmpty() || seatNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all required fields.");
                    return;
                }
        
                printa printaWindow = new printa(customerName, movieTitle, String.valueOf(price), seatNumber, priority, tkt_id);
                
                printaWindow.print(); 
                printaWindow.setVisible(true);
                dispose(); 
            }
        });
        

        setMinimumSize(new Dimension(500, 350));
        getContentPane().setBackground(backgroundColor);
        pack();
        setLocationRelativeTo(null);
        setResizable(true); 
        setVisible(true);
    }

     protected boolean updateDatabase(int tkt_id, String editedCustomerName, String editedMovie, double editedPrice,
            String editedSeat, String editedPriority) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            final String DB_URL = "jdbc:mysql://localhost:3306/tiketa";
            final String USERNAME = "root";
            final String PASSWORD = "";

            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            String sql = "UPDATE registrazione SET C_nme=?, movie=?, prc=?, seat=?, prty=? WHERE tkt_id=?";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, editedCustomerName);
            preparedStatement.setString(2, editedMovie);
            preparedStatement.setDouble(3, editedPrice);
            preparedStatement.setString(4, editedSeat);
            preparedStatement.setString(5, editedPriority);
            preparedStatement.setInt(6, tkt_id);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                return true; 
            }
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
        return false;
    }

}
