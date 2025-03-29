package com.medorb.HMS.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.medorb.HMS.model.Appointment;
import com.medorb.HMS.model.Bed;
import com.medorb.HMS.model.Doctor;
import com.medorb.HMS.model.Hospital;
import com.medorb.HMS.model.HospitalAdmin;
import com.medorb.HMS.model.OpdQueue;
import com.medorb.HMS.model.Patient;
import com.medorb.HMS.model.SuperAdmin;
import com.medorb.HMS.service.AppointmentService;
import com.medorb.HMS.service.BedService;
import com.medorb.HMS.service.DoctorService;
import com.medorb.HMS.service.HospitalAdminService;
import com.medorb.HMS.service.HospitalService;
import com.medorb.HMS.service.OpdQueueService;
import com.medorb.HMS.service.PatientService;
import com.medorb.HMS.service.SuperAdminService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SuperAdminViewController {

    private final SuperAdminService superAdminService;
    private final DoctorService doctorService;
    private final HospitalAdminService hospitalAdminService;
    private final OpdQueueService opdQueueService;
    private final AppointmentService appointmentService;
    private final BedService bedService;
    private final PatientService patientService;
    private final HospitalService hospitalService;

    @Autowired
    public SuperAdminViewController(SuperAdminService superAdminService,
    		                        DoctorService doctorService, 
    		                        HospitalAdminService hospitalAdminService, 
    		                        OpdQueueService opdQueueService, 
    		                        AppointmentService appointmentService, 
    		                        BedService bedService,
    		                        PatientService patientService,
    		                        HospitalService hospitalService) {
        
    	
    	this.superAdminService = superAdminService;
        this.doctorService = doctorService;
        this.hospitalAdminService = hospitalAdminService;
        this.opdQueueService = opdQueueService;
        this.appointmentService = appointmentService;
        this.bedService = bedService;
        this.patientService = patientService;
        this.hospitalService = hospitalService;
    }

    @GetMapping("/superAdmin/dashboard")
    public String showSuperAdminDashboard(HttpServletRequest request, Model model) {
        // 1) Grab from session
        SuperAdmin superAdmin = (SuperAdmin) request.getSession().getAttribute("loggedInSuperAdmin");

        // 2) If null => user is not logged in or session expired => redirect
        if (superAdmin == null) {
            return "redirect:/index"; 
            // or "redirect:/index" or "redirect:/?error=SessionExpired"
        }

        // 3) Put in model
        model.addAttribute("superAdmin", superAdmin);

        // 4) Then fetch stats if needed
        long totalHospitals = superAdminService.getTotalHospitals();
        long totalPatients = superAdminService.getTotalPatients();
        long totalDoctors = superAdminService.getTotalDoctors();
        long occupiedBeds = superAdminService.getOccupiedBeds();
        long totalBeds = superAdminService.getTotalBeds();
        long todaysAppointments = superAdminService.getTodaysAppointmentsCount();

        model.addAttribute("totalHospitals", totalHospitals);
        model.addAttribute("totalPatients", totalPatients);
        model.addAttribute("totalDoctors", totalDoctors);
        model.addAttribute("occupiedBeds", occupiedBeds);
        model.addAttribute("totalBeds", totalBeds);
        model.addAttribute("freeBeds", totalBeds - occupiedBeds);
        model.addAttribute("todaysAppointments", todaysAppointments);

        // 5) Return the template
        return "super-admin-dashboard";
    }
    
    // ==========================
    // Hospital Management
    // ==========================
    
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
    
 // ==========================
 // Doctors Management
 // ==========================

    @GetMapping("/superAdmin/doctors")
    public String showDoctorManagementPage(
        @RequestParam(required = false) Integer hospitalId,
        @RequestParam(required = false) String name,
        Model model
    ) {
        // 1) Fetch all hospitals for the dropdown
        List<Hospital> allHospitals = hospitalService.getAllHospitals();
        model.addAttribute("hospitalList", allHospitals);

        // 2) Depending on the query params, filter doctors
        List<Doctor> doctorList;
        if (hospitalId != null && hospitalId > 0 && name != null && !name.isBlank()) {
            // filter by BOTH hospital & name
            doctorList = doctorService.findByHospitalIdAndName(hospitalId, name);
        } 
        else if (hospitalId != null && hospitalId > 0) {
            // filter by hospital only
            doctorList = doctorService.findByHospitalId(hospitalId);
        } 
        else if (name != null && !name.isBlank()) {
            // filter by name only
            doctorList = doctorService.findByName(name);
        } 
        else {
            // no filter => get all
            doctorList = doctorService.getAllDoctors();
        }

        model.addAttribute("doctors", doctorList);

        // Provide a blank Doctor object for the create form
        model.addAttribute("newDoctor", new Doctor());

        // Keep track of the currently selected filters so we can show them in the UI
        model.addAttribute("selectedHospitalId", hospitalId);
        model.addAttribute("searchName", name);

        return "doctor-management";
    }

 @PostMapping("/superAdmin/doctors")
 public String createDoctor(@ModelAttribute Doctor newDoctor, @RequestParam("hospitalId") Integer hospitalId) {
     // Fetch and set the selected hospital
     Optional<Hospital> optHosp = hospitalService.getHospitalById(hospitalId);
     optHosp.ifPresent(newDoctor::setHospital);

     doctorService.createDoctor(newDoctor);
     return "redirect:/superAdmin/doctors";
 }

 // DELETE Doctor
 @GetMapping("/superAdmin/doctors/delete/{id}")
 public String deleteDoctor(@PathVariable("id") Integer doctorId) {
     doctorService.deleteDoctor(doctorId);
     return "redirect:/superAdmin/doctors";
 }

 // EDIT Doctor Form
 @GetMapping("/superAdmin/doctors/edit/{id}")
 public String showEditDoctorForm(@PathVariable("id") Integer doctorId, Model model) {
     Doctor doctor = doctorService.getDoctorById(doctorId).orElse(null);
     List<Hospital> hospitalList = hospitalService.getAllHospitals(); // Fetch hospitals for dropdown
     
     model.addAttribute("doctor", doctor);
     model.addAttribute("hospitalList", hospitalList);
     return "doctor-edit";
 }

 // UPDATE Doctor
 @PostMapping("/superAdmin/doctors/edit")
 public String updateDoctor(@ModelAttribute Doctor doctor, @RequestParam("hospitalId") Integer hospitalId) {
     // Fetch and set hospital
     Optional<Hospital> optHosp = hospitalService.getHospitalById(hospitalId);
     optHosp.ifPresent(doctor::setHospital);

     doctorService.updateDoctor(doctor.getDoctorId(), doctor);
     return "redirect:/superAdmin/doctors";
 }

//==========================
//HospitalAdmin Management
//==========================

//FIXED: Show HospitalAdmin Management Page (GET)
@GetMapping("/superAdmin/hospitalAdmins")
public String showHospitalAdminManagementPage(Model model) {
  // Fetch all hospital admins
  List<HospitalAdmin> adminList = hospitalAdminService.getAllHospitalAdmins();
  
  // Fetch all hospitals (for dropdown selection)
  List<Hospital> hospitalList = hospitalService.getAllHospitals();

  // Add them to the model
  model.addAttribute("hospitalAdmins", adminList);
  model.addAttribute("hospitalList", hospitalList);
  
  // Provide an empty HospitalAdmin object for form submission
  model.addAttribute("newHospitalAdmin", new HospitalAdmin());

  return "hospital-admin-management"; // Ensure this template exists
}

//CREATE a new HospitalAdmin
@PostMapping("/superAdmin/hospitalAdmins")
public String createHospitalAdmin(@ModelAttribute HospitalAdmin newAdmin) {
  hospitalAdminService.createHospitalAdmin(newAdmin);
  return "redirect:/superAdmin/hospitalAdmins";
}

//DELETE a HospitalAdmin
@GetMapping("/superAdmin/hospitalAdmins/delete/{id}")
public String deleteHospitalAdmin(@PathVariable("id") Integer adminId) {
  hospitalAdminService.deleteHospitalAdmin(adminId);
  return "redirect:/superAdmin/hospitalAdmins";
}

//EDIT HospitalAdmin (Show Form)
@GetMapping("/superAdmin/hospitalAdmins/edit/{id}")
public String showEditHospitalAdminForm(@PathVariable("id") Integer adminId, Model model) {
  HospitalAdmin admin = hospitalAdminService.getHospitalAdminById(adminId).orElse(null);
  model.addAttribute("hospitalAdmin", admin);
  
  // Fetch hospitals for selection
  List<Hospital> hospitalList = hospitalService.getAllHospitals();
  model.addAttribute("hospitalList", hospitalList);

  return "hospital-admin-edit";
}

//UPDATE HospitalAdmin
@PostMapping("/superAdmin/hospitalAdmins/edit")
public String updateHospitalAdmin(@ModelAttribute HospitalAdmin admin) {
  hospitalAdminService.updateHospitalAdmin(admin.getHospitalAdminId(), admin);
  return "redirect:/superAdmin/hospitalAdmins";
}
    
//=========================
// GET /superAdmin/opdQueues (with optional filters)
// =========================
@GetMapping("/superAdmin/opdQueues")
public String showOpdQueueManagementPage(
    @RequestParam(required=false) String patientName,
    @RequestParam(required=false) Integer doctorId,
    @RequestParam(required=false) Integer hospitalId,
    Model model
) {
    // Filter logic
    List<OpdQueue> queueList = opdQueueService.filterOpdQueues(patientName, doctorId, hospitalId);
    model.addAttribute("opdQueueList", queueList);

    // For the filter form
    model.addAttribute("doctorList", doctorService.getAllDoctors());
    model.addAttribute("hospitalList", hospitalService.getAllHospitals());

    // For the create form
    model.addAttribute("allPatients", patientService.getAllPatients());
    model.addAttribute("appointments", appointmentService.getAllAppointments());
    model.addAttribute("newOpdQueue", new OpdQueue());

    return "opd-queue-management";
}

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

        // 1) Also fetch and add all the dropdown data:
        model.addAttribute("patientList", patientService.getAllPatients());
        model.addAttribute("doctorList", doctorService.getAllDoctors());
        model.addAttribute("hospitalList", hospitalService.getAllHospitals());

        // If you also want an Appointment dropdown:
        model.addAttribute("appointments", appointmentService.getAllAppointments());

        return "opd-queue-edit";  // thymeleaf template
    }

    @PostMapping("/superAdmin/opdQueues/edit")
    public String updateOpdQueue(@ModelAttribute OpdQueue formQueue) {
        // 1) fetch existing queue from DB
        Optional<OpdQueue> optionalQ = opdQueueService.getOpdQueueEntryById(formQueue.getOpdQueueId());
        if (optionalQ.isEmpty()) {
            // no such queue entry => redirect or show error
            return "redirect:/superAdmin/opdQueues";
        }
        OpdQueue dbQueue = optionalQ.get();
        
        // 2) Now we carefully update its fields
        //    We do NOT blindly set dbQueue.setPatient(formQueue.getPatient()),
        //    because that might be a transient object.
        //    Instead, if user selected a patient ID, we re-fetch that patient from DB:
        
        if (formQueue.getPatient() != null && formQueue.getPatient().getPatientId() != null) {
            Integer patId = formQueue.getPatient().getPatientId();
            Patient existingPatient = patientService.getPatientById(patId).orElse(null);
            dbQueue.setPatient(existingPatient);  // This ensures the patient is a managed entity
        } else {
            dbQueue.setPatient(null);  // or keep existing if you want
        }
        
        // 3) For the "walk-in" name:
        dbQueue.setPatientName(formQueue.getPatientName());
        
        // 4) Re-fetch the Doctor from DB
        if (formQueue.getDoctor() != null && formQueue.getDoctor().getDoctorId() != null) {
            Integer docId = formQueue.getDoctor().getDoctorId();
            Doctor existingDoctor = doctorService.getDoctorById(docId).orElse(null);
            dbQueue.setDoctor(existingDoctor);
        }
        
        // 5) Re-fetch the Hospital
        if (formQueue.getHospital() != null && formQueue.getHospital().getHospitalId() != null) {
            Integer hospId = formQueue.getHospital().getHospitalId();
            Hospital existingHospital = hospitalService.getHospitalById(hospId).orElse(null);
            dbQueue.setHospital(existingHospital);
        }
        
        // 6) If there's an appointment ID
        if (formQueue.getAppointment() != null && formQueue.getAppointment().getAppointmentId() != null) {
            Integer appId = formQueue.getAppointment().getAppointmentId();
            Appointment existingApp = appointmentService.getAppointmentById(appId).orElse(null);
            dbQueue.setAppointment(existingApp);
        } else {
            dbQueue.setAppointment(null);
        }
        
        // 7) Update other scalar fields
        dbQueue.setRegistrationTime(formQueue.getRegistrationTime());
        dbQueue.setQueueStatus(formQueue.getQueueStatus());
        dbQueue.setTokenNumber(formQueue.getTokenNumber());
        
        // 8) Now save the updated entity
        opdQueueService.updateOpdQueueEntry(dbQueue.getOpdQueueId(), dbQueue);

        return "redirect:/superAdmin/opdQueues";
    }

    
    // ==========================
    // Bed Management
    // ==========================
    
    @GetMapping("/superAdmin/beds")
    public String showBedManagementPage(Model model) {
        // 1) Fetch all beds
        List<Bed> beds = bedService.getAllBeds();

        // 2) Add them to the model
        model.addAttribute("beds", beds);
        model.addAttribute("hospitalList", hospitalService.getAllHospitals());

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
    	model.addAttribute("hospitalList", hospitalService.getAllHospitals());
        Bed existingBed = bedService.getBedById(bedId).orElse(null);
        model.addAttribute("bed", existingBed);
        return "bed-edit"; // We'll create bed-edit.html
    }

    @PostMapping("/superAdmin/beds/edit")
    public String updateBed(@ModelAttribute Bed bed) {
        bedService.updateBed(bed.getBedId(), bed);
        return "redirect:/superAdmin/beds";
    }
    
    // ==========================
    // SuperAdmin Profile Update
    // ==========================
    
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
    
    // ==========================
    // SuperAdmin Management
    // ==========================
    
    @GetMapping("/superAdmin/superAdmins")
    public String showSuperAdminManagementPage(Model model) {
        // 1) Fetch all super admins
        List<SuperAdmin> superAdmins = superAdminService.getAllSuperAdmins();
        // 2) Add them to the model
        model.addAttribute("superAdmins", superAdmins);
        // 3) Provide a blank SuperAdmin object for the create form
        model.addAttribute("newSuperAdmin", new SuperAdmin());
        // Return the Thymeleaf page
        return "super-admin-management";
    }

    // CREATE a new SuperAdmin
    @PostMapping("/superAdmin/superAdmins")
    public String createSuperAdmin(@ModelAttribute SuperAdmin newAdmin) {
        superAdminService.createSuperAdmin(newAdmin);
        return "redirect:/superAdmin/superAdmins";
    }

    // DELETE a SuperAdmin
    @GetMapping("/superAdmin/superAdmins/delete/{id}")
    public String deleteSuperAdmin(@PathVariable("id") Integer superAdminId) {
        superAdminService.deleteSuperAdmin(superAdminId);
        return "redirect:/superAdmin/superAdmins";
    }

    // EDIT SuperAdmin (show form)
    @GetMapping("/superAdmin/superAdmins/edit/{id}")
    public String showEditSuperAdminForm(@PathVariable("id") Integer superAdminId, Model model) {
        SuperAdmin admin = superAdminService.getSuperAdminById(superAdminId).orElse(null);
        model.addAttribute("superAdmin", admin);
        // Return a new "super-admin-edit.html" page
        return "super-admin-edit";
    }

    // UPDATE SuperAdmin
    @PostMapping("/superAdmin/superAdmins/edit")
    public String updateSuperAdmin(@ModelAttribute SuperAdmin admin) {
        superAdminService.updateSuperAdmin(admin.getSuperAdminId(), admin);
        return "redirect:/superAdmin/superAdmins";
    }
    
    // ==========================
    // Appointments OVERVIEW
    // ==========================
    // 1) GET /superAdmin/appointments - Show all appointments
    @GetMapping("/superAdmin/appointments")
    public String showAppointmentsOverview(
        @RequestParam(required=false) String patientName,
        @RequestParam(required=false) Integer doctorId,
        @RequestParam(required=false) Integer hospitalId,
        @RequestParam(required=false) String status,
        Model model
    ) {
        // 1) Filter the appointments
        //    Example: a custom service or repository method
        List<Appointment> appointments = appointmentService.filterAppointments(patientName, doctorId, hospitalId, status);

        // 2) Add them to model
        model.addAttribute("appointments", appointments);

        // For filter form
        model.addAttribute("doctorList", doctorService.getAllDoctors());
        model.addAttribute("hospitalList", hospitalService.getAllHospitals());
        // If you want to also show patient dropdown, or you just want to do name-based searching?

        // For the create form
        model.addAttribute("allPatients", patientService.getAllPatients());
        model.addAttribute("newAppointment", new Appointment());

        return "appointments-overview";
    }

    // 2) POST /superAdmin/appointments - Create a new appointment
    @PostMapping("/superAdmin/appointments")
    public String createAppointment(@ModelAttribute Appointment newAppointment) {
        appointmentService.createAppointment(newAppointment);
        return "redirect:/superAdmin/appointments";
    }

    // 3) GET /superAdmin/appointments/edit/{id} - Show edit form
    @GetMapping("/superAdmin/appointments/edit/{id}")
    public String showEditAppointmentForm(@PathVariable("id") Integer id, Model model) {
    	model.addAttribute("allPatients", patientService.getAllPatients());
        model.addAttribute("doctorList", doctorService.getAllDoctors());
        model.addAttribute("hospitalList", hospitalService.getAllHospitals());
        Appointment existing = appointmentService.getAppointmentById(id).orElse(null);
        model.addAttribute("appointment", existing);
        return "appointment-edit"; // We'll create appointment-edit.html
    }

    // 4) POST /superAdmin/appointments/edit - Update an existing appointment
    @PostMapping("/superAdmin/appointments/edit")
    public String updateAppointment(@ModelAttribute Appointment updatedAppointment) {
        if (updatedAppointment.getAppointmentId() != null) {
            appointmentService.updateAppointment(updatedAppointment.getAppointmentId(), updatedAppointment);
        }
        return "redirect:/superAdmin/appointments";
    }

    // 5) GET /superAdmin/appointments/delete/{id} - Delete an appointment
    @GetMapping("/superAdmin/appointments/delete/{id}")
    public String deleteAppointment(@PathVariable("id") Integer id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/superAdmin/appointments";
    }
    
    // ==========================
    // PATIENTS OVERVIEW
    // ==========================

    // 1) GET /superAdmin/patients - Show all patients
    @GetMapping("/superAdmin/patients")
    public String showPatientsManagementPage(
            @RequestParam(required = false) String name,
            Model model
    ) {
        // 1) If you have a PatientService, use it. Or call repository directly.
        List<Patient> patientList;

        if (name != null && !name.isBlank()) {
            // Filter by name
            patientList = patientService.findByName(name);
        } else {
            // No filter => get all
            patientList = patientService.getAllPatients();
        }

        // 2) Put results in model
        model.addAttribute("patients", patientList);

        // 3) Keep the 'name' so we can show it in the search input
        model.addAttribute("searchName", name);

        // 4) Provide a blank Patient object for the create form
        model.addAttribute("newPatient", new Patient());

        return "patients-overview";
    }

    // 3) GET /superAdmin/patients/edit/{id} - Show edit form
    @GetMapping("/superAdmin/patients/edit/{id}")
    public String showEditPatientForm(@PathVariable("id") Integer id, Model model) {
        Patient existing = patientService.getPatientById(id).orElse(null);
        model.addAttribute("patient", existing);
        return "patient-edit"; // We'll create patient-edit.html
    }

    // 4) POST /superAdmin/patients/edit - Update an existing patient
    @PostMapping("/superAdmin/patients/edit")
    public String updatePatient(@ModelAttribute Patient updatedPatient) {
        if (updatedPatient.getPatientId() != null) {
            patientService.updatePatient(updatedPatient.getPatientId(), updatedPatient);
        }
        return "redirect:/superAdmin/patients";
    }

    // 5) GET /superAdmin/patients/delete/{id} - Delete a patient
    @GetMapping("/superAdmin/patients/delete/{id}")
    public String deletePatient(@PathVariable("id") Integer id) {
        patientService.deletePatient(id);
        return "redirect:/superAdmin/patients";
    }
    
    // This mapping assumes that the user is already logged in as a super admin.
    @GetMapping("/superAdmin/analytics")
    public String showAnalyticsPage(HttpServletRequest request, Model model) {
        // Retrieve super admin from session (or add additional analytics data if needed)
        SuperAdmin superAdmin = (SuperAdmin) request.getSession().getAttribute("loggedInSuperAdmin");
        if (superAdmin == null) {
            return "redirect:/index";
        }
        model.addAttribute("superAdmin", superAdmin);
        return "analytics";  // This will resolve to analytics.html
    }
    
}





