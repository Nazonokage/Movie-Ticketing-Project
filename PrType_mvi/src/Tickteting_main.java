import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;import java.sql.Statement;

public class Tickteting_main extends JFrame{
  private Font mainFont = new Font ("Cancun", Font.BOLD, 20);
  private JTable table;
  private DefaultTableModel tableModel;
private Container bgpanel;

  public void initialize ()
  { // setup sng frame
	 JFrame frame = new JFrame();
	 frame.setSize(700, 400);
	 frame.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("Pics/logo.png")).getImage());
	 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 
	 ImageIcon originalIcon = new ImageIcon(getClass().getClassLoader().getResource("Pics/pict5.png"));
	 int targetWidth = 200;
	 int targetHeight = 200;
	 Image originalImage = originalIcon.getImage();
	 Image resizedImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
	 ImageIcon resizedIcon = new ImageIcon(resizedImage);
	 JLabel imglbl = new JLabel(resizedIcon);
	 JPanel imagePanel = new JPanel();
	 imagePanel.add(imglbl);

	 // titolo
	 JLabel ttllbl = new JLabel("Movie Ticketing Database");
	 ttllbl.setFont(new Font("Cancun", Font.BOLD, 25));
	 JPanel ttlPanel = new JPanel();
	 ttlPanel.add(ttllbl);
	 ttllbl.setForeground(Color.white);
	 
	 // Form 
	 JLabel lblname = new JLabel("Customer Name");
	 JTextField tfname = new JTextField();
	 tfname.setFont(mainFont);
	 lblname.setFont(mainFont);

	 JLabel lblmovi = new JLabel("Movie Title");
	 JTextField tfmov = new JTextField();
	 tfmov.setFont(mainFont);
	 lblmovi.setFont(mainFont);

	 JLabel lblprc = new JLabel("Price");
	 JTextField tfprc = new JTextField("300");
	 tfprc.setFont(mainFont);
	 lblprc.setFont(mainFont);

	 JLabel lblSeat = new JLabel("Seat Number");
	 JTextField tfSeat = new JTextField();
	 tfSeat.setFont(mainFont);
	 lblSeat.setFont(mainFont);

	 JLabel lblprty = new JLabel("Priority");
	 String[] priorityChoices = {"Regular", "VIP", "Student", "Senior", "PWD", "Child"};
	 JComboBox<String> priorityComboBox = new JComboBox<>(priorityChoices);
	 priorityComboBox.setFont(mainFont);
	 lblprty.setFont(mainFont);

	 JPanel fPanel = new JPanel();
	 fPanel.setLayout(new GridLayout(6, 2));
	 fPanel.add(lblname);
	 fPanel.add(tfname);
	 fPanel.add(lblmovi);
	 fPanel.add(tfmov);
	 fPanel.add(lblprc);
	 fPanel.add(tfprc);
	 fPanel.add(lblSeat);
	 fPanel.add(tfSeat);
	 fPanel.add(lblprty);
	 fPanel.add(priorityComboBox);


    String[]columnNames =
    {"Ticket ID", "Customer Name", "Movie Title", "Price", "Seat Number","Priority"};
    tableModel = new DefaultTableModel (columnNames, 0);
    table = new JTable (tableModel);
    table.setFont (mainFont);
    JPanel buttonPanel = new JPanel (new FlowLayout (FlowLayout.RIGHT));	
    buttonPanel.setBackground (new Color (128, 0, 0));

	//mga buttons
    JButton btnShow = new JButton ("Show");
    btnShow.setFont (mainFont);
    JButton btnReg = new JButton ("Register");
    btnReg.setFont (mainFont);
	JButton btnRegisterAndPrint = new JButton("Register and Print");
	btnRegisterAndPrint.setFont(mainFont);

	btnRegisterAndPrint.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String customerName = tfname.getText().trim();
			String movieTitle = tfmov.getText().trim();
			double price = Double.parseDouble(tfprc.getText());
			String seatNumber = tfSeat.getText().trim();
			String priority = (String) priorityComboBox.getSelectedItem();

			if (customerName.isEmpty() || movieTitle.isEmpty() || seatNumber.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Please fill in all required fields.");
				return;
			}

			Connection conn = null;
			PreparedStatement preparedStatement = null;
			int tkt_id = -1;

			try {
				final String DB_URL = "jdbc:mysql://localhost:3306/busticketing";
				final String USERNAME = "root";
				final String PASSWORD = "";
				conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

				java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());

				String sql = "INSERT INTO registrazione (C_nme, movie, prc, seat, prty, RegDateTime) VALUES (?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1, customerName);
				preparedStatement.setString(2, movieTitle);
				preparedStatement.setDouble(3, price);
				preparedStatement.setString(4, seatNumber);
				preparedStatement.setString(5, priority);
				preparedStatement.setDate(6, sqlDate);
				preparedStatement.executeUpdate();

				ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
				if (generatedKeys.next()) {
					tkt_id = generatedKeys.getInt(1);
				}

				tfname.setText("");
				tfmov.setText("");
				tfprc.setText("400");
				tfSeat.setText("");
				priorityComboBox.setSelectedIndex(0);
				frame.dispose();
				JOptionPane.showMessageDialog(null, "Ticket registered successfully.");
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

			printa printaWindow = new printa(customerName, movieTitle, String.valueOf(price), seatNumber, priority, tkt_id);
			printaWindow.setVisible(true);
		}
	});

	

	btnReg.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String customerName = tfname.getText().trim();
			String movieTitle = tfmov.getText().trim();
			double price = Double.parseDouble(tfprc.getText());
			String seatNumber = tfSeat.getText().trim();
			String priority = (String) priorityComboBox.getSelectedItem();
	
			if (customerName.isEmpty() || movieTitle.isEmpty() || seatNumber.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Please fill in all required fields.");
				return;
			}
	
			Connection conn = null;
			PreparedStatement preparedStatement = null;
	
			try {
				final String DB_URL = "jdbc:mysql://localhost:3306/tiketa";
				final String USERNAME = "root";
				final String PASSWORD = "";
				conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
	
				java.sql.Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
	
				String sql = "INSERT INTO registrazione (C_nme, movie, prc, seat, prty, RegDateTime) VALUES (?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, customerName);
				preparedStatement.setString(2, movieTitle);
				preparedStatement.setDouble(3, price);
				preparedStatement.setString(4, seatNumber);
				preparedStatement.setString(5, priority);
				preparedStatement.setTimestamp(6, timestamp);
				preparedStatement.executeUpdate();
	
				tfname.setText("");
				tfmov.setText("");
				tfprc.setText("400");
				tfSeat.setText("");
				priorityComboBox.setSelectedIndex(0);
				JOptionPane.showMessageDialog(null, "Ticket registered successfully.");
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
	});
	
	  btnShow.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
				frame.dispose(); 
				DataDisplay dataDisplay = new DataDisplay(frame, tableModel);
				dataDisplay.setVisible(true);
						
			}
		});
				
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
   			 frame.setContentPane (new JPanel ()
			  {@Override protected void paintComponent (Graphics g)
			  {super.paintComponent (g);
			  g.setColor (new Color (128, 0, 0));
			  g.fillRect (0, 0, getWidth (), getHeight ());}
			  });
			frame.setTitle ("Ticket Registration");
			frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
			frame.add (fPanel, BorderLayout.CENTER  );
			frame.add(btnShow, BorderLayout.SOUTH);  
			frame.add(btnReg, BorderLayout.SOUTH);   
			frame.setLocationRelativeTo (null);
			frame.setVisible (true);
			buttonPanel.add(btnRegisterAndPrint);
			buttonPanel.add (btnShow);
			buttonPanel.add (btnReg);
			ttlPanel.setBackground(new Color(128,0,0));

    JPanel containerPanel = new JPanel (new BorderLayout ());


    containerPanel.setBackground (new Color (128, 0, 0));	
    containerPanel.add (ttlPanel, BorderLayout.NORTH);	
    containerPanel.add (imagePanel, BorderLayout.WEST);	
    containerPanel.add (fPanel, BorderLayout.CENTER);	
    containerPanel.add (buttonPanel, BorderLayout.SOUTH);	
    frame.add (containerPanel, BorderLayout.CENTER);	

	btnReg.setBackground (new Color (0, 128, 0));
	btnReg.setForeground (Color.WHITE);
	btnShow.setBackground (new Color (255, 165, 0));
	btnShow.setForeground (Color.WHITE);
	btnRegisterAndPrint.setBackground(Color.blue);
	btnRegisterAndPrint.setForeground(Color.white);
	
	btnReg.addMouseListener (new java.awt.event.MouseAdapter () {
		public void mouseEntered (java.awt.event.MouseEvent evt){}
		public void mouseExited (java.awt.event.MouseEvent evt) {
			btnReg.setBackground (new Color (0, 128, 0));
		}
	});
	
	btnShow.addMouseListener (new java.awt.event.MouseAdapter () {
		public void mouseEntered (java.awt.event.MouseEvent evt) {
			btnShow.setBackground (new Color (128, 0, 0));
		}
		public void mouseExited (java.awt.event.MouseEvent evt) {
			btnShow.setBackground (new Color (255, 165, 0));
		}
	});
	
		bgpanel = new JPanel();
		bgpanel.setLayout(new BorderLayout());
		frame.getContentPane().add(bgpanel, BorderLayout.CENTER);
		String imagePath = "/Pics/stripe.png";
		ImageIcon imageIcon = new ImageIcon(getClass().getResource(imagePath));
		JLabel label_img = new JLabel(imageIcon);
		label_img.setHorizontalAlignment(SwingConstants.CENTER);
		bgpanel.add(label_img, BorderLayout.SOUTH);



  }

}
