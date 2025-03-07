package com.medorb.HMS.controller;

import com.medorb.HMS.model.SystemSettings;
import com.medorb.HMS.service.SystemSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SystemSettingsViewController {

    private final SystemSettingsService systemSettingsService;

    @Autowired
    public SystemSettingsViewController(SystemSettingsService systemSettingsService) {
        this.systemSettingsService = systemSettingsService;
    }

    // GET /superAdmin/settings - Show system settings form
    @GetMapping("/superAdmin/settings")
    public String showSystemSettings(Model model) {
        // For minimal approach, we assume we always use ID=1
        // If row doesn't exist, create a new one in memory
        SystemSettings settings = systemSettingsService.getSystemSettingsById(1)
                .orElse(new SystemSettings());
        // If new, ID=1 so next time we save the same row
        if (settings.getSettingsId() == null) {
            settings.setSettingsId(1);
        }

        model.addAttribute("systemSettings", settings);
        return "system-settings"; // We'll create system-settings.html
    }

    // POST /superAdmin/settings - Save system settings
    @PostMapping("/superAdmin/settings")
    public String updateSystemSettings(@ModelAttribute SystemSettings systemSettings) {
        // Force ID=1 in case it wasn't in the form
        systemSettings.setSettingsId(1);
        systemSettingsService.saveSystemSettings(systemSettings);

        // Redirect back to the same page or dashboard
        return "redirect:/superAdmin/settings";
    }
}
