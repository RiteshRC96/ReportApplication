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
import java.io.OutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import com.project.login.service.WeaverTraderService;
import com.project.login.entity.WeaverTrader;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ContractImageGenerationService {

    @Autowired
    private WeaverTraderService weaverTraderService;

    // ---------------- MERGED SERVICE LOGIC (FAST & EFFICIENT) ----------------

    public void generateContract(gen_bill contract, OutputStream out) {
        try (PDDocument document = createFilledDocument(contract)) {
            document.save(out);
        } catch (Exception e) {
            throw new RuntimeException("Error generating contract PDF", e);
        }
    }

    public void generateContractImage(gen_bill contract, OutputStream out) {
        try (PDDocument document = createFilledDocument(contract)) {
            PDFRenderer renderer = new PDFRenderer(document);
            renderer.setSubsamplingAllowed(true); // Reduces memory consumption for images in PDF

            // 🚀 Performance Tip: Increased from 70 DPI to 120 DPI for better resolution while avoiding OOM.
            // Mobile sharing will look clearer.
            System.gc(); // Hint to JVM to free up memory before allocating large BufferedImage
            BufferedImage image = renderer.renderImageWithDPI(0, 120);

            ImageIO.setUseCache(true); // Use disk cache instead of heap memory to avoid OOM
            ImageIO.write(image, "jpeg", out);
            
            // Release memory immediately
            image.flush();
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
        // Use setupTempFileOnly to prevent Java Heap Space OutOfMemoryError
        PDDocument document = PDDocument.load(templateStream, org.apache.pdfbox.io.MemoryUsageSetting.setupTempFileOnly());
        templateStream.close(); // Prevent resource leak
        PDPage page = document.getPage(0);

        // Append to existing page content
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
                PDPageContentStream.AppendMode.APPEND, true, true)) {

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Define fonts
            PDType1Font font = PDType1Font.HELVETICA;
            PDType1Font boldFont = PDType1Font.HELVETICA_BOLD;

            String weaverStr = capitalizeWords(contract.getWeaverName());
            if (contract.getWeaverId() != null && contract.getUserId() != null) {
                WeaverTrader wt = weaverTraderService.findByIdAndUser(contract.getWeaverId(), contract.getUserId()).orElse(null);
                if (wt != null && wt.getphno() != null) {
                    weaverStr = weaverStr + " -   " + wt.getphno();
                }
            }

            String traderStr = capitalizeWords(contract.getTraderName());
            if (contract.getTraderId() != null && contract.getUserId() != null) {
                WeaverTrader tt = weaverTraderService.findByIdAndUser(contract.getTraderId(), contract.getUserId()).orElse(null);
                if (tt != null && tt.getphno() != null) {
                    traderStr = traderStr + " -   " + tt.getphno();
                }
            }

            // Helper to draw text at coordinates
            drawText(contentStream, font, 12, 160, 638, contract.getContractNo());
            drawText(contentStream, font, 12, 480, 638,
                    contract.getContractDate() != null ? contract.getContractDate().format(dtf) : null);
            drawText(contentStream, font, 12, 140, 600, weaverStr);
            drawText(contentStream, font, 12, 140, 560, traderStr);
            drawText(contentStream, font, 12, 140, 520, capitalizeWords(contract.getBrokerName()));
            drawText(contentStream, font, 12, 140, 483, contract.getQuality());
            drawText(contentStream, font, 12, 140, 445, "Avg. "+contract.getQuantityMeters());
            drawText(contentStream, font, 12, 230, 445, contract.getSizingfabric());
            drawText(contentStream, font, 12, 470, 445, contract.getBeams());
            drawText(contentStream, font, 12, 140, 407, contract.getJobRate());
            drawText(contentStream, font, 12, 445, 407, contract.getPaymentDays());
            drawText(contentStream, font, 12, 250, 370, contract.getProductionSchedule());
            drawText(contentStream, font, 12, 550, 370, contract.getNoOfMachines());
            drawText(contentStream, font, 12, 140, 333, contract.getRemark());
            drawText(contentStream, font, 12, 140, 295, contract.getCutLength());
            drawText(contentStream, font, 12, 365, 295, contract.getMinimumDelivery());
            drawText(contentStream, font, 12, 495, 295, contract.getRollingFolding());
            
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