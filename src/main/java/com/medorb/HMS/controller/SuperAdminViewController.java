package com.medorb.HMS.controller;

import com.medorb.HMS.model.Doctor;
import com.medorb.HMS.model.Hospital;
import com.medorb.HMS.model.SuperAdmin;
import com.medorb.HMS.service.DoctorService;
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
    private final DoctorService doctorService;

    @Autowired
    public SuperAdminViewController(SuperAdminService superAdminService, DoctorService doctorService) {
        this.superAdminService = superAdminService;
        this.doctorService = doctorService;
        
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
    
    @GetMapping("/superAdmin/doctors")
    public String showDoctorManagementPage(Model model) {
        // 1) Fetch all doctors
        List<Doctor> doctorList = doctorService.getAllDoctors();

        // 2) Add them to the model
        model.addAttribute("doctors", doctorList);

        // 3) Provide a blank Doctor object for the create form
        model.addAttribute("newDoctor", new Doctor());

        // 4) Return the Thymeleaf page
        return "doctor-management";
    }
    
    @PostMapping("/superAdmin/doctors")
    public String createDoctor(@ModelAttribute Doctor newDoctor) {
        // If user typed hospitalId manually, you must fetch the actual Hospital entity:
        // Hospital h = hospitalService.getHospitalById(newDoctor.getHospital().getHospitalId()).orElse(null);
        // newDoctor.setHospital(h);

        doctorService.createDoctor(newDoctor);
        return "redirect:/superAdmin/doctors"; 
    }

    
    @GetMapping("/superAdmin/doctors/delete/{id}")
    public String deleteDoctor(@PathVariable("id") Integer doctorId) {
        doctorService.deleteDoctor(doctorId);
        return "redirect:/superAdmin/doctors";
    }
    
    @GetMapping("/superAdmin/doctors/edit/{id}")
    public String showEditDoctorForm(@PathVariable("id") Integer doctorId, Model model) {
        Doctor doctor = doctorService.getDoctorById(doctorId).orElse(null);
        model.addAttribute("doctor", doctor);
        return "doctor-edit"; 
    }


    
    @PostMapping("/superAdmin/doctors/edit")
    public String updateDoctor(@ModelAttribute Doctor doctor) {
        doctorService.updateDoctor(doctor.getDoctorId(), doctor);
        return "redirect:/superAdmin/doctors";
    }




}
