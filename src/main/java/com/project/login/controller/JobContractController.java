// added cut length, minimum delivery, rolling/folding

package com.project.login.controller;

import com.project.login.entity.User;
import com.project.login.entity.gen_bill;
import com.project.login.service.JobContractService;
import com.project.login.service.WeaverTraderService;
import com.project.security.CustomUserDetails;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class JobContractController {

    private final JobContractService jobContractService;
    private final WeaverTraderService weaverTraderService;

    public JobContractController(JobContractService jobContractService,
                                 WeaverTraderService weaverTraderService) {
        this.jobContractService = jobContractService;
        this.weaverTraderService = weaverTraderService;
    }

    /* =====================
       SHOW GEN BILL PAGE
       ===================== */
    @GetMapping("/gen-bill")
    public String genBillPage(Model model, Authentication authentication) {
    	System.out.println("Page Visited: " + "Gen_Bill");

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Long userId = userDetails.getId();

        model.addAttribute("weavers",
                weaverTraderService.getWeavers(userId));
        model.addAttribute("traders",
                weaverTraderService.getTraders(userId));
        model.addAttribute("bill", new gen_bill());

        return "gen_bill";
    }

    /* =====================
       SAVE BILL
       ===================== */
    @PostMapping(
            value = "/gen_bill",
            consumes = "application/x-www-form-urlencoded"
    )
    @ResponseBody
    public ResponseEntity<String> saveBill(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate contract_date,
            @RequestParam String weaver_name,
            @RequestParam String trader_name,
            @RequestParam String quality,
            @RequestParam int quantity_meters,
            @RequestParam Double job_rate,
            @RequestParam int payment_days,
            @RequestParam int beams,
            @RequestParam String production_schedule,
            @RequestParam int no_of_machines,
            @RequestParam(required = false) String remark,
            @RequestParam(required = false) String cut_length,
            @RequestParam(required = false) String minimum_delivery,
            @RequestParam(required = false) String[] rolling_folding

    ){

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
            !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return ResponseEntity.status(401).body("UNAUTHORIZED");
        }
        
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

       try {
        User user = new User();
        user.setId(userDetails.getId());
        user.setName(userDetails.getName());

        gen_bill bill = new gen_bill();
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
        String rollingFoldingValue = null;
        if (rolling_folding != null) {
            rollingFoldingValue = String.join(",", rolling_folding);
        }

        bill.setCutLength(cut_length);
        bill.setMinimumDelivery(minimum_delivery);
        bill.setRollingFolding(rollingFoldingValue);


        // ✅ contract_no generated per user (MAX + 1)
        jobContractService.saveJobContract(bill, user);
        
       }catch (Exception e) {
    	   System.out.println(e);
    	  
		// TODO: handle exception
	}

        return ResponseEntity.ok("SUCCESS");
    }
}
