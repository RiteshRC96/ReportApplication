package com.project.login.service;

import com.project.login.entity.gen_bill;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
public class ContractImageGenerationService {

    public byte[] generateContract(gen_bill contract) {

        try {

            InputStream templateStream =
                    new ClassPathResource("static/job_Contract.pdf").getInputStream();

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
                canvas.setTextMatrix(200, 715); // Adjust X,Y
                canvas.showText(contract.getContractNo().toString());
            }

            if (contract.getContractDate() != null) {
                canvas.setTextMatrix(460, 715); 
                canvas.showText(contract.getContractDate().toString());
            }

            /* ---------------------------
               PARTIES
            ----------------------------*/
            if (contract.getWeaverName() != null) {
                canvas.setTextMatrix(200, 680);
                canvas.showText(contract.getWeaverName());
            }

            if (contract.getTraderName() != null) {
                canvas.setTextMatrix(200, 655);
                canvas.showText(contract.getTraderName());
            }

            if (contract.getBrokerName() != null) {
                canvas.setTextMatrix(200, 630);
                canvas.showText(contract.getBrokerName());
            }

            if (contract.getQuality() != null) {
                canvas.setTextMatrix(200, 605);
                canvas.showText(contract.getQuality());
            }

            /* ---------------------------
               JOB DETAILS
            ----------------------------*/
            // Left Column (Quantity, Rate, Schedule, Remark)
            if (contract.getQuantityMeters() != null) {
                canvas.setTextMatrix(200, 560);
                canvas.showText(contract.getQuantityMeters().toString());
            }

            if (contract.getJobRate() != null) {
                canvas.setTextMatrix(200, 535);
                canvas.showText(contract.getJobRate().toString());
            }

            if (contract.getProductionSchedule() != null) {
                canvas.setTextMatrix(200, 510);
                canvas.showText(contract.getProductionSchedule());
            }

            if (contract.getRemark() != null) {
                canvas.setTextMatrix(200, 485);
                canvas.showText(contract.getRemark());
            }

            // Right Column (Beams, Payment Days, Machines)
            if (contract.getBeams() != null) {
                canvas.setTextMatrix(450, 560);
                canvas.showText(contract.getBeams().toString());
            }

            if (contract.getPaymentDays() != null) {
                canvas.setTextMatrix(450, 535);
                canvas.showText(contract.getPaymentDays().toString());
            }

            if (contract.getNoOfMachines() != null) {
                canvas.setTextMatrix(450, 510);
                canvas.showText(contract.getNoOfMachines().toString());
            }

            // Bottom Section (Cut length, delivery, folding)
            if (contract.getCutLength() != null) {
                canvas.setTextMatrix(100, 420);
                canvas.showText(contract.getCutLength());
            }

            if (contract.getMinimumDelivery() != null) {
                canvas.setTextMatrix(280, 420);
                canvas.showText(contract.getMinimumDelivery());
            }

            if (contract.getRollingFolding() != null) {
                canvas.setTextMatrix(450, 420);
                canvas.showText(contract.getRollingFolding());
            }

            canvas.endText();

            pdfDoc.close();

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException("Error generating contract PDF", e);

        }
    }
}