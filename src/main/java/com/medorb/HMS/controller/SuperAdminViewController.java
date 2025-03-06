package com.medorb.HMS.controller;

import com.medorb.HMS.model.Hospital;
import com.medorb.HMS.model.SuperAdmin;
import com.medorb.HMS.service.SuperAdminService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SuperAdminViewController {

    private final SuperAdminService superAdminService;

    @Autowired
    public SuperAdminViewController(SuperAdminService superAdminService) {
        this.superAdminService = superAdminService;
    }

    @GetMapping("/superAdmin/dashboard")
    public String showSuperAdminDashboard(HttpServletRequest request, Model model) {
        // 1. Retrieve SuperAdmin from session
        SuperAdmin superAdmin = (SuperAdmin) request.getSession().getAttribute("loggedInSuperAdmin");
        model.addAttribute("superAdmin", superAdmin);

        // 2. Fetch Dashboard Data from DB
        long totalHospitals = superAdminService.getTotalHospitals();
        long totalPatients = superAdminService.getTotalPatients();
        long totalDoctors = superAdminService.getTotalDoctors();
        long occupiedBeds = superAdminService.getOccupiedBeds();
        long totalBeds = superAdminService.getTotalBeds();
        long todaysAppointments = superAdminService.getTodaysAppointmentsCount();

        // 3. Put stats in the model
        model.addAttribute("totalHospitals", totalHospitals);
        model.addAttribute("totalPatients", totalPatients);
        model.addAttribute("totalDoctors", totalDoctors);
        model.addAttribute("occupiedBeds", occupiedBeds);
        model.addAttribute("totalBeds", totalBeds);
        model.addAttribute("freeBeds", totalBeds - occupiedBeds);        
        model.addAttribute("todaysAppointments", todaysAppointments);


        // 4. Return the Thymeleaf template
        return "super-admin-dashboard";
    }
    
    @GetMapping("/superAdmin/hospitals")
    public String showHospitalManagementPage(Model model) {
        List<Hospital> hospitalList = superAdminService.getAllHospitals();
        model.addAttribute("hospitals", hospitalList);
        model.addAttribute("newHospital", new Hospital()); // For a create form
        return "hospital-management"; // A new .html file
    }
    
    @PostMapping("/superAdmin/hospitals")
    public String createHospital(@ModelAttribute Hospital newHospital) {
        superAdminService.createHospitalBySuperAdmin(newHospital);
        // After creation, redirect back to the list
        return "redirect:/superAdmin/hospitals";
    }
    
    @GetMapping("/superAdmin/hospitals/delete/{id}")
    public String deleteHospital(@PathVariable("id") Integer hospitalId) {
        superAdminService.deleteHospitalBySuperAdmin(hospitalId);
        return "redirect:/superAdmin/hospitals";
    }
    
    @GetMapping("/superAdmin/hospitals/edit/{id}")
    public String showEditHospitalForm(@PathVariable("id") Integer hospitalId, Model model) {
        // 1) Fetch existing hospital from DB
        Hospital hospital = superAdminService.getHospitalById(hospitalId)
                                             .orElse(null); // or throw an exception

        // 2) Put it in the model to populate form
        model.addAttribute("hospital", hospital);

        // 3) Return a new "hospital-edit.html" page for editing
        return "hospital-edit"; 
    }

    // For saving the edited hospital
    @PostMapping("/superAdmin/hospitals/edit")
    public String editHospital(@ModelAttribute Hospital hospital) {
        // 1) Call service method to update
        superAdminService.updateHospitalBySuperAdmin(hospital.getHospitalId(), hospital);

        // 2) Redirect back to the hospital list
        return "redirect:/superAdmin/hospitals";
    }

}
