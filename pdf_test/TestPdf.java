import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import java.io.FileOutputStream;

public class TestPdf {
    public static void main(String[] args) throws Exception {
        PdfReader reader = new PdfReader("job_Contract.pdf");
        PdfWriter writer = new PdfWriter(new FileOutputStream("output.pdf"));
        PdfDocument pdfDoc = new PdfDocument(reader, writer);
        PdfPage page = pdfDoc.getFirstPage();
        PdfCanvas canvas = new PdfCanvas(page);
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        canvas.beginText();
        canvas.setFontAndSize(font, 11);
        
        drawCrosshair(canvas, 100, 680, "C-NO");
        drawCrosshair(canvas, 430, 680, "DATE"); 
        
        drawCrosshair(canvas, 200, 638, "WEAVER");
        drawCrosshair(canvas, 200, 616, "TRADER"); 
        drawCrosshair(canvas, 200, 592, "BROKER");
        drawCrosshair(canvas, 200, 565, "QUALITY");

        canvas.endText();
        pdfDoc.close();
        System.out.println("Generated output.pdf inside pdf_test");
    }

    private static void drawCrosshair(PdfCanvas canvas, float x, float y, String label) {
        canvas.setStrokeColor(com.itextpdf.kernel.colors.DeviceRgb.RED);
        canvas.circle(x, y, 2);
        canvas.fill();
        canvas.setColor(com.itextpdf.kernel.colors.DeviceRgb.BLACK, true);
        canvas.setTextMatrix(x, y + 5);
        canvas.showText(label);
    }
}
