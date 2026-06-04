package com.project.login.controller;
import com.project.login.entity.WeaverTrader;
import com.project.login.service.WeaverTraderService;
import com.project.security.CustomUserDetails;

import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/weaver-trader")
public class WeaverTraderController {
	

    private final WeaverTraderService service;
    private final com.project.login.repository.JobContractRepository jobContractRepository;

    public WeaverTraderController(WeaverTraderService service, 
                                  com.project.login.repository.JobContractRepository jobContractRepository) {
        this.service = service;
        this.jobContractRepository = jobContractRepository;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       Model model, Authentication authentication) {
    	System.out.println("Page Visited: " + "weaver-trader");

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Long userId = userDetails.getId();

        Page<WeaverTrader> listPage = service.findByUserPaginated(userId, PageRequest.of(page, size));
        
        model.addAttribute("list", listPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", listPage.getTotalPages());
        model.addAttribute("pageSize", size);
        
        return "weaver_trader/list";
    }


    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("wt", new WeaverTrader());
        System.out.println("Page Visited: " + "create weaver/trader");
        return "weaver_trader/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute WeaverTrader wt,
                       Authentication authentication,
                       org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
    	System.out.println("Button clicked: " + "Save");
    	
    	CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();
    	Long userId = userDetails.getId();
        wt.setUserId(userId);
        
        // CHECK DUPLICATION (Name, Mob No, Type)
        if (wt.getId() == null) { // only for new records
            java.util.Optional<WeaverTrader> duplicate = service.findByNameAndPhnoAndTypeAndUser(wt.getName(), wt.getphno(), wt.getType(), userId);
            if (duplicate.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Duplicate Record: Name, Mobile Number and Type already exists!");
                return "redirect:/weaver-trader/create";
            }
        }

        // Capitalize Name
        if (wt.getName() != null) {
            wt.setName(capitalizeInitialLetters(wt.getName()));
        }

        service.save(wt);
        redirectAttributes.addFlashAttribute("success", "Record saved successfully!");
        return "redirect:/weaver-trader";
    }


    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id,
                           Model model,
                           Authentication authentication) {
    	System.out.println("Button clicked: " + "Edit");
    	System.out.println("Page Visited: " + "update");

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Long userId = userDetails.getId();

        WeaverTrader wt = service.findByIdAndUser(id, userId)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        model.addAttribute("wt", wt);

        return "weaver_trader/form";
    }


    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         Authentication authentication,
                         org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        WeaverTrader wt = service.findByIdAndUser(id, userId)
                .orElse(null);
        
        if (wt != null) {
            boolean isUsed = false;
            if ("WEAVER".equals(wt.getType())) {
                isUsed = jobContractRepository.existsByWeaverNameAndUserId(wt.getName(), userId);
            } else {
                isUsed = jobContractRepository.existsByTraderNameAndUserId(wt.getName(), userId);
            }
            
            if (isUsed) {
                redirectAttributes.addFlashAttribute("error", "Used in contract form. Cannot delete!");
                return "redirect:/weaver-trader";
            }
            
            service.delete(id, userId);
            redirectAttributes.addFlashAttribute("success", "Deleted successfully!");
        }

        return "redirect:/weaver-trader";
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
