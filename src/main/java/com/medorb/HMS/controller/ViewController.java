package com.medorb.HMS.controller;

import com.medorb.HMS.model.SuperAdmin;
import com.medorb.HMS.service.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ViewController {

    private final SuperAdminService superAdminService;

    @Autowired
    public ViewController(SuperAdminService superAdminService) {
        this.superAdminService = superAdminService;
    }

    @GetMapping("/")
    public String showIndexPage() {
        return "index";  // Resolves to index.html
    }

    @GetMapping("/superAdmin/dashboard")
    public String showSuperAdminDashboard(Model model) {
        SuperAdmin superAdmin = new SuperAdmin();
        superAdmin.setSuperAdminId(1);
        superAdmin.setAdminName("Jane Doe");
        superAdmin.setEmail("jane.doe@example.com");

        model.addAttribute("superAdmin", superAdmin);
        return "super-admin-dashboard"; // Resolves to super-admin-dashboard.html
    }

    // New method to handle Thymeleaf login form submission
    @PostMapping("/login")
    public String loginSuperAdminThymeleaf(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<SuperAdmin> superAdminOptional = superAdminService.getSuperAdminByEmail(email);

        if (superAdminOptional.isPresent() && superAdminOptional.get().getPassword().equals(password)) {
            model.addAttribute("superAdmin", superAdminOptional.get());
            return "redirect:/superAdmin/dashboard"; // Redirect to Thymeleaf view
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "index"; // Stay on login page with error message
        }
    }
}
