package com.project.login.controller;

import com.project.login.entity.gen_bill;
import com.project.login.service.JobContractService;
import com.project.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ReportController {

    private final JobContractService jobContractService;

    public ReportController(JobContractService jobContractService) {
        this.jobContractService = jobContractService;
    }

    /* ==========================
       REPORT PAGE
       ========================== */
    @GetMapping("/report")
    public String reportPage(
            @RequestParam(required = false) String weaverName,
            @RequestParam(required = false) String traderName,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate,
            Model model
    ) {
    	System.out.println("Page Visited: " + "Report");

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String userName = userDetails.getName();

        List<gen_bill> reports = jobContractService.searchReportsByUser(
                userName,
                weaverName,
                traderName,
                fromDate,
                toDate
        );

        model.addAttribute("reports", reports);
        model.addAttribute("weaverName", weaverName);
        model.addAttribute("traderName", traderName);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        return "report";
    }

    /* ==========================
       EXCEL EXPORT
       ========================== */
    @GetMapping("/report/excel")
    public void exportExcel(
            @RequestParam(required = false) String weaverName,
            @RequestParam(required = false) String traderName,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate,
            HttpServletResponse response
    ) throws IOException {
    	System.out.println("Page Visited: " + "Convert to excel");

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        String userName = userDetails.getName();

        List<gen_bill> data = jobContractService.searchReportsByUser(
                userName,
                weaverName,
                traderName,
                fromDate,
                toDate
        );

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Job Contract Report");

        // ===== HEADER =====
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        Row header = sheet.createRow(0);
        String[] columns = {
        	    "Contract No", "Sr No", "Contract Date", "Weaver", "Trader", "Broker",
        	    "Quality", "Quantity (Meters)", "Beams", "Job Rate", "Payment Days",
        	    "Production Schedule", "Machines", "Remark",
        	    "Cut Length", "Minimum Delivery", "Rolling / Folding",
        	    "Created At"
        	};


        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i);
        }

        // ===== DATA =====
        int rowNum = 1;
        for (gen_bill g : data) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(g.getContractNo());
            row.createCell(1).setCellValue(g.getSrNo());
            row.createCell(2).setCellValue(String.valueOf(g.getContractDate()));
            row.createCell(3).setCellValue(g.getWeaverName());
            row.createCell(4).setCellValue(g.getTraderName());
            row.createCell(5).setCellValue(g.getBrokerName());
            row.createCell(6).setCellValue(g.getQuality());
            row.createCell(7).setCellValue(g.getQuantityMeters());
            row.createCell(8).setCellValue(g.getBeams());
            row.createCell(9).setCellValue(g.getJobRate());
            row.createCell(10).setCellValue(g.getPaymentDays());
            row.createCell(11).setCellValue(g.getProductionSchedule());
            row.createCell(12).setCellValue(g.getNoOfMachines());
            row.createCell(13).setCellValue(g.getRemark());
            row.createCell(14).setCellValue(g.getCutLength());
            row.createCell(15).setCellValue(g.getMinimumDelivery());
            row.createCell(16).setCellValue(g.getRollingFolding());
            row.createCell(17).setCellValue(String.valueOf(g.getCreatedAt()));

            row.createCell(14).setCellValue(String.valueOf(g.getCreatedAt()));
        }

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        );
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=job_contract_report.xlsx"
        );

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
