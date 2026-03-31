package com.project.login.service;

import com.project.login.entity.gen_bill;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;

@Service
public class ContractImageGenerationService {

    public byte[] generateContract(gen_bill contract) {
        try {
            InputStream templateStream =
                    new ClassPathResource("static/job_Contract_New.pdf").getInputStream();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            PdfReader reader = new PdfReader(templateStream);
            PdfWriter writer = new PdfWriter(outputStream);

            PdfDocument pdfDoc = new PdfDocument(reader, writer);

            PdfPage page = pdfDoc.getFirstPage();

            PdfCanvas canvas = new PdfCanvas(page);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            canvas.beginText();
            canvas.setFontAndSize(font, 11);

            /* ---------------------------
               CONTRACT INFORMATION
            ----------------------------*/
            if (contract.getContractNo() != null) {
                canvas.setTextMatrix(160, 638); // Adjust X,Y
                canvas.showText(contract.getContractNo().toString());
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            if (contract.getContractDate() != null) {
                canvas.setTextMatrix(480, 638); 
                canvas.showText(contract.getContractDate().format(dtf));
            }

            /* ---------------------------
               PARTIES
            ----------------------------*/
            if (contract.getWeaverName() != null) {
                canvas.setTextMatrix(140, 600);
                canvas.showText(capitalizeWords(contract.getWeaverName()));
            }

            if (contract.getTraderName() != null) {
                canvas.setTextMatrix(140, 560);
                canvas.showText(capitalizeWords(contract.getTraderName()));
            }

            if (contract.getBrokerName() != null) {
                canvas.setTextMatrix(140, 520);
                canvas.showText(capitalizeWords(contract.getBrokerName()));
            }

            if (contract.getQuality() != null) {
                canvas.setTextMatrix(140, 483);
                canvas.showText(contract.getQuality());
            }

            /* ---------------------------
               JOB DETAILS
            ----------------------------*/
            if (contract.getQuantityMeters() != null) {
                canvas.setTextMatrix(140, 447);
                canvas.showText(contract.getQuantityMeters().toString());
            }
            
            if (contract.getSizingfabric()!= null) {
                canvas.setTextMatrix(180, 447);
                PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
                canvas.setFontAndSize(boldFont, 11);
                String sf = contract.getSizingfabric().replace(",", "").trim().toUpperCase();
                canvas.showText(sf);
                canvas.setFontAndSize(font, 11); // switch back
            }
            
            if (contract.getBeams() != null) {
                canvas.setTextMatrix(450, 447);
                canvas.showText(contract.getBeams().toString());
            }

            if (contract.getJobRate() != null) {
                canvas.setTextMatrix(140, 405);
                canvas.showText(contract.getJobRate().toString());
            }
            
            if (contract.getPaymentDays() != null) {
                canvas.setTextMatrix(480, 405);
                canvas.showText(contract.getPaymentDays().toString());
            }

            if (contract.getProductionSchedule() != null) {
                canvas.setTextMatrix(250, 370);
                canvas.showText(contract.getProductionSchedule());
            }
            
            if (contract.getNoOfMachines() != null) {
                canvas.setTextMatrix(550, 370);
                canvas.showText(contract.getNoOfMachines().toString());
            }

            if (contract.getRemark() != null) {
                canvas.setTextMatrix(140, 330);
                canvas.showText(contract.getRemark());
            }
            
            if (contract.getCutLength() != null) {
                canvas.setTextMatrix(140, 295);
                canvas.showText(contract.getCutLength());
            }

            if (contract.getMinimumDelivery() != null) {
                canvas.setTextMatrix(365, 295);
                canvas.showText(contract.getMinimumDelivery());
            }

            if (contract.getRollingFolding() != null) {
                canvas.setTextMatrix(400, 295);
                canvas.showText(contract.getRollingFolding());
            }

            canvas.endText();
            pdfDoc.close();

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating contract PDF", e);
        }
    }

    public byte[] generateContractImage(gen_bill contract) {
        try {
            // Load template image
            InputStream templateStream = 
                    new ClassPathResource("static/job_Contract.jpeg").getInputStream();
            BufferedImage image = ImageIO.read(templateStream);
            Graphics2D g2d = image.createGraphics();

            // Enable anti-aliasing for text
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            // Set Font and Color
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("fonts/ARIAL.TTF");
            if (is == null) {
                throw new RuntimeException("Font file not found: /fonts/ARIAL.TTF");
            }
            
            Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.BOLD, 22f);
            g2d.setFont(font);
            
            g2d.setFont(font);
            g2d.setColor(Color.BLACK);

            // PDF points to Image pixels scale factor
            float scaleX = image.getWidth() / 595f;
            float scaleY = image.getHeight() / 842f;

            /* ---------------------------	
               CONTRACT INFORMATION
            ----------------------------*/
            if (contract.getContractNo() != null) {
                drawText(g2d, contract.getContractNo().toString(), 160, 684, scaleX, scaleY);
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            if (contract.getContractDate() != null) {
                drawText(g2d, contract.getContractDate().format(dtf), 480, 684, scaleX, scaleY);
            }

            /* ---------------------------
               PARTIES
            ----------------------------*/
            if (contract.getWeaverName() != null) {
                drawText(g2d, capitalizeWords(contract.getWeaverName()), 140, 642, scaleX, scaleY);
            }

            if (contract.getTraderName() != null) {
                drawText(g2d, capitalizeWords(contract.getTraderName()), 140, 597, scaleX, scaleY);
            }

            if (contract.getBrokerName() != null) {
                drawText(g2d, capitalizeWords(contract.getBrokerName()), 140, 551, scaleX, scaleY);
            }

            if (contract.getQuality() != null) {
                drawText(g2d, contract.getQuality(), 140, 515, scaleX, scaleY);
            }

            /* ---------------------------
               JOB DETAILS
            ----------------------------*/
            if (contract.getQuantityMeters() != null) {
                drawText(g2d, contract.getQuantityMeters().toString(), 140, 473, scaleX, scaleY);
            }
            
            if (contract.getSizingfabric() != null) {
                Font boldFont = new Font("Arial", Font.BOLD, 22);
                g2d.setFont(boldFont);
                String sf = contract.getSizingfabric().replace(",", "").trim().toUpperCase();
                drawText(g2d, sf, 180, 473, scaleX, scaleY);
                g2d.setFont(font); // switch back
            }
            
            if (contract.getBeams() != null) {
                drawText(g2d, contract.getBeams().toString(), 450, 473, scaleX, scaleY);
            }

            if (contract.getJobRate() != null) {
                drawText(g2d, contract.getJobRate().toString(), 140, 430, scaleX, scaleY);
            }
            
            if (contract.getPaymentDays() != null) {
                drawText(g2d, contract.getPaymentDays().toString(), 474, 430, scaleX, scaleY);
            }

            if (contract.getProductionSchedule() != null) {
                drawText(g2d, contract.getProductionSchedule(), 250, 390, scaleX, scaleY);
            }
            
            if (contract.getNoOfMachines() != null) {
                drawText(g2d, contract.getNoOfMachines().toString(), 545, 390, scaleX, scaleY);
            }

            if (contract.getRemark() != null) {
                drawText(g2d, contract.getRemark(), 140, 350, scaleX, scaleY);
            }

            if (contract.getCutLength() != null) {
                drawText(g2d, contract.getCutLength(), 140, 310, scaleX, scaleY);
            }

            if (contract.getMinimumDelivery() != null) {
                drawText(g2d, contract.getMinimumDelivery(), 365, 310, scaleX, scaleY);
            }

            if (contract.getRollingFolding() != null) {
                drawText(g2d, contract.getRollingFolding(), 390, 310, scaleX, scaleY);
            }

            g2d.dispose();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating contract image", e);
        }
    }

    private void drawText(Graphics2D g2d, String text, float xPoints, float yPoints, float scaleX, float scaleY) {
        int x = (int) (xPoints * scaleX);
        int y = (int) ((842 - yPoints) * scaleY);
        g2d.drawString(text, x, y);
    }

    private String capitalizeWords(String str) {
        if (str == null || str.isEmpty()) return str;
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