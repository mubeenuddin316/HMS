package com.medorb.HMS.controller;

import com.medorb.HMS.model.HospitalAdmin;
import com.medorb.HMS.model.SuperAdmin;
import com.medorb.HMS.service.HospitalAdminService;
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
    private final HospitalAdminService hospitalAdminService;

    @Autowired
    public ViewController(SuperAdminService superAdminService, HospitalAdminService hospitalAdminService) {
        this.superAdminService = superAdminService;
        this.hospitalAdminService = hospitalAdminService;
    }

    @GetMapping("/")
    public String showIndexPage() {
        return "index";  // Resolves to index.html
    }

    // ✅ Fix: Super Admin Login (Uses Real Data from DB)
    @PostMapping("/login")
    public String loginSuperAdmin(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<SuperAdmin> superAdminOptional = superAdminService.getSuperAdminByEmail(email);

        if (superAdminOptional.isPresent() && superAdminOptional.get().getPassword().equals(password)) {
            model.addAttribute("superAdmin", superAdminOptional.get());  // ✅ Pass real Super Admin
            return "super-admin-dashboard"; // ✅ No redirect, direct Thymeleaf rendering
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "index"; // Stay on login page with error message
        }
    }

    // ✅ Fix: Hospital Admin Login (Uses Real Data from DB)
    @PostMapping("/hospitalAdmin/login")
    public String loginHospitalAdmin(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<HospitalAdmin> hospitalAdminOptional = hospitalAdminService.getHospitalAdminByEmail(email);

        if (hospitalAdminOptional.isPresent() && hospitalAdminOptional.get().getPassword().equals(password)) {
            model.addAttribute("hospitalAdmin", hospitalAdminOptional.get());  // ✅ Pass real Hospital Admin
            return "hospital-admin-dashboard"; // ✅ No redirect, direct Thymeleaf rendering
        } else {
            model.addAttribute("hospitalAdminError", "Invalid email or password");
            return "index"; // Stay on login page with error message
        }
    }
}
