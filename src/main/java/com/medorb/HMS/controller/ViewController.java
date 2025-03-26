package com.medorb.HMS.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.medorb.HMS.dto.HospitalWithBedsDTO;
import com.medorb.HMS.model.Doctor;
import com.medorb.HMS.model.Hospital;
import com.medorb.HMS.model.HospitalAdmin;
import com.medorb.HMS.model.Patient;
import com.medorb.HMS.model.SuperAdmin;
import com.medorb.HMS.service.BedService;
import com.medorb.HMS.service.DoctorService;
import com.medorb.HMS.service.HospitalAdminService;
import com.medorb.HMS.service.HospitalService;
import com.medorb.HMS.service.PatientService;
import com.medorb.HMS.service.SuperAdminService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ViewController {

    private final SuperAdminService superAdminService;
    private final HospitalAdminService hospitalAdminService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final HospitalService hospitalService;
    private final BedService bedService;

    @Autowired
    public ViewController(SuperAdminService superAdminService, 
    		              HospitalAdminService hospitalAdminService, 
    		              HospitalService hospitalService, 
    		              DoctorService doctorService, 
    		              PatientService patientService,
    		              BedService bedService
    		) {
        this.superAdminService = superAdminService;
        this.hospitalAdminService = hospitalAdminService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.hospitalService = hospitalService;
        this.bedService = bedService;
    }

    @GetMapping("/")
    public String showIndexPage(Model model) {
    	List<Hospital> hospitals = hospitalService.getAllHospitals();
        List<HospitalWithBedsDTO> hospitalDTOs = new ArrayList<>();

        for (Hospital h : hospitals) {
            long totalBeds = bedService.countByHospital(h);
            long availableBeds = bedService.countByHospitalAndIsOccupied(h, false);
            
            HospitalWithBedsDTO dto = new HospitalWithBedsDTO();
            dto.setHospital(h);
            dto.setTotalBeds(totalBeds);
            dto.setAvailableBeds(availableBeds);
            hospitalDTOs.add(dto);
        }

        model.addAttribute("hospitalsWithBeds", hospitalDTOs);
        return "hero";  // Resolves to index.html
    }
    
    @GetMapping("/index")
    public String managementPage() {
        return "index";  // Resolves to index.html
    }


    // ✅ Super Admin Login with REDIRECT approach (Option A)
//    @PostMapping("/login")
//    public String loginSuperAdmin(@RequestParam String email,
//                                  @RequestParam String password,
//                                  HttpServletRequest request,
//                                  Model model) {
//
//        Optional<SuperAdmin> superAdminOptional = superAdminService.getSuperAdminByEmail(email);
//
//        if (superAdminOptional.isPresent() &&
//            superAdminOptional.get().getPassword().equals(password)) {
//
//            // Store the SuperAdmin object in HTTP session so it survives the redirect
//            request.getSession().setAttribute("loggedInSuperAdmin", superAdminOptional.get());
//
//            // Redirect to GET /superAdmin/dashboard
//            return "redirect:/superAdmin/dashboard";
//        } else {
//            model.addAttribute("error", "Invalid email or password");
//            return "index"; // Stay on login page with error
//        }
//    }
    @PostMapping("/loginSuperAdmin")
    public String loginSuperAdmin(@RequestParam String email,
                                  @RequestParam String password,
                                  HttpServletRequest request,
                                  Model model) {
        Optional<SuperAdmin> superAdminOpt = superAdminService.getSuperAdminByEmail(email);
        if (superAdminOpt.isPresent() && superAdminOpt.get().getPassword().equals(password)) {
            // 1) Put the superAdmin in session
            request.getSession().setAttribute("loggedInSuperAdmin", superAdminOpt.get());
            // 2) Redirect to GET /superAdmin/dashboard
            return "redirect:/superAdmin/dashboard";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "index"; // or some login page
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
    public String loginDoctor(@RequestParam String email,
                              @RequestParam String password,
                              HttpServletRequest request,
                              Model model) {
        Optional<Doctor> doctorOptional = doctorService.getDoctorByEmail(email);

        if (doctorOptional.isPresent() && doctorOptional.get().getPassword().equals(password)) {
            Doctor doctor = doctorOptional.get();
            // 1) Put the doctor in session
            request.getSession().setAttribute("loggedInDoctor", doctor);

            // 2) Redirect to /doctor/dashboard so that code which fetches appointments is called
            return "redirect:/doctor/dashboard";

        } else {
            model.addAttribute("doctorError", "Invalid email or password");
            return "index"; // back to login page
        }
    }

    @PostMapping("/patient/login")
    public String loginPatient(@RequestParam String email,
                               @RequestParam String password,
                               HttpServletRequest request,
                               Model model) {
        Optional<Patient> patientOptional = patientService.getPatientByEmail(email);

        if (patientOptional.isPresent() && patientOptional.get().getPassword().equals(password)) {
            // 1) Store in session
            request.getSession().setAttribute("loggedInPatient", patientOptional.get());
            // 2) Redirect to patient dashboard
            return "redirect:/patient/dashboard";
        } else {
            model.addAttribute("patientError", "Invalid email or password");
            return "redirect:/hero";
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
        
        return "redirect:/hero"; // Redirect to login page after registration
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // Invalidate the session
        request.getSession().invalidate();
        // Redirect to home page or login page
        return "redirect:/index";
    }
    
    @GetMapping("/logoutp")
    public String logoutp(HttpServletRequest request) {
        // Invalidate the session
        request.getSession().invalidate();
        // Redirect to home page or login page
        return "redirect:/hero";
    }


}
