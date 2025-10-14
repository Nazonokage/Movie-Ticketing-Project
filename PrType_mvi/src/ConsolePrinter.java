import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.print.Paper;
import java.util.Scanner;

public class ConsolePrinter {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask the user for input
        System.out.println("Enter a string:");

        // Read the input from the user
        String inputString = scanner.nextLine();

        // Validate input
        if (inputString.isEmpty()) {
            System.out.println("Input string cannot be empty.");
            return;
        }

        // Print the input string
        System.out.println("You entered: " + inputString);

        // Print to the physical printer
        if (printToPrinter(inputString)) {
            System.out.println("Printing successful.");
        } else {
            System.out.println("Failed to print.");
        }

        // Close the scanner
        scanner.close();
    }

    private static boolean printToPrinter(String text) {
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
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                // Calculate text position
                int x = (int) (pageFormat.getImageableWidth() / 2); // Center horizontally
                int y = (int) (pageFormat.getImageableHeight() / 2); // Center vertically

                g2d.drawString(text, x, y); // Draw text
                return Printable.PAGE_EXISTS;
            }
        });

        try {
            printerJob.print();
            return true; // Printing successful
        } catch (PrinterException e) {
            e.printStackTrace();
            return false; // Printing failed
        }
    }

    private static double inchesToPoints(double inches) {
        return inches * 72; // 1 inch = 72 points
    }
}
