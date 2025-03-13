package com.medorb.HMS.controller;

import com.medorb.HMS.model.Doctor;
import com.medorb.HMS.model.HospitalAdmin;
import com.medorb.HMS.model.Patient;
import com.medorb.HMS.model.SuperAdmin;
import com.medorb.HMS.service.DoctorService;
import com.medorb.HMS.service.HospitalAdminService;
import com.medorb.HMS.service.HospitalService;
import com.medorb.HMS.service.PatientService;
import com.medorb.HMS.service.SuperAdminService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ViewController {

    private final SuperAdminService superAdminService;
    private final HospitalAdminService hospitalAdminService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Autowired
    public ViewController(SuperAdminService superAdminService, HospitalAdminService hospitalAdminService, HospitalService hospitalService, DoctorService doctorService, PatientService patientService) {
        this.superAdminService = superAdminService;
        this.hospitalAdminService = hospitalAdminService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @GetMapping("/")
    public String showIndexPage() {
        return "index";  // Resolves to index.html
    }

    // ✅ Super Admin Login with REDIRECT approach (Option A)
    @PostMapping("/login")
    public String loginSuperAdmin(@RequestParam String email,
                                  @RequestParam String password,
                                  HttpServletRequest request,
                                  Model model) {

        Optional<SuperAdmin> superAdminOptional = superAdminService.getSuperAdminByEmail(email);

        if (superAdminOptional.isPresent() &&
            superAdminOptional.get().getPassword().equals(password)) {

            // Store the SuperAdmin object in HTTP session so it survives the redirect
            request.getSession().setAttribute("loggedInSuperAdmin", superAdminOptional.get());

            // Redirect to GET /superAdmin/dashboard
            return "redirect:/superAdmin/dashboard";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "index"; // Stay on login page with error
        }
    }

    // ✅ Fix: Hospital Admin Login (Uses Real Data from DB)
    @PostMapping("/hospitalAdmin/login")
    public String loginHospitalAdmin(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletRequest request,
            Model model
    ) {
        Optional<HospitalAdmin> hospitalAdminOptional = hospitalAdminService.getHospitalAdminByEmail(email);

        if (hospitalAdminOptional.isPresent() && hospitalAdminOptional.get().getPassword().equals(password)) {
            HospitalAdmin admin = hospitalAdminOptional.get();
            // Store in session
            request.getSession().setAttribute("loggedInHospitalAdmin", admin);

            // Redirect to the new route so we can load real data
            return "redirect:/hospitalAdmin/dashboard";
        } else {
            model.addAttribute("hospitalAdminError", "Invalid email or password");
            return "index"; // Stay on login page with error
        }
    }

    
    // ✅ Doctor Login
    @PostMapping("/doctor/login")
    public String loginDoctor(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<Doctor> doctorOptional = doctorService.getDoctorByEmail(email);

        if (doctorOptional.isPresent() && doctorOptional.get().getPassword().equals(password)) {
            model.addAttribute("doctor", doctorOptional.get());
            return "doctor-dashboard";
        } else {
            model.addAttribute("doctorError", "Invalid email or password");
            return "index";
        }
    }

    // ✅ Patient Login
    @PostMapping("/patient/login")
    public String loginPatient(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<Patient> patientOptional = patientService.getPatientByEmail(email);

        if (patientOptional.isPresent() && patientOptional.get().getPassword().equals(password)) {
            model.addAttribute("patient", patientOptional.get());
            return "patient-dashboard";
        } else {
            model.addAttribute("patientError", "Invalid email or password");
            return "index";
        }
    }
    
    @GetMapping("/patient/register")
    public String showPatientRegistrationForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient-register"; // Resolves to patient-register.html
    }
    
    @PostMapping("/patient/register")
    public String registerPatient(@ModelAttribute Patient patient, Model model) {
        Optional<Patient> existingPatient = patientService.getPatientByEmail(patient.getEmail());

        if (existingPatient.isPresent()) {
            model.addAttribute("errorMessage", "Email already exists. Try logging in.");
            return "patient-register";
        }

        Patient savedPatient = patientService.createPatient(patient);
        model.addAttribute("successMessage", "Registration successful! Please login.");
        
        return "index"; // Redirect to login page after registration
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // Invalidate the session
        request.getSession().invalidate();
        // Redirect to home page or login page
        return "redirect:/";
    }


}
