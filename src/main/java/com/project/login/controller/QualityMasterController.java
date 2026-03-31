package com.project.login.controller;

import com.project.login.entity.QualityMasterEntity;
import com.project.login.service.QualityMasterService;
import com.project.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/quality")
public class QualityMasterController {

    private final QualityMasterService service;
    private final com.project.login.repository.JobContractRepository jobContractRepository;

    public QualityMasterController(QualityMasterService service, 
                                    com.project.login.repository.JobContractRepository jobContractRepository) {
        this.service = service;
        this.jobContractRepository = jobContractRepository;
    }

    @GetMapping
    public String list(Model model, Authentication authentication) {
    	System.out.println("Page Visited: Quality Master");
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Long userId = userDetails.getId();

        model.addAttribute("list", service.findByUser(userId));

        return "QualityMaster/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
    	System.out.println("Button Clicked: create(quality master)");

        model.addAttribute("quality", new QualityMasterEntity());

        return "QualityMaster/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute QualityMasterEntity quality,
                       Authentication authentication,
                       org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
    	System.out.println("Button Clicked: save(quality master)");

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        quality.setUserId(userId);

        // Check for duplication
        if (quality.getId() == null) {
            java.util.Optional<QualityMasterEntity> duplicate = service.findByNameAndUser(quality.getQualityName(), userId);
            if (duplicate.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Quality Name already exists!");
                return "redirect:/quality/create";
            }
        }

        service.save(quality);
        redirectAttributes.addFlashAttribute("success", "Quality saved successfully!");
        return "redirect:/quality";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id,
                           Model model,
                           Authentication authentication) {
    	System.out.println("Button Clicked: edit(quality master)");

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Long userId = userDetails.getId();

        QualityMasterEntity quality = service.findByIdAndUser(id, userId)
                .orElseThrow(() -> new RuntimeException("Record not found"));

        model.addAttribute("quality", quality);

        return "QualityMaster/form";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
                         Authentication authentication,
                         org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
    	System.out.println("Button Clicked: delete(quality master)");

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        QualityMasterEntity quality = service.findByIdAndUser(id, userId).orElse(null);
        if (quality != null) {
            if (jobContractRepository.existsByQualityAndUserId(quality.getQualityName(), userId)) {
                redirectAttributes.addFlashAttribute("error", "Used in contract form. Cannot delete!");
                return "redirect:/quality";
            }
            service.delete(id, userId);
            redirectAttributes.addFlashAttribute("success", "Quality deleted successfully!");
        }

        return "redirect:/quality";
    }
}
