package com.medorb.HMS.controller;

import com.medorb.HMS.model.Appointment;
import com.medorb.HMS.model.Bed;
import com.medorb.HMS.model.Doctor;
import com.medorb.HMS.model.Hospital;
import com.medorb.HMS.model.HospitalAdmin;
import com.medorb.HMS.model.OpdQueue;
import com.medorb.HMS.model.SuperAdmin;
import com.medorb.HMS.service.AppointmentService;
import com.medorb.HMS.service.BedService;
import com.medorb.HMS.service.DoctorService;
import com.medorb.HMS.service.HospitalAdminService;
import com.medorb.HMS.service.OpdQueueService;
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
    private final HospitalAdminService hospitalAdminService;
    private final OpdQueueService opdQueueService;
    private final AppointmentService appointmentService;
    private final BedService bedService;

    @Autowired
    public SuperAdminViewController(SuperAdminService superAdminService,
    		                        DoctorService doctorService, 
    		                        HospitalAdminService hospitalAdminService, 
    		                        OpdQueueService opdQueueService, 
    		                        AppointmentService appointmentService, 
    		                        BedService bedService) {
        
    	
    	this.superAdminService = superAdminService;
        this.doctorService = doctorService;
        this.hospitalAdminService = hospitalAdminService;
        this.opdQueueService = opdQueueService;
        this.appointmentService = appointmentService;
        this.bedService = bedService;
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
    
    @GetMapping("/superAdmin/hospitalAdmins")
    public String showHospitalAdminManagementPage(Model model) {
        // 1) Fetch all hospital admins
        List<HospitalAdmin> adminList = hospitalAdminService.getAllHospitalAdmins();

        // 2) Add them to the model
        model.addAttribute("hospitalAdmins", adminList);

        // 3) Provide a blank HospitalAdmin object for the create form
        model.addAttribute("newHospitalAdmin", new HospitalAdmin());

        // Return the Thymeleaf page
        return "hospital-admin-management"; // We'll create hospital-admin-management.html
    }

    // CREATE a new HospitalAdmin
    @PostMapping("/superAdmin/hospitalAdmins")
    public String createHospitalAdmin(@ModelAttribute HospitalAdmin newAdmin) {
        // We assume newAdmin has a valid hospital object or ID
        hospitalAdminService.createHospitalAdmin(newAdmin);
        return "redirect:/superAdmin/hospitalAdmins";
    }

    // DELETE a HospitalAdmin
    @GetMapping("/superAdmin/hospitalAdmins/delete/{id}")
    public String deleteHospitalAdmin(@PathVariable("id") Integer adminId) {
        hospitalAdminService.deleteHospitalAdmin(adminId);
        return "redirect:/superAdmin/hospitalAdmins";
    }

    // EDIT HospitalAdmin (show form)
    @GetMapping("/superAdmin/hospitalAdmins/edit/{id}")
    public String showEditHospitalAdminForm(@PathVariable("id") Integer adminId, Model model) {
        // Fetch existing admin
        HospitalAdmin admin = hospitalAdminService.getHospitalAdminById(adminId).orElse(null);
        model.addAttribute("hospitalAdmin", admin);

        // Return a new "hospital-admin-edit.html" page
        return "hospital-admin-edit";
    }

    // UPDATE HospitalAdmin
    @PostMapping("/superAdmin/hospitalAdmins/edit")
    public String updateHospitalAdmin(@ModelAttribute HospitalAdmin admin) {
        hospitalAdminService.updateHospitalAdmin(admin.getHospitalAdminId(), admin);
        return "redirect:/superAdmin/hospitalAdmins";
    }
    
    @GetMapping("/superAdmin/opdQueues")
    public String showOpdQueueManagementPage(Model model) {
        List<OpdQueue> queueList = opdQueueService.getAllOpdQueueEntries();
        model.addAttribute("opdQueueList", queueList);

        model.addAttribute("newOpdQueue", new OpdQueue());

        // 🆕 fetch existing appointments
        List<Appointment> appointments = appointmentService.getAllAppointments();
        model.addAttribute("appointments", appointments);

        return "opd-queue-management";
    }


    // CREATE a new OPD queue entry
    @PostMapping("/superAdmin/opdQueues")
    public String createOpdQueue(@ModelAttribute OpdQueue newOpdQueue) {
        opdQueueService.createOpdQueueEntry(newOpdQueue);
        return "redirect:/superAdmin/opdQueues";
    }

    // DELETE an OPD queue entry
    @GetMapping("/superAdmin/opdQueues/delete/{id}")
    public String deleteOpdQueue(@PathVariable("id") Integer queueId) {
        opdQueueService.deleteOpdQueueEntry(queueId);
        return "redirect:/superAdmin/opdQueues";
    }

    // (Optional) EDIT or update OPD queue entry
    @GetMapping("/superAdmin/opdQueues/edit/{id}")
    public String showEditOpdQueueForm(@PathVariable("id") Integer queueId, Model model) {
        OpdQueue queueEntry = opdQueueService.getOpdQueueEntryById(queueId).orElse(null);
        model.addAttribute("opdQueue", queueEntry);
        return "opd-queue-edit";
    }

    @PostMapping("/superAdmin/opdQueues/edit")
    public String updateOpdQueue(@ModelAttribute OpdQueue opdQueue) {
        opdQueueService.updateOpdQueueEntry(opdQueue.getOpdQueueId(), opdQueue);
        return "redirect:/superAdmin/opdQueues";
    }
    
    @GetMapping("/superAdmin/beds")
    public String showBedManagementPage(Model model) {
        // 1) Fetch all beds
        List<Bed> beds = bedService.getAllBeds();

        // 2) Add them to the model
        model.addAttribute("beds", beds);

        // 3) Provide a blank Bed object for the create form
        model.addAttribute("newBed", new Bed());

        return "bed-management"; // We'll create bed-management.html
    }

    @PostMapping("/superAdmin/beds")
    public String createBed(@ModelAttribute Bed newBed) {
        bedService.createBed(newBed);
        // No /HMS prefix needed here, as Spring automatically prepends the context path
        return "redirect:/superAdmin/beds";
    }

    @GetMapping("/superAdmin/beds/delete/{id}")
    public String deleteBed(@PathVariable("id") Integer bedId) {
        bedService.deleteBed(bedId);
        return "redirect:/superAdmin/beds";
    }

    @GetMapping("/superAdmin/beds/edit/{id}")
    public String showEditBedForm(@PathVariable("id") Integer bedId, Model model) {
        Bed existingBed = bedService.getBedById(bedId).orElse(null);
        model.addAttribute("bed", existingBed);
        return "bed-edit"; // We'll create bed-edit.html
    }

    @PostMapping("/superAdmin/beds/edit")
    public String updateBed(@ModelAttribute Bed bed) {
        bedService.updateBed(bed.getBedId(), bed);
        return "redirect:/superAdmin/beds";
    }
    
    @GetMapping("/superAdmin/profile")
    public String showSuperAdminProfile(HttpServletRequest request, Model model) {
        // Retrieve the loggedInSuperAdmin from session (assuming you stored it on login)
        SuperAdmin sessionAdmin = (SuperAdmin) request.getSession().getAttribute("loggedInSuperAdmin");
        if (sessionAdmin == null) {
            // Not logged in or session expired
            return "redirect:/"; // or some error page
        }

        // Fetch the latest data from DB (in case it changed)
        SuperAdmin superAdminFromDb = superAdminService.getSuperAdminById(sessionAdmin.getSuperAdminId())
                                                       .orElse(sessionAdmin);

        // Put it in the model for display
        model.addAttribute("superAdmin", superAdminFromDb);
        return "super-admin-profile"; // We'll create super-admin-profile.html
    }

    // 2) Update the Profile
    @PostMapping("/superAdmin/profile")
    public String updateSuperAdminProfile(@ModelAttribute SuperAdmin updatedAdmin,
                                          HttpServletRequest request) {
        // Validate session
        SuperAdmin sessionAdmin = (SuperAdmin) request.getSession().getAttribute("loggedInSuperAdmin");
        if (sessionAdmin == null) {
            return "redirect:/"; // or error
        }

        // Only allow updates to the same ID
        Integer superAdminId = sessionAdmin.getSuperAdminId();

        // Update in DB
        superAdminService.updateSuperAdmin(superAdminId, updatedAdmin);

        // Update session with new data if needed
        request.getSession().setAttribute("loggedInSuperAdmin", updatedAdmin);

        // Redirect back to the profile page or dashboard
        return "redirect:/superAdmin/dashboard";
    }
}





