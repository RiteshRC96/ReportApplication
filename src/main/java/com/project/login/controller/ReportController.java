package com.project.login.controller;

import com.project.login.entity.gen_bill;
import com.project.login.service.JobContractPdfService;
import com.project.login.service.JobContractService;
import com.project.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/report")
public class ReportController {

        private final JobContractService jobContractService;
        private final JobContractPdfService jobContractPdfService;
        private final com.project.login.service.WalletService walletService;
        private final PasswordEncoder passwordEncoder;

        public ReportController(
                        JobContractService jobContractService,
                        JobContractPdfService jobContractPdfService,
                        com.project.login.service.WalletService walletService,
                        PasswordEncoder passwordEncoder) {
                this.jobContractService = jobContractService;
                this.jobContractPdfService = jobContractPdfService;
                this.walletService = walletService;
                this.passwordEncoder = passwordEncoder;
        }
        @PostMapping("/delete/{userId}/{contractNo}")
        public String deleteJobContract(
                        @PathVariable Long userId,
                        @PathVariable int contractNo,
                        @RequestParam("confirmPassword") String confirmPassword,
                        RedirectAttributes redirectAttributes) {
                System.out.println("Button clicked: Delete Job Contract");
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

                if (passwordEncoder.matches(confirmPassword, userDetails.getPassword())) {
                        jobContractService.deleteByUserIdAndContractNo(userId, contractNo);
                        redirectAttributes.addFlashAttribute("success", "Record deleted successfully.");
                } else {
                        redirectAttributes.addFlashAttribute("error", "Incorrect password! Record was not deleted.");
                }
                return "redirect:/report";
        }

        @GetMapping
        public String reportPage(
                        @RequestParam(required = false) String weaverName,
                        @RequestParam(required = false) String traderName,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                        Model model) {
                System.out.println("Visited page: /report");

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();


                Long userId = userDetails.getId();

                List<gen_bill> allReports = jobContractService.searchReportsByUser(
                                userId,
                                weaverName,
                                traderName,
                                fromDate,
                                toDate);

                // ✅ Add Wallet Balance
                Double walletBalance = walletService.getBalance(userId);
                model.addAttribute("walletBalance", walletBalance);

                model.addAttribute("reports", allReports);
                model.addAttribute("weaverName", weaverName);
                model.addAttribute("traderName", traderName);
                model.addAttribute("fromDate", fromDate);
                model.addAttribute("toDate", toDate);

                return "report";
        }

        @GetMapping("/excel")
        public void exportExcel(
                        @RequestParam(required = false) String weaverName,
                        @RequestParam(required = false) String traderName,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                        @RequestParam(required = false) List<Integer> contractNos,
                        HttpServletResponse response) throws IOException {
                System.out.println("Button clicked: Export Excel");

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

                Long userId = userDetails.getId();

                List<gen_bill> data;
                if (contractNos != null && !contractNos.isEmpty()) {
                        data = jobContractService.getContractsByContractNos(userId, contractNos);
                } else {
                        data = jobContractService.searchReportsByUser(
                                        userId,
                                        weaverName,
                                        traderName,
                                        fromDate,
                                        toDate);
                }

                Workbook workbook = new SXSSFWorkbook(50);
                Sheet sheet = workbook.createSheet("Job Contract Report");

                Row header = sheet.createRow(0);
                String[] columns = {
                                "Contract No", "Contract Date", "Weaver", "Trader", "Quality",
                                "Quantity", "Sizing/Fabric", "Beams", "Job Rate", "Pick", "Rate", "Amount",
                                "Brokerage % Amt", "Brokerage Mtr Amt", "Payment Days",
                                "Production Schedule", "Machines", "Remark",
                                "Cut Length", "Minimum Delivery", "Rolling/Folding", "Created At"
                };

                for (int i = 0; i < columns.length; i++) {
                        header.createCell(i).setCellValue(columns[i]);
                }

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                int rowNum = 1;
                for (gen_bill g : data) {
                        Row row = sheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(g.getContractNo());
                        row.createCell(1).setCellValue(g.getContractDate() != null ? g.getContractDate().format(dtf) : "-");
                        row.createCell(2).setCellValue(g.getWeaverName());
                        row.createCell(3).setCellValue(g.getTraderName());
                        row.createCell(4).setCellValue(g.getQuality());
                        row.createCell(5).setCellValue(g.getQuantityMeters());
                        row.createCell(6).setCellValue(g.getSizingfabric());
                        row.createCell(7).setCellValue(g.getBeams());
                        row.createCell(8).setCellValue(g.getJobRate());
                        row.createCell(9).setCellValue(g.getPick() != null ? g.getPick() : 0.0);
                        row.createCell(10).setCellValue(g.getRate() != null ? g.getRate() : 0.0);
                        row.createCell(11).setCellValue(g.getAmount() != null ? g.getAmount() : 0.0);
                        row.createCell(12).setCellValue(
                                        g.getBrokeragePercentAmt() != null ? g.getBrokeragePercentAmt() : 0.0);
                        row.createCell(13).setCellValue(g.getBrokerageMtrAmt() != null ? g.getBrokerageMtrAmt() : 0.0);
                        row.createCell(14).setCellValue(g.getPaymentDays());
                        row.createCell(15).setCellValue(g.getProductionSchedule());
                        row.createCell(16).setCellValue(g.getNoOfMachines());
                        row.createCell(17).setCellValue(g.getRemark());
                        row.createCell(18).setCellValue(g.getCutLength());
                        row.createCell(19).setCellValue(g.getMinimumDelivery());
                        row.createCell(20).setCellValue(g.getRollingFolding());
                        row.createCell(21).setCellValue(g.getCreatedAt() != null ? g.getCreatedAt().format(dtf) : "-");
                }

                response.setContentType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
                String fileName = "job_contract_report_" + dateStr + ".xlsx";

                response.setHeader(
                                "Content-Disposition",
                                "attachment; filename=" + fileName);

                workbook.write(response.getOutputStream());
                workbook.close();
                ((SXSSFWorkbook) workbook).dispose();
        }
        
        @GetMapping("/pdf")
        public void exportPdf(
                        @RequestParam(required = false) String weaverName,
                        @RequestParam(required = false) String traderName,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                        @RequestParam(required = false) List<Integer> contractNos,
                        HttpServletResponse response) throws IOException {
                System.out.println("Button clicked: Export PDF");

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

                Long userId = userDetails.getId();

                List<gen_bill> data;
                if (contractNos != null && !contractNos.isEmpty()) {
                        data = jobContractService.getContractsByContractNos(userId, contractNos);
                } else {
                        data = jobContractService.searchReportsByUser(
                                        userId,
                                        weaverName,
                                        traderName,
                                        fromDate,
                                        toDate);
                }

                String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                String fileName = "job_contract_report_" + dateStr + ".pdf";

                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

                jobContractPdfService.export(data, response.getOutputStream());
        }
}
