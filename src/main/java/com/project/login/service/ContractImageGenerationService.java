package com.project.login.service;

import com.project.login.entity.gen_bill;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;

@Service
public class ContractImageGenerationService {

    // ---------------- MERGED SERVICE LOGIC (FAST & EFFICIENT) ----------------

    public byte[] generateContract(gen_bill contract) {
        try (PDDocument document = createFilledDocument(contract)) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating contract PDF", e);
        }
    }

    public byte[] generateContractImage(gen_bill contract) {
        try (PDDocument document = createFilledDocument(contract)) {
            PDFRenderer renderer = new PDFRenderer(document);

            // 🚀 Performance Tip: Use 150 DPI instead of 300 for 4x faster rendering
            // 150 DPI is still very clear for a document
            BufferedImage image = renderer.renderImageWithDPI(0, 150);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating contract image", e);
        }
    }

    /**
     * Reusable logic to load the PDF template and fill it with data.
     * This avoids reloading the document and significantly speeds up processing.
     */
    private PDDocument createFilledDocument(gen_bill contract) throws Exception {
        InputStream templateStream = new ClassPathResource("static/job_Contract.pdf").getInputStream();
        PDDocument document = PDDocument.load(templateStream);
        PDPage page = document.getPage(0);

        // Append to existing page content
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
                PDPageContentStream.AppendMode.APPEND, true, true)) {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Define fonts
            PDType1Font font = PDType1Font.HELVETICA;
            PDType1Font boldFont = PDType1Font.HELVETICA_BOLD;

            // Helper to draw text at coordinates
            drawText(contentStream, font, 13, 160, 638, contract.getContractNo());
            drawText(contentStream, font, 13, 480, 638,
                    contract.getContractDate() != null ? contract.getContractDate().format(dtf) : null);
            drawText(contentStream, font, 13, 140, 600, capitalizeWords(contract.getWeaverName()));
            drawText(contentStream, font, 13, 140, 560, capitalizeWords(contract.getTraderName()));
            drawText(contentStream, font, 13, 140, 520, capitalizeWords(contract.getBrokerName()));
            drawText(contentStream, font, 13, 140, 483, contract.getQuality());
            drawText(contentStream, font, 13, 140, 447, contract.getQuantityMeters());

            if (contract.getSizingfabric() != null) {
                String sf = contract.getSizingfabric().replace(",", "").trim();
                drawText(contentStream, font, 13, 180, 447, sf);
            }

            drawText(contentStream, font, 13, 450, 447, contract.getBeams());
            drawText(contentStream, font, 13, 135, 407, contract.getJobRate());
            drawText(contentStream, font, 13, 445, 407, contract.getPaymentDays());
            drawText(contentStream, font, 13, 250, 370, contract.getProductionSchedule());
            drawText(contentStream, font, 13, 550, 370, contract.getNoOfMachines());
            drawText(contentStream, font, 13, 140, 333, contract.getRemark());
            drawText(contentStream, font, 13, 140, 295, contract.getCutLength());
            drawText(contentStream, font, 13, 365, 295, contract.getMinimumDelivery());
            drawText(contentStream, font, 13, 395, 295, contract.getRollingFolding());
        }

        return document;
    }

    private void drawText(PDPageContentStream contentStream, PDType1Font font, float size, float x, float y,
            Object value) throws Exception {
        if (value == null)
            return;
        String text = value.toString();
        if (text.isEmpty())
            return;

        contentStream.beginText();
        contentStream.setFont(font, size);
        contentStream.newLineAtOffset(x, y);
        try {
            contentStream.showText(text);
        } catch (IllegalArgumentException e) {
            // Handle characters not supported by the font by stripping them or using a
            // replacement
            contentStream.showText(text.replaceAll("[^\\x00-\\x7F]", "?"));
        }
        contentStream.endText();
    }

    private String capitalizeWords(String str) {
        if (str == null || str.isEmpty())
            return str;

        StringBuilder sb = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : str.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                sb.append(c);
            } else if (capitalizeNext) {
                sb.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }
}