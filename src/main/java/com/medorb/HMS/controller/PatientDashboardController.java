package com.medorb.HMS.controller;

import com.medorb.HMS.dto.AppointmentForm;
import com.medorb.HMS.model.Appointment;
import com.medorb.HMS.model.Doctor;
import com.medorb.HMS.model.Hospital;
import com.medorb.HMS.model.Patient;
import com.medorb.HMS.repository.AppointmentRepository;
import com.medorb.HMS.repository.DoctorRepository;
import com.medorb.HMS.repository.HospitalRepository;
import com.medorb.HMS.repository.PatientRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class PatientDashboardController {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final HospitalRepository hospitalRepository;

    @Autowired
    public PatientDashboardController(PatientRepository patientRepository,
                                      AppointmentRepository appointmentRepository,
                                      DoctorRepository doctorRepository,
                                      HospitalRepository hospitalRepository) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.hospitalRepository = hospitalRepository;
    }

    /**
     * GET /patient/dashboard
     * Shows the patient dashboard page, including patient info + past visit history.
     */
    @GetMapping("/patient/dashboard")
    public String showPatientDashboard(HttpServletRequest request, Model model) {
        // 1) Check if a patient is logged in
        Patient loggedInPatient = (Patient) request.getSession().getAttribute("loggedInPatient");
        if (loggedInPatient == null) {
            // Not logged in, redirect to home or login
            return "redirect:/";
        }

        // 2) Re-fetch the patient from the DB (to ensure we have fresh data)
        Optional<Patient> optionalPatient = patientRepository.findById(loggedInPatient.getPatientId());
        if (optionalPatient.isEmpty()) {
            // If somehow the patient is missing in DB, also redirect or show error
            return "redirect:/";
        }
        Patient dbPatient = optionalPatient.get();

        // 3) Fetch the patient's past visits/appointments
        //    e.g., completed appointments, or date < now. Adjust to your logic.
        //    For example, if your Appointment entity has status "COMPLETED"
        //    or "CANCELLED" for old visits. We'll assume "COMPLETED" means a past visit.
        List<Appointment> pastVisits = appointmentRepository.findPastVisitsByPatientId(dbPatient.getPatientId());
     // 4) Query for upcoming visits
        List<Appointment> upcomingVisits = appointmentRepository.findUpcomingVisitsByPatientId(dbPatient.getPatientId());

        // 5) Put everything in the model
        model.addAttribute("patient", dbPatient);
        model.addAttribute("pastVisits", pastVisits);
        model.addAttribute("upcomingVisits", upcomingVisits);

        // 5) Return the Thymeleaf template
        return "patient-dashboard";
    }
    
    /**
     * GET /patient/bookAppointment
     * Show the form to create a new appointment.
     */
    @GetMapping("/patient/bookAppointment")
    public String showBookAppointmentForm(
            @RequestParam(name="hospitalId", required=false) Integer hospitalId,
            @ModelAttribute("appointmentForm") AppointmentForm form,
            HttpServletRequest request,
            Model model
        ) {
            // 1) Check patient session
            Patient loggedIn = (Patient) request.getSession().getAttribute("loggedInPatient");
            if (loggedIn == null) {
                return "redirect:/"; 
            }

            // 2) All hospitals for the first dropdown
            List<Hospital> allHospitals = hospitalRepository.findAll();
            model.addAttribute("allHospitals", allHospitals);

            // 3) Possibly fetch doctors if hospitalId is chosen
            List<Doctor> doctorList = Collections.emptyList();
            if (hospitalId != null) {
                doctorList = doctorRepository.findByHospital_HospitalId(hospitalId);
            }
            model.addAttribute("doctorList", doctorList);

            // 4) We keep track of the "selected" hospital ID, so we can highlight it in the dropdown
            model.addAttribute("selectedHospitalId", hospitalId);

            // The form itself is "appointmentForm" (the @ModelAttribute above).
            // This lets us preserve user’s typed date/time & notes if they re-submit.

            return "patient-book-appointment";
        }

    /**
     * POST /patient/bookAppointment
     * Process the form submission to create a new appointment.
     */
    @PostMapping("/patient/bookAppointment")
    public String processBookAppointment(
            @ModelAttribute("appointmentForm") AppointmentForm form,
            HttpServletRequest request
        ) {
            // 1) Check session
            Patient loggedIn = (Patient) request.getSession().getAttribute("loggedInPatient");
            if (loggedIn == null) {
                return "redirect:/";
            }

            // 2) Re-fetch patient from DB
            Optional<Patient> optPat = patientRepository.findById(loggedIn.getPatientId());
            if (optPat.isEmpty()) {
                return "redirect:/"; // or error
            }
            Patient dbPatient = optPat.get();

            // 3) Re-fetch chosen hospital
            Optional<Hospital> optHosp = hospitalRepository.findById(form.getHospitalId());
            if (optHosp.isEmpty()) {
                // invalid
                return "redirect:/patient/bookAppointment?error=InvalidHospital";
            }
            Hospital dbHospital = optHosp.get();

            // 4) Re-fetch chosen doctor
            Optional<Doctor> optDoc = doctorRepository.findById(form.getDoctorId());
            if (optDoc.isEmpty()) {
                // invalid
                return "redirect:/patient/bookAppointment?error=InvalidDoctor";
            }
            Doctor dbDoctor = optDoc.get();

            // 5) Create Appointment
            Appointment newApp = new Appointment();
            newApp.setPatient(dbPatient);
            newApp.setHospital(dbHospital);
            newApp.setDoctor(dbDoctor);
            newApp.setAppointmentDatetime(form.getAppointmentDatetime());
            newApp.setNotes(form.getNotes());
            // By default => PENDING
            newApp.setStatus(Appointment.AppointmentStatus.PENDING);

            appointmentRepository.save(newApp);

            // 6) redirect to dash
            return "redirect:/patient/dashboard";
        }
    
    @GetMapping("/patient/updateProfile")
    public String showUpdateProfileForm(HttpServletRequest request, Model model) {
        Patient loggedIn = (Patient) request.getSession().getAttribute("loggedInPatient");
        if (loggedIn == null) {
            return "redirect:/";
        }

        // Re-fetch from DB if desired
        Optional<Patient> optionalPatient = patientRepository.findById(loggedIn.getPatientId());
        if (optionalPatient.isEmpty()) {
            return "redirect:/";
        }
        Patient dbPatient = optionalPatient.get();

        // Put it in model
        model.addAttribute("patientForm", dbPatient);
        return "patient-update-profile";
    }

    @PostMapping("/patient/updateProfile")
    public String processUpdateProfile(
            @ModelAttribute("patientForm") Patient formPatient,
            HttpServletRequest request
    ) {
        Patient loggedIn = (Patient) request.getSession().getAttribute("loggedInPatient");
        if (loggedIn == null) {
            return "redirect:/";
        }

        // Re-fetch from DB
        Optional<Patient> optionalPatient = patientRepository.findById(loggedIn.getPatientId());
        if (optionalPatient.isPresent()) {
            Patient dbPatient = optionalPatient.get();

            // Update the fields you allow
            dbPatient.setName(formPatient.getName());
            dbPatient.setEmail(formPatient.getEmail());
            dbPatient.setContactNumber(formPatient.getContactNumber());
            dbPatient.setAddress(formPatient.getAddress());
            // If you want to allow password change, handle it carefully (hashing, etc.)

            // Save
            patientRepository.save(dbPatient);

            // Update session
            request.getSession().setAttribute("loggedInPatient", dbPatient);
        }

        return "redirect:/patient/dashboard";
    }
}
