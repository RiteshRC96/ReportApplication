// added cut length, minimum delivery, rolling/folding

package com.project.login.controller;

import com.project.login.entity.User;
import com.project.login.entity.gen_bill;
import com.project.login.service.JobContractService;
import com.project.login.service.QualityMasterService;
import com.project.login.service.WeaverTraderService;
import com.project.login.service.ContractImageGenerationService;
import com.project.security.CustomUserDetails;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;

@Controller
public class JobContractController {

    private final QualityMasterService qulitymasterMasterService;
    private final JobContractService jobContractService;
    private final WeaverTraderService weaverTraderService;
    private final ContractImageGenerationService contractImageGenerationService;

    public JobContractController(JobContractService jobContractService,
            WeaverTraderService weaverTraderService,
            QualityMasterService qulitymasterMasterService,
            ContractImageGenerationService contractImageGenerationService) {
        this.qulitymasterMasterService = qulitymasterMasterService;
        this.jobContractService = jobContractService;
        this.weaverTraderService = weaverTraderService;
        this.contractImageGenerationService = contractImageGenerationService;
    }

    /*
     * =====================
     * SHOW GEN BILL PAGE (CREATE)
     * =====================
     */
    @GetMapping("/gen-bill")
    public String genBillPage(Model model, Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Long userId = userDetails.getId();

        model.addAttribute("weavers", weaverTraderService.getWeavers(userId));
        model.addAttribute("traders", weaverTraderService.getTraders(userId));
        model.addAttribute("quality", qulitymasterMasterService.findByUser(userId));

        model.addAttribute("editMode", false);
        model.addAttribute("job", new gen_bill());

        gen_bill job = new gen_bill();
        job.setContractDate(java.time.LocalDate.now()); // ✅ Set today's date
        
        // Auto-generate next contract number
        Integer nextContractNo = jobContractService.generateContractNo(userId);
        job.setContractNo(nextContractNo);

        model.addAttribute("job", job);

        return "gen_bill";
    }

    /*
     * =====================
     * SHOW GEN BILL PAGE (EDIT)
     * =====================
     */
    @GetMapping("/gen-bill/edit/{userId}/{contractNo}")
    public String editGenBill(
            @PathVariable Long userId,
            @PathVariable Integer contractNo, // ✅ FIXED
            Model model,
            Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 🔐 Security check
        if (!userDetails.getId().equals(userId)) {
            return "redirect:/report";
        }

        gen_bill job = jobContractService
                .getByUserIdAndContractNo(userId, contractNo); // ✅ FIXED

        model.addAttribute("weavers", weaverTraderService.getWeavers(userId));
        model.addAttribute("traders", weaverTraderService.getTraders(userId));
        model.addAttribute("quality", qulitymasterMasterService.findByUser(userId));

        model.addAttribute("editMode", true);
        model.addAttribute("job", job);

        return "gen_bill";
    }

    /*
     * =====================
     * SAVE / UPDATE BILL
     * =====================
     */
    @PostMapping(value = "/gen_bill", consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public ResponseEntity<String> saveBill(

            @RequestParam(required = false) Integer contractNo, // ✅ FIXED

            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate contract_date,

            @RequestParam String weaver_name,
            @RequestParam String trader_name,
            @RequestParam String quality,
            @RequestParam Integer quantity_meters,
            @RequestParam Double job_rate,
            @RequestParam Integer payment_days,
            @RequestParam Integer beams,
            @RequestParam String production_schedule,
            @RequestParam Integer no_of_machines,
            @RequestParam(required = false) String remark,
            @RequestParam(required = false) String cut_length,
            @RequestParam(required = false) String minimum_delivery,
            @RequestParam(required = false) String rolling_folding,
            @RequestParam(required = false) String sizing_fabric,
            @RequestParam(required = false) Double pick,
            @RequestParam(required = false) Double weaver_brokerage_percent,
            @RequestParam(required = false) Double weaver_brokerage_paisa,
            @RequestParam(required = false) Double rate,
            @RequestParam(required = false) Double amount,
            @RequestParam(required = false) Double brokerage_percent_amt,
            @RequestParam(required = false) Double brokerage_mtr_amt) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(401).body("UNAUTHORIZED");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        try {
            User user = new User();
            user.setId(userDetails.getId());
            user.setName(userDetails.getName());

            gen_bill bill = new gen_bill();

            // 🔑 UPDATE CASE
            if (contractNo != null) {
                bill.setContractNo(contractNo); // ✅ FIXED
            }

            bill.setUserId(user.getId()); // ✅ FIXED
            bill.setContractDate(contract_date);
            bill.setWeaverName(weaver_name);
            bill.setTraderName(trader_name);
            bill.setBrokerName(userDetails.getName());
            bill.setQuality(quality);
            bill.setQuantityMeters(quantity_meters);
            bill.setJobRate(job_rate);
            bill.setPaymentDays(payment_days);
            bill.setProductionSchedule(production_schedule);
            bill.setNoOfMachines(no_of_machines);
            bill.setRemark(remark);
            bill.setBeams(beams);
            bill.setCutLength(cut_length);
            bill.setMinimumDelivery(minimum_delivery);
            bill.setRollingFolding(rolling_folding);
            bill.setSizingfabric(sizing_fabric);
            bill.setPick(pick);
            bill.setWeaverBrokeragePercent(weaver_brokerage_percent);
            bill.setWeaverBrokeragePaisa(weaver_brokerage_paisa);
            bill.setRate(rate);
            bill.setAmount(amount);
            bill.setBrokeragePercentAmt(brokerage_percent_amt);
            bill.setBrokerageMtrAmt(brokerage_mtr_amt);

            // 🔄 Same service handles save or update
            gen_bill savedBill = jobContractService.saveOrUpdate(bill, user);
            
            return ResponseEntity.ok("SUCCESS:" + userDetails.getId() + ":" + savedBill.getContractNo());

        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = "ERROR: " + (e.getMessage() != null ? e.getMessage() : e.toString());
            System.err.println("Job Contract Save Error: " + errorMsg);
            return ResponseEntity.internalServerError().body(errorMsg);
        }

    }

    /*
     * =====================
     * API: GET WEAVER DETAILS (FOR AUTO-FILL)
     * =====================
     */
    @GetMapping("/api/weaver-details/{weaverName}")
    @ResponseBody
    public ResponseEntity<?> getWeaverDetails(
            @PathVariable String weaverName,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Long userId = userDetails.getId();

        var weaver = weaverTraderService.findByNameAndUser(weaverName, userId)
                .map(w -> java.util.Map.of(
                        "brokeragePercent", w.getWeaverBrokeragePercent(),
                        "brokeragePaisa", w.getWeaverBrokeragePaisa()))
                .orElse(null);

        if (weaver == null) {
            return ResponseEntity.ok(java.util.Map.of(
                    "brokeragePercent", 0.0,
                    "brokeragePaisa", 0.0));
        }

        return ResponseEntity.ok(weaver);
    }

    /*
     * =====================
     * API: GET QUALITY DETAILS (FOR AUTO-FILL PICK)
     * =====================
     */
    @GetMapping("/api/quality-details")
    @ResponseBody
    public ResponseEntity<?> getQualityDetails(
            @RequestParam String qualityName,
            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Long userId = userDetails.getId();

        System.out.println("✓ API Called: /api/quality-details?qualityName=" + qualityName + " for userId=" + userId);

        var quality = qulitymasterMasterService.findByNameAndUser(qualityName, userId)
                .map(q -> {
                    System.out.println("✓ Quality Found: " + q.getQualityName() + " | Pick=" + q.getPick());
                    return java.util.Map.of(
                            "pick", q.getPick() != null ? q.getPick() : "0",
                            "qualityName", q.getQualityName(),
                            "width", q.getWidth() != null ? q.getWidth() : "",
                            "reed", q.getReed() != null ? q.getReed() : "",
                            "warp", q.getWarp() != null ? q.getWarp() : "",
                            "weft", q.getWeft() != null ? q.getWeft() : "",
                            "weave", q.getWeave() != null ? q.getWeave() : "");
                })
                .orElse(null);

        if (quality == null) {
            System.out.println("✗ Quality NOT Found for: " + qualityName + " | userId=" + userId);
            return ResponseEntity.ok(java.util.Map.of(
                    "pick", "0",
                    "error", "Quality not found: " + qualityName));
        }

        System.out.println("✓ Returning quality data: " + quality);
        return ResponseEntity.ok(quality);
    }

    /*
     * =====================
     * API: ADD NEW WEAVER
     * =====================
     */
    @PostMapping("/api/add-weaver")
    @ResponseBody
    public ResponseEntity<?> addWeaver(
            @RequestParam String name,
            @RequestParam Long phno,
            @RequestParam(defaultValue = "WEAVER") String type,
            @RequestParam(defaultValue = "0") Double brokeragePercent,
            @RequestParam(defaultValue = "0") Double brokeragePaisa,
            Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            com.project.login.entity.WeaverTrader weaver = new com.project.login.entity.WeaverTrader();
            weaver.setName(name);
            weaver.setphno(phno);
            weaver.setType(type);
            weaver.setWeaverBrokeragePercent(brokeragePercent);
            weaver.setWeaverBrokeragePaisa(brokeragePaisa);
            weaver.setUserId(userDetails.getId());

            com.project.login.entity.WeaverTrader saved = weaverTraderService.save(weaver);

            return ResponseEntity.ok(java.util.Map.of(
                    "id", saved.getId(),
                    "name", saved.getName(),
                    "phno", saved.getphno(),
                    "type", saved.getType(),
                    "brokeragePercent", saved.getWeaverBrokeragePercent(),
                    "brokeragePaisa", saved.getWeaverBrokeragePaisa()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }

    /*
     * =====================
     * API: ADD NEW TRADER
     * =====================
     */
    @PostMapping("/api/add-trader")
    @ResponseBody
    public ResponseEntity<?> addTrader(
            @RequestParam String name,
            @RequestParam Long phno,
            @RequestParam(defaultValue = "TRADER") String type,
            @RequestParam(defaultValue = "0") Double brokeragePercent,
            @RequestParam(defaultValue = "0") Double brokeragePaisa,
            Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            com.project.login.entity.WeaverTrader trader = new com.project.login.entity.WeaverTrader();
            trader.setName(name);
            trader.setphno(phno);
            trader.setType(type);
            trader.setWeaverBrokeragePercent(brokeragePercent);
            trader.setWeaverBrokeragePaisa(brokeragePaisa);
            trader.setUserId(userDetails.getId());

            com.project.login.entity.WeaverTrader saved = weaverTraderService.save(trader);

            return ResponseEntity.ok(java.util.Map.of(
                    "id", saved.getId(),
                    "name", saved.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }

    /*
     * =====================
     * API: ADD NEW QUALITY
     * =====================
     */
    @PostMapping("/api/add-quality")
    @ResponseBody
    public ResponseEntity<?> addQuality(
            @RequestParam String qualityName,
            @RequestParam(required = false) String pick,
            @RequestParam(required = false) String width,
            @RequestParam(required = false) String reed,
            @RequestParam(required = false) String warp,
            @RequestParam(required = false) String weft,
            @RequestParam(required = false) String weave,
            Authentication authentication) {
        try {
            // Validate quality name
            if (qualityName == null || qualityName.trim().isEmpty()) {
                throw new IllegalArgumentException("Quality name cannot be empty");
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            System.out.println("✓ Adding Quality: " + qualityName + " for userId=" + userDetails.getId());

            com.project.login.entity.QualityMasterEntity quality = new com.project.login.entity.QualityMasterEntity();
            quality.setQualityName(qualityName);
            quality.setPick(pick);
            quality.setWidth(width);
            quality.setReed(reed);
            quality.setWarp(warp);
            quality.setWeft(weft);
            quality.setWeave(weave);
            quality.setUserId(userDetails.getId());

            com.project.login.entity.QualityMasterEntity saved = qulitymasterMasterService.save(quality);

            System.out.println("✓ Quality saved successfully with ID: " + saved.getId());

            return ResponseEntity.ok(java.util.Map.of(
                    "id", saved.getId(),
                    "qualityName", saved.getQualityName(),
                    "pick", saved.getPick() != null ? saved.getPick() : ""));
        } catch (Exception e) {
            System.err.println("✗ Error adding quality: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of("error", "Failed to add quality: " + e.getMessage()));
        }
    }

    /*
     * =====================
     * DOWNLOAD CONTRACT PDF
     * =====================
     */
    @GetMapping("/api/download-contract-pdf/{userId}/{contractNo}")
    public ResponseEntity<?> downloadContractPdf(
            @PathVariable Long userId,
            @PathVariable Integer contractNo,
            Authentication authentication) {

        try {

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Security check
            if (!userDetails.getId().equals(userId)) {
                return ResponseEntity.status(403).body("Access Denied");
            }

            // Fetch contract
            gen_bill contract = jobContractService.getByUserIdAndContractNo(userId, contractNo);

            if (contract == null) {
                return ResponseEntity.status(404).body("Contract not found");
            }

            // Generate PDF
            byte[] pdfBytes = contractImageGenerationService.generateContract(contract);

            // Prepare download response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            String filename = "Contract_" + contract.getContractNo() + "_" +
                    contract.getWeaverName() + ".pdf";

            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {

            System.err.println("Error downloading contract PDF: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body("Failed to download contract PDF");
        }
    }

    /*
     * =====================
     * DOWNLOAD CONTRACT IMAGE
     * =====================
     */
    @GetMapping("/api/download-contract-image/{userId}/{contractNo}")
    public ResponseEntity<?> downloadContractImage(
            @PathVariable Long userId,
            @PathVariable Integer contractNo,
            Authentication authentication) {

        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Security check
            if (!userDetails.getId().equals(userId)) {
                return ResponseEntity.status(403).body("Access Denied");
            }

            // Fetch contract
            gen_bill contract = jobContractService.getByUserIdAndContractNo(userId, contractNo);

            if (contract == null) {
                return ResponseEntity.status(404).body("Contract not found");
            }

            // Generate Image
            byte[] imageBytes = contractImageGenerationService.generateContractImage(contract);

            // Prepare download response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            String filename = "Contract_" + contract.getContractNo() + "_" +
                    contract.getWeaverName() + ".jpg";

            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(imageBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageBytes);

        } catch (Exception e) {
            System.err.println("Error downloading contract image: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Failed to download contract image");
        }
    }


    /*
     * =====================
     * GET CONTRACT DATA FOR SHARING
     * =====================
     */
    @GetMapping("/api/contract-data/{userId}/{contractNo}")
    @ResponseBody
    public ResponseEntity<?> getContractDataForSharing(
            @PathVariable Long userId,
            @PathVariable Integer contractNo,
            Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Security check
            if (!userDetails.getId().equals(userId)) {
                return ResponseEntity.status(403).body(java.util.Map.of("error", "Access Denied"));
            }

            // Fetch contract
            gen_bill contract = jobContractService.getByUserIdAndContractNo(userId, contractNo);

            if (contract == null) {
                return ResponseEntity.status(404).body(java.util.Map.of("error", "Contract not found"));
            }

            // Return contract data as JSON
            return ResponseEntity.ok(java.util.Map.ofEntries(
                    java.util.Map.entry("contractNo", contract.getContractNo()),
                    java.util.Map.entry("contractDate", contract.getContractDate()),
                    java.util.Map.entry("weaverName", contract.getWeaverName()),
                    java.util.Map.entry("traderName", contract.getTraderName()),
                    java.util.Map.entry("brokerName", contract.getBrokerName()),
                    java.util.Map.entry("quality", contract.getQuality()),
                    java.util.Map.entry("quantityMeters", contract.getQuantityMeters()),
                    java.util.Map.entry("beams", contract.getBeams()),
                    java.util.Map.entry("jobRate", contract.getJobRate()),
                    java.util.Map.entry("paymentDays", contract.getPaymentDays()),
                    java.util.Map.entry("productionSchedule", contract.getProductionSchedule()),
                    java.util.Map.entry("noOfMachines", contract.getNoOfMachines()),
                    java.util.Map.entry("cutLength", contract.getCutLength()),
                    java.util.Map.entry("minimumDelivery", contract.getMinimumDelivery()),
                    java.util.Map.entry("rollingFolding", contract.getRollingFolding()),
                    java.util.Map.entry("sizingFabric", contract.getSizingfabric()),
                    java.util.Map.entry("remark", contract.getRemark())));

        } catch (Exception e) {
            System.err.println("Error fetching contract data: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(
                    java.util.Map.of("error", "Failed to fetch contract data: " + e.getMessage()));
        }
    }
}
