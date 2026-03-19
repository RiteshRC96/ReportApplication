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

            if (contract.getContractDate() != null) {
                canvas.setTextMatrix(480, 638); 
                canvas.showText(contract.getContractDate().toString());
            }

            /* ---------------------------
               PARTIES
            ----------------------------*/
            if (contract.getWeaverName() != null) {
                canvas.setTextMatrix(140, 600);
                canvas.showText(contract.getWeaverName());
            }

            if (contract.getTraderName() != null) {
                canvas.setTextMatrix(140, 560);
                canvas.showText(contract.getTraderName());
            }

            if (contract.getBrokerName() != null) {
                canvas.setTextMatrix(140, 520);
                canvas.showText(contract.getBrokerName());
            }

            if (contract.getQuality() != null) {
                canvas.setTextMatrix(140, 483);
                canvas.showText(contract.getQuality());
            }

            /* ---------------------------
               JOB DETAILS
            ----------------------------*/
            // Left Column (Quantity, Rate, Schedule, Remark)
            if (contract.getQuantityMeters() != null) {
                canvas.setTextMatrix(140, 447);
                canvas.showText(contract.getQuantityMeters().toString());
            }
            
            if (contract.getSizingfabric()!= null) {
                canvas.setTextMatrix(180, 447);
                canvas.showText(contract.getSizingfabric().toString());
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
}