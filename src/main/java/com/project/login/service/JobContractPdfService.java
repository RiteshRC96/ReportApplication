package com.project.login.service;

import com.project.login.entity.gen_bill;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class JobContractPdfService {

        public ByteArrayInputStream export(List<gen_bill> list) {

                ByteArrayOutputStream out = new ByteArrayOutputStream();

                try {

                        PdfWriter writer = new PdfWriter(out);
                        PdfDocument pdfDocument = new PdfDocument(writer);

                        // Landscape A4
                        Document document = new Document(pdfDocument, PageSize.A4.rotate());
                        document.setMargins(20, 20, 20, 20);

                        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
                        PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

                        // ===== Title =====
                        Paragraph title = new Paragraph("JOB CONTRACT REPORT")
                                        .setFont(boldFont)
                                        .setFontSize(12)
                                        .setTextAlignment(TextAlignment.CENTER);

                        document.add(title);
                        document.add(new Paragraph(" "));

                        Table table = new Table(UnitValue.createPercentArray(20));
                        table.setWidth(UnitValue.createPercentValue(100));
                        table.setAutoLayout();

                        // ===== Header Row =====
                        String[] headers = {
                                        "Contract No", "Contract Date", "Weaver\nTrader", "Quality",
                                        "Quantity", "Sizing/Fabric", "Beams", "Job Rate", "Pick", "Rate", "Amount",
                                        "Brokerage % Amt", "Brokerage Mtr Amt", "Payment Days",
                                        "Production Schedule", "Machines", "Remark",
                                        "Cut Length", "Minimum Delivery", "Rolling/Folding"
                        };

                        for (String header : headers) {
                                table.addHeaderCell(
                                                new Cell()
                                                                .add(new Paragraph(header)
                                                                                .setFont(boldFont)
                                                                                .setFontSize(8))
                                                                .setTextAlignment(TextAlignment.CENTER)
                                                                .setVerticalAlignment(VerticalAlignment.MIDDLE));
                        }

                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        // DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy
                        // HH:mm");

                        // ===== Data Rows =====
                        for (gen_bill bill : list) {

                                Integer qty = bill.getQuantityMeters();
                                Double rate = bill.getJobRate();

                                table.addCell(getCell(String.valueOf(bill.getContractNo()), normalFont));

                                table.addCell(getCell(
                                                bill.getContractDate() != null
                                                                ? bill.getContractDate().format(dateFormatter)
                                                                : "-",
                                                normalFont));

                                // Combine Weaver and Trader
                                String weaver = bill.getWeaverName() != null ? capitalizeInitialLetters(bill.getWeaverName()) : "-";
                                String trader = bill.getTraderName() != null ? capitalizeInitialLetters(bill.getTraderName()) : "-";
                                table.addCell(getCell(weaver + "\n" + trader, normalFont));

                                table.addCell(getCell(bill.getQuality(), normalFont));
                                table.addCell(getCell(qty != null ? qty.toString() : "0", normalFont));
                                table.addCell(getCell(bill.getSizingfabric() != null ? bill.getSizingfabric().toUpperCase() : "-", normalFont));
                                table.addCell(getCell(bill.getBeams() != null ? bill.getBeams().toString() : "0",
                                                normalFont));
                                table.addCell(getCell(rate != null ? rate.toString() : "0", normalFont));

                                // New calculation columns
                                table.addCell(getCell(
                                                bill.getPick() != null ? String.format("%.2f", bill.getPick()) : "0.00",
                                                normalFont));
                                table.addCell(getCell(
                                                bill.getRate() != null ? String.format("%.2f", bill.getRate()) : "0.00",
                                                normalFont));
                                table.addCell(getCell(bill.getAmount() != null ? String.format("%.2f", bill.getAmount())
                                                : "0.00", normalFont));
                                table.addCell(getCell(bill.getBrokeragePercentAmt() != null
                                                ? String.format("%.2f", bill.getBrokeragePercentAmt())
                                                : "0.00", normalFont));
                                table.addCell(getCell(bill.getBrokerageMtrAmt() != null
                                                ? String.format("%.2f", bill.getBrokerageMtrAmt())
                                                : "0.00", normalFont));

                                table.addCell(getCell(
                                                bill.getPaymentDays() != null ? bill.getPaymentDays().toString() : "-",
                                                normalFont));

                                // ✅ Production Schedule
                                table.addCell(getCell(bill.getProductionSchedule(), normalFont));

                                // ✅ Machines
                                table.addCell(getCell(
                                                bill.getNoOfMachines() != null ? bill.getNoOfMachines().toString()
                                                                : "-",
                                                normalFont));

                                // ✅ Remark
                                table.addCell(getCell(bill.getRemark(), normalFont));

                                table.addCell(getCell(bill.getCutLength(), normalFont));
                                table.addCell(getCell(bill.getMinimumDelivery(), normalFont));
                                table.addCell(getCell(bill.getRollingFolding(), normalFont));
                        }

                        document.add(table);
                        document.close();

                } catch (Exception e) {
                        e.printStackTrace();
                }

                return new ByteArrayInputStream(out.toByteArray());
        }

        private Cell getCell(String text, PdfFont font) {
                return new Cell()
                                .add(new Paragraph(text != null ? text : "-")
                                                .setFont(font)
                                                .setFontSize(7))
                                .setTextAlignment(TextAlignment.LEFT);
        }
        private String capitalizeInitialLetters(String str) {
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
