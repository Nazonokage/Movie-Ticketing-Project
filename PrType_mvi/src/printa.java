import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.Date;
//ad
import java.awt.print.Paper;


public class printa extends JFrame {
    private double originalPrice;
    private double discountPercentage;

    public printa(String customerName, String movieTitle, String price, String seatNumber, String priority, int tkt_id) {
        setTitle("Movie Ticket");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("Pics/logo.png")).getImage());

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int width = getWidth();
                int height = getHeight();
                g.setColor(new Color(128, 0, 0));
                int arc = 30;
                g.fillRoundRect(0, 0, width, height, arc, arc);
            }
        };
        panel.setLayout(new BorderLayout());

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/Pics/logoR.png"));
        int targetWidth = 170;
        int targetHeight = 150;
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        originalPrice = Double.parseDouble(price);
        discountPercentage = getDiscountPercentage(priority);

        JTextArea infoTextArea = new JTextArea();
        infoTextArea.setEditable(false);
        infoTextArea.setFont(new Font("Arial", Font.PLAIN, 23));
        infoTextArea.setForeground(Color.black);
        infoTextArea.setBackground(new Color(250, 250, 250));
        infoTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        infoTextArea.setWrapStyleWord(true);
        infoTextArea.setLineWrap(true);
        infoTextArea.append("================================\n");
        infoTextArea.append("Seat Number: " + seatNumber + "\n");
        infoTextArea.append("Movie Title: " + movieTitle + "\n");
        infoTextArea.append("Customer Name: " + customerName + "\n");
        infoTextArea.append("Original Price: ₱ " + price + "\n");
        infoTextArea.append("Priority: " + priority + "\n");
        infoTextArea.append("Date and Time: " + currentDateTime + "\n");
        infoTextArea.append("Discount: " + (discountPercentage * 100) + "%\n");
        double discountedPrice = calculateDiscountedPrice(originalPrice, discountPercentage);
        infoTextArea.append("Discounted Price: ₱ " + String.format("%.2f", discountedPrice) + "\n");
        double vat = calculateVAT(discountedPrice);
        infoTextArea.append("VAT ₱ " + String.format("%.2f", vat) + "\n");
        double total = discountedPrice + vat;
        infoTextArea.append("Total ₱ " + String.format("%.2f", total) + "\n");
        infoTextArea.append("================================\n");
        infoTextArea.append("This ticket is valid for 1 showing.\n");
        infoTextArea.append("Expiration within 1 month if not used.\n");
        infoTextArea.append("================================\n");
        infoTextArea.append("Ticket ID: " + tkt_id + "\n");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(128, 0, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JButton printButton = new JButton("Print");
        JButton back2reg = new JButton("Registration");

        printButton.setBackground(new Color(255, 165, 0));
        printButton.setForeground(Color.WHITE);
        printButton.setFont(new Font("Arial", Font.BOLD, 16));
        printButton.setPreferredSize(new Dimension(120, 40));
        printButton.setFocusPainted(false);
        printButton.setBorder(BorderFactory.createLineBorder(new Color(255, 165, 0), 2));

        back2reg.setBackground(new Color(255, 165, 0));
        back2reg.setForeground(Color.WHITE);
        back2reg.setFont(new Font("Arial", Font.BOLD, 16));
        back2reg.setPreferredSize(new Dimension(120, 40));
        back2reg.setFocusPainted(false);
        back2reg.setBorder(BorderFactory.createLineBorder(new Color(255, 165, 0), 2));
//
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printPanel(panel);
            }
        });

        back2reg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Tickteting_main ticketingMain = new Tickteting_main();
                ticketingMain.initialize();
            }
        });

        buttonPanel.add(printButton);
        buttonPanel.add(back2reg);
        panel.add(imageLabel, BorderLayout.NORTH);
        panel.add(infoTextArea, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(panel);
        setLocationRelativeTo(null);
    }

    private double getDiscountPercentage(String priority) {
        switch (priority) {
            case "VIP":
                return 0.7; 
            case "Student":
            case "Senior":
            case "PWD":
            case "Child":
                return 0.15; 
            default:
                return 0.0; 
        }
    }

    private double calculateDiscountedPrice(double originalPrice, double discountPercentage) {
        return originalPrice - (originalPrice * discountPercentage);
    }

    private double calculateVAT(double discountedPrice) {
        return discountedPrice * 0.11;
    }

        public static void printPanel(JPanel panel) {
                PrinterJob printerJob = PrinterJob.getPrinterJob();
                PageFormat pageFormat = printerJob.defaultPage();
                Paper paper = pageFormat.getPaper();

                // Set paper size to 48mm width and 3276mm height (203dpi)
                double paperWidth = inchesToPoints(48 / 25.4); // Convert mm to inches
                double paperHeight = inchesToPoints(3276 / 25.4); // Convert mm to inches
                paper.setSize(paperWidth, paperHeight);
                paper.setImageableArea(0, 0, paperWidth, paperHeight); // Full paper is imageable

                // Set the adjusted paper to the page format
                pageFormat.setPaper(paper);

                printerJob.setPrintable(new Printable() {
                    @Override
                    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
                        if (pageIndex > 0) {
                            return Printable.NO_SUCH_PAGE;
                        }

                        Graphics2D g2d = (Graphics2D) g;
                        double panelWidth = panel.getWidth();
                        double panelHeight = panel.getHeight();
                        double scaleWidth = paperWidth / panelWidth;
                        double scaleHeight = paperHeight / panelHeight;
                        double scaleFactor = Math.min(scaleWidth, scaleHeight);

                        g2d.scale(scaleFactor, scaleFactor);
                        panel.paint(g2d);

                        return Printable.PAGE_EXISTS;
                    }
                }, pageFormat);

                try {
                    printerJob.print();
                } catch (PrinterException e) {
                    e.printStackTrace();
                }
            }

            private static double inchesToPoints(double inches) {
                return inches * 72; // 1 inch = 72 points
            }
            
            public void print() {
            }
        }
