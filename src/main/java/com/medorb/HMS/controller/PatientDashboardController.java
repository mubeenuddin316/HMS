package com.medorb.HMS.controller;

import com.medorb.HMS.model.Appointment;
import com.medorb.HMS.model.Patient;
import com.medorb.HMS.repository.AppointmentRepository;
import com.medorb.HMS.repository.PatientRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class PatientDashboardController {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public PatientDashboardController(PatientRepository patientRepository,
                                      AppointmentRepository appointmentRepository) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
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

        // 4) Add data to the model
        model.addAttribute("patient", dbPatient);
        model.addAttribute("pastVisits", pastVisits);

        // 5) Return the Thymeleaf template
        return "patient-dashboard";
    }
}
