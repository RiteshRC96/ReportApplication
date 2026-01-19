package com.project.login.controller;
import com.project.login.entity.WeaverTrader;
import com.project.login.service.WeaverTraderService;
import org.springframework.security.core.Authentication;
import com.project.security.CustomUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/weaver-trader")
public class WeaverTraderController {
	

    private final WeaverTraderService service;

    public WeaverTraderController(WeaverTraderService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model, Authentication authentication) {
    	System.out.println("Page Visited: " + "weaver-trader");

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Long userId = userDetails.getId();

        model.addAttribute("list", service.findByUser(userId));
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
                       Authentication authentication) {
    	System.out.println("Button clicked: " + "Save");
    	
    	CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();
    	
        wt.setUserId(userDetails.getId());
        service.save(wt);

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
                         Authentication authentication) {

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        Long userId = userDetails.getId();

        // 🔴 THIS LINE WAS MISSING
        service.delete(id, userId);

        return "redirect:/weaver-trader";
    }


}
