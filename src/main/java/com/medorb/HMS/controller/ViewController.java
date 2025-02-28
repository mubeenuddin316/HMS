package com.medorb.HMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String showIndexPage() {
        return "index"; // resolves to /index.jsp
    }

    @GetMapping("/superAdmin/dashboard")
    public String showSuperAdminDashboard() {
        return "super-admin-dashboard"; // resolves to /super-admin-dashboard.jsp
    }
}
