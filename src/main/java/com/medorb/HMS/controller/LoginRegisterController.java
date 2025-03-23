package com.medorb.HMS.controller;

import com.medorb.HMS.model.Patient;
import com.medorb.HMS.service.PatientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class LoginRegisterController {

    private final PatientService patientService;

    @Autowired
    public LoginRegisterController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/patient/auth")
    public String showLoginForm(Model model) {
        model.addAttribute("patient", new Patient()); // for registration form
        return "patient-auth";
    }

    @PostMapping("/patient/auth/login")
    public String loginPatient(@RequestParam String email,
                               @RequestParam String password,
                               HttpServletRequest request,
                               Model model) {
        Optional<Patient> patientOptional = patientService.getPatientByEmail(email);

        if (patientOptional.isPresent() && patientOptional.get().getPassword().equals(password)) {
            request.getSession().setAttribute("loggedInPatient", patientOptional.get());
            return "redirect:/patient/dashboard";
        } else {
            model.addAttribute("loginError", "Invalid email or password");
            return "patient-auth";
        }
    }

    @PostMapping("/patient/auth/register")
    public String registerPatient(@ModelAttribute("patient") Patient patient, Model model) {
        Optional<Patient> existing = patientService.getPatientByEmail(patient.getEmail());
        if (existing.isPresent()) {
            model.addAttribute("registerError", "Email already registered. Try logging in.");
            return "patient-auth";
        }

        patientService.createPatient(patient);
        model.addAttribute("registerSuccess", "Registration successful! You can now login.");
        return "patient-auth";
    }
}
