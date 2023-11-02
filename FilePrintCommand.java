import java.awt.Font;
import java.awt.Graphics;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class FilePrintCommand implements Printable, Command {

    private final Viewer viewer;
    
    private String data;
    
    private Font font;

    private int[] pageBreaks;

    private String[] textLines;

    private ActionController controller;

    public FilePrintCommand(Viewer viewer, ActionController controller) {
        this.viewer = viewer;
        this.controller = controller;
    }

    public void printDocument() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
                javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), "The document has been printed!");
            } catch (PrinterException printerException) {
                System.out.println("Error: " + printerException);
            }
        }
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        int lineHeight = metrics.getHeight();

        textLines = data.split("\n");
        int linesPerPage = ((int) pf.getImageableHeight() - 100) / lineHeight;
        int numBreaks = (textLines.length - 1) / linesPerPage;
        pageBreaks = new int[numBreaks];
        for (int b = 0; b < numBreaks; b++) {
            pageBreaks[b] = (b + 1) * linesPerPage;
        }


        if (pageIndex > pageBreaks.length) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        int y = 50;
        int x = 50;
        int start = (pageIndex == 0) ? 0 : pageBreaks[pageIndex - 1];
        int end = (pageIndex == pageBreaks.length)
                ? textLines.length : pageBreaks[pageIndex];
        for (int line = start; line < end; line++) {
            y += lineHeight;
            g.drawString(textLines[line], x, y);
        }
        return PAGE_EXISTS;
    }

    @Override
    public void execute() {
        Command command = new FileSaveCommand(controller);
        command.execute();

        data = viewer.getTextAreaContent();
        font = viewer.getTextArea().getFont();
        printDocument();
    }
}