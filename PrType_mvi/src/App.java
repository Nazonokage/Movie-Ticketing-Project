import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    public App() {
        
        frame = new JFrame("Movie Ticketing System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
        String iconPath = "/Pics/logo.png"; 
        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
        frame.setIconImage(icon.getImage());

        String titleLogoPath = "/Pics/logoR.png"; 
        ImageIcon titleLogoIcon = new ImageIcon(getClass().getResource(titleLogoPath));
        Image titleLogoImage = titleLogoIcon.getImage();

        JLabel titleLabel = new JLabel("Movie Ticketing System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Dubai Medium", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); 

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(128, 0, 0));
        titlePanel.setLayout(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        int textLabelHeight = titleLabel.getPreferredSize().height;

        titleLogoImage = titleLogoImage.getScaledInstance(-1, textLabelHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedLogoIcon = new ImageIcon(titleLogoImage);
        JLabel titleLogoLabel = new JLabel(resizedLogoIcon);

        titlePanel.add(titleLogoLabel, BorderLayout.WEST);

        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(new Color(128, 0, 0, 220)); 
        loginPanel.setLayout(new GridLayout(3, 2));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);

        usernameField = new JTextField(10);
        passwordField = new JPasswordField(10);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(255, 0, 0)); 
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false); 
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                if (("admin".equals(username) && "admin".equals(password)) || ("user".equals(username) && "user".equals(password))) {
                	 String username1 = usernameField.getText();
                     char[] passwordChars1 = passwordField.getPassword();
                     String password1 = new String(passwordChars1);

                     if (("admin".equals(username1) && "admin".equals(password1)) || ("user".equals(username1) && "user".equals(password1))) {
                     	 frame.dispose();
                        Tickteting_main ticketingMain = new Tickteting_main();
                         ticketingMain.initialize();
                        
                     } else {
                         JOptionPane.showMessageDialog(frame, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
                     }                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        Font font = new Font("Dubai", Font.PLAIN, 16);
        usernameLabel.setFont(font);
        passwordLabel.setFont(font);
        usernameField.setFont(font);
        passwordField.setFont(font);
        loginButton.setFont(font);
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel());
        loginPanel.add(loginButton);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(128, 0, 0)); // Dark Red
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(titlePanel, BorderLayout.NORTH);
        backgroundPanel.add(loginPanel, BorderLayout.CENTER);
        frame.setContentPane(backgroundPanel);
        
        String imagePath = "/Pics/stripe.png"; 
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(imagePath));
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(imageLabel, BorderLayout.SOUTH);
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                App loginApp = new App();
                loginApp.show();
            }
        });
    }
}
