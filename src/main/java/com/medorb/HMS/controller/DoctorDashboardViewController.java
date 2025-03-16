package com.medorb.HMS.controller;

import com.medorb.HMS.model.Appointment;
import com.medorb.HMS.model.Appointment.AppointmentStatus;
import com.medorb.HMS.model.Doctor;
import com.medorb.HMS.model.Patient;
import com.medorb.HMS.repository.AppointmentRepository;
import com.medorb.HMS.repository.PatientRepository;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
public class DoctorDashboardViewController {    
    
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    
    @Autowired
    public DoctorDashboardViewController (AppointmentRepository appointmentRepository,
    		                              PatientRepository patientRepository
    ) {
    	this.appointmentRepository = appointmentRepository;
    	this.patientRepository = patientRepository;
    	
    }
	
    @GetMapping("/doctor/dashboard")
    public String showDoctorDashboard(HttpServletRequest request, Model model) {
        Doctor loggedInDoctor = (Doctor) request.getSession().getAttribute("loggedInDoctor");
        if (loggedInDoctor == null) {
            return "redirect:/";
        }

        Integer doctorId = loggedInDoctor.getDoctorId();
        // 1) Fetch all appointments for this doctor
        List<Appointment> allAppointments = appointmentRepository.findByDoctor_DoctorId(doctorId);

        // 2) Build a list of “upcoming” appointments:
        //    i.e., exclude COMPLETED, then sort by ascending date/time
        List<Appointment> upcomingAppointments = allAppointments.stream()
            .filter(app -> app.getStatus() != Appointment.AppointmentStatus.COMPLETED)
            .sorted((a, b) -> a.getAppointmentDatetime().compareTo(b.getAppointmentDatetime()))
            .toList(); // or .collect(Collectors.toList());

        // 3) Calculate your stat cards as before
        long totalAppointments = allAppointments.size();
        long scheduledCount = allAppointments.stream()
            .filter(app -> app.getStatus() == Appointment.AppointmentStatus.SCHEDULED 
                        || app.getStatus() == Appointment.AppointmentStatus.RESCHEDULED)
            .count();
        long pendingCount = allAppointments.stream()
            .filter(app -> app.getStatus() == Appointment.AppointmentStatus.PENDING)
            .count();
        long cancelledCount = allAppointments.stream()
            .filter(app -> app.getStatus() == Appointment.AppointmentStatus.CANCELLED)
            .count();
        long completedCount = allAppointments.stream()
            .filter(app -> app.getStatus() == Appointment.AppointmentStatus.COMPLETED)
            .count();

        // 4) Add attributes
        model.addAttribute("doctor", loggedInDoctor);
        // For your “Today’s Appointments” table, now we show upcoming (non-completed) only
        model.addAttribute("upcomingAppointments", upcomingAppointments);

        model.addAttribute("totalAppointments", totalAppointments);
        model.addAttribute("scheduledCount", scheduledCount);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("cancelledCount", cancelledCount);
        model.addAttribute("completedCount", completedCount);

        return "doctor-dashboard"; 
    }
    
    @GetMapping("/doctor/appointments/reschedule/{appointmentId}")
    public String showRescheduleForm(@PathVariable Integer appointmentId,
                                     HttpServletRequest request,
                                     Model model) {
        // 1) Check if doctor is logged in
        Doctor doctor = (Doctor) request.getSession().getAttribute("loggedInDoctor");
        if (doctor == null) {
            return "redirect:/";
        }

        // 2) Fetch appointment, ensure belongs to this doctor
        Optional<Appointment> optApp = appointmentRepository.findById(appointmentId);
        if (optApp.isEmpty()) {
            return "redirect:/doctor/dashboard";
        }
        Appointment appointment = optApp.get();
        if (!appointment.getDoctor().getDoctorId().equals(doctor.getDoctorId())) {
            // Not authorized
            return "redirect:/doctor/dashboard";
        }

        // 3) Put the appointment in the model
        model.addAttribute("appointment", appointment);

        // 4) Return a reschedule form page
        return "reschedule-appointment"; 
    }

    @PostMapping("/doctor/appointments/reschedule")
    public String processRescheduleForm(@ModelAttribute("appointment") Appointment formApp,
                                        HttpServletRequest request) {
        Doctor doctor = (Doctor) request.getSession().getAttribute("loggedInDoctor");
        if (doctor == null) {
            return "redirect:/";
        }

        // Re-fetch from DB, update date/time, set status to RESCHEDULED
        Optional<Appointment> optApp = appointmentRepository.findById(formApp.getAppointmentId());
        if (optApp.isPresent()) {
            Appointment dbApp = optApp.get();
            if (dbApp.getDoctor().getDoctorId().equals(doctor.getDoctorId())) {
                dbApp.setAppointmentDatetime(formApp.getAppointmentDatetime());
                dbApp.setStatus(Appointment.AppointmentStatus.RESCHEDULED);
                appointmentRepository.save(dbApp);
            }
        }
        return "redirect:/doctor/dashboard";
    }

    // CANCEL route - set status to CANCELLED
    @GetMapping("/doctor/appointments/cancel/{appointmentId}")
    public String cancelAppointment(@PathVariable Integer appointmentId,
                                    HttpServletRequest request) {
        Doctor doctor = (Doctor) request.getSession().getAttribute("loggedInDoctor");
        if (doctor == null) {
            return "redirect:/";
        }

        Optional<Appointment> optApp = appointmentRepository.findById(appointmentId);
        if (optApp.isPresent()) {
            Appointment dbApp = optApp.get();
            if (dbApp.getDoctor().getDoctorId().equals(doctor.getDoctorId())) {
                dbApp.setStatus(Appointment.AppointmentStatus.CANCELLED);
                appointmentRepository.save(dbApp);
            }
        }
        return "redirect:/doctor/dashboard";
    }
    
//    // 1) GET /doctor/appointments - Show all appointments for this doctor, sorted by date/time ascending
//    @GetMapping("/doctor/appointments")
//    public String showDoctorAppointments(HttpServletRequest request, Model model) {
//        // Check if doctor is logged in
//        Doctor loggedInDoctor = (Doctor) request.getSession().getAttribute("loggedInDoctor");
//        if (loggedInDoctor == null) {
//            return "redirect:/"; // Not logged in
//        }
//        Integer doctorId = loggedInDoctor.getDoctorId();
//
//        // Fetch all appointments for that doctor (example: in ascending date/time)
//        // If you want to filter out COMPLETED or CANCELLED, you can do it in code or a custom repo method
//        List<Appointment> doctorAppointments = appointmentRepository.findByDoctor_DoctorId(doctorId);
//
//        // (Optional) sort by ascending date/time if your repo doesn't already do so
//        // Could do a custom query, or we can do a simple .stream() approach:
//        doctorAppointments = doctorAppointments.stream()
//            .sorted((a, b) -> a.getAppointmentDatetime().compareTo(b.getAppointmentDatetime()))
//            .toList();
//
//        model.addAttribute("doctor", loggedInDoctor);
//        model.addAttribute("doctorAppointments", doctorAppointments);
//
//        return "doctor-appointments"; // The Thymeleaf page we'll create
//    }

    // 2) GET /doctor/appointments/complete/{id} - Mark an appointment as COMPLETED
    @GetMapping("/doctor/appointments/complete/{appointmentId}")
    public String completeAppointment(@PathVariable("appointmentId") Integer appointmentId,
                                      HttpServletRequest request) {
        Doctor loggedInDoctor = (Doctor) request.getSession().getAttribute("loggedInDoctor");
        if (loggedInDoctor == null) {
            return "redirect:/";
        }

        Optional<Appointment> optApp = appointmentRepository.findById(appointmentId);
        if (optApp.isPresent()) {
            Appointment dbApp = optApp.get();
            // Ensure it belongs to the logged-in doctor
            if (dbApp.getDoctor().getDoctorId().equals(loggedInDoctor.getDoctorId())) {
                dbApp.setStatus(AppointmentStatus.COMPLETED);
                appointmentRepository.save(dbApp);
            }
        }
        // After completion, redirect back to the appointments list
        return "redirect:/doctor/appointments";
    }
    
 // 1) GET /doctor/appointments - Show all appointments + create form
    @GetMapping("/doctor/appointments")
    public String showDoctorAppointments(HttpServletRequest request, Model model) {
        // Check if doctor is logged in
        Doctor loggedInDoctor = (Doctor) request.getSession().getAttribute("loggedInDoctor");
        if (loggedInDoctor == null) {
            return "redirect:/"; // Not logged in
        }
        Integer doctorId = loggedInDoctor.getDoctorId();

        // Fetch all appointments for that doctor, sorted by date/time ascending
        List<Appointment> doctorAppointments = appointmentRepository.findByDoctor_DoctorId(doctorId).stream()
            .sorted((a, b) -> a.getAppointmentDatetime().compareTo(b.getAppointmentDatetime()))
            .toList();

        // Provide a new Appointment object for the create form
        Appointment newAppointment = new Appointment();
        // Optionally set a default date/time or status if you want
        // newAppointment.setStatus(AppointmentStatus.SCHEDULED);

        // Provide a list of all patients (or filter them if you prefer)
        List<Patient> allPatients = patientRepository.findAll();

        // Put in model
        model.addAttribute("doctor", loggedInDoctor);
        model.addAttribute("doctorAppointments", doctorAppointments);
        model.addAttribute("newAppointment", newAppointment);
        model.addAttribute("allPatients", allPatients);

        return "doctor-appointments"; 
    }

    // 2) POST /doctor/appointments - Create a new appointment
    @PostMapping("/doctor/appointments")
    public String createAppointment(@ModelAttribute("newAppointment") Appointment formApp,
                                    HttpServletRequest request) {
        Doctor loggedInDoctor = (Doctor) request.getSession().getAttribute("loggedInDoctor");
        if (loggedInDoctor == null) {
            return "redirect:/"; // not logged in
        }

        // 1) Fill in missing fields: doctor, hospital, default status
        formApp.setDoctor(loggedInDoctor);
        formApp.setHospital(loggedInDoctor.getHospital()); // if your Doctor references a hospital
        if (formApp.getStatus() == null) {
            formApp.setStatus(AppointmentStatus.SCHEDULED); 
        }

        // 2) If you used a <select> for patient, formApp.patient already has the ID
        //    but you might want to re-fetch the actual patient entity from DB
        if (formApp.getPatient() != null && formApp.getPatient().getPatientId() != null) {
            Optional<Patient> p = patientRepository.findById(formApp.getPatient().getPatientId());
            p.ifPresent(formApp::setPatient);
        }

        // 3) Save
        appointmentRepository.save(formApp);

        // 4) Redirect back to /doctor/appointments
        return "redirect:/doctor/appointments";
    }

}
