
package com.medorb.HMS.controller;

import com.medorb.HMS.model.Appointment;
import com.medorb.HMS.model.Appointment.AppointmentStatus;
import com.medorb.HMS.model.OpdQueue.QueueStatus;
import com.medorb.HMS.model.Bed;
import com.medorb.HMS.model.Doctor; // ADD: import Doctor
import com.medorb.HMS.model.HospitalAdmin;
import com.medorb.HMS.model.OpdQueue;
import com.medorb.HMS.model.Patient;
import com.medorb.HMS.repository.AppointmentRepository;
import com.medorb.HMS.repository.BedRepository;
import com.medorb.HMS.repository.DoctorRepository;
import com.medorb.HMS.repository.HospitalAdminRepository;
import com.medorb.HMS.repository.OpdQueueRepository;
import com.medorb.HMS.repository.PatientRepository;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class HospitalAdminViewController {

    private final DoctorRepository doctorRepository;
    private final BedRepository bedRepository;
    private final AppointmentRepository appointmentRepository;
    private final OpdQueueRepository opdQueueRepository;
    private final PatientRepository patientRepository;
    private final HospitalAdminRepository hospitalAdminRepository;

    @Autowired
    public HospitalAdminViewController(
            DoctorRepository doctorRepository,
            BedRepository bedRepository,
            AppointmentRepository appointmentRepository,
            OpdQueueRepository opdQueueRepository,
            PatientRepository patientRepository,
            HospitalAdminRepository hospitalAdminRepository
    ) {
        this.doctorRepository = doctorRepository;
        this.bedRepository = bedRepository;
        this.appointmentRepository = appointmentRepository;
        this.opdQueueRepository = opdQueueRepository;
        this.patientRepository = patientRepository;
        this.hospitalAdminRepository = hospitalAdminRepository;
        
    }

    @GetMapping("/hospitalAdmin/dashboard")
    public String showHospitalAdminDashboard(HttpServletRequest request, Model model) {
        // 1) Retrieve the logged-in HospitalAdmin from session
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");

        // If not logged in or no hospital found, redirect to home
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        model.addAttribute("hospitalAdmin", hospitalAdmin);

        // 2) Real data: get hospitalId
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // ============== DOCTORS & BEDS STATS ==============
        long totalDoctors = doctorRepository.countByHospital_HospitalId(hospitalId);
        long totalBeds = bedRepository.countByHospital_HospitalId(hospitalId);
        long occupiedBeds = bedRepository.countByHospital_HospitalIdAndIsOccupiedTrue(hospitalId);
        long availableBeds = totalBeds - occupiedBeds;

        // ============== APPOINTMENT STATS ==============
        long scheduledCount  = appointmentRepository.countByHospital_HospitalIdAndStatus(hospitalId, AppointmentStatus.SCHEDULED);
        long pendingCount    = appointmentRepository.countByHospital_HospitalIdAndStatus(hospitalId, AppointmentStatus.PENDING);
        long cancelledCount  = appointmentRepository.countByHospital_HospitalIdAndStatus(hospitalId, AppointmentStatus.CANCELLED);

        // ============== LAST 30 APPOINTMENTS (SCHEDULED/RESCHEDULED/PENDING/CANCELLED) ==============
        List<Appointment> last30Appointments = appointmentRepository.findRecentAppointmentsByStatuses(
                hospitalId,
                List.of(AppointmentStatus.SCHEDULED, AppointmentStatus.RESCHEDULED,
                        AppointmentStatus.PENDING, AppointmentStatus.CANCELLED),
                PageRequest.of(0, 30)
        );

        // 3) Put stats & appointments in model
        model.addAttribute("totalDoctors", totalDoctors);
        model.addAttribute("totalBeds", totalBeds);
        model.addAttribute("occupiedBeds", occupiedBeds);
        model.addAttribute("availableBeds", availableBeds);

        model.addAttribute("scheduledCount", scheduledCount);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("cancelledCount", cancelledCount);

        model.addAttribute("last30Appointments", last30Appointments);

        // 4) Return the Thymeleaf template
        return "hospital-admin-dashboard";
    }

    @GetMapping("/hospitalAdmin/appointments/schedule/form/{appointmentId}")
    public String showScheduleForm(
        @PathVariable("appointmentId") Integer appointmentId,
        HttpServletRequest request,
        Model model
    ) {
        // 1) Check if admin is logged in
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/"; // not logged in
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // 2) Fetch appointment from DB
        Optional<Appointment> optionalApp = appointmentRepository.findById(appointmentId);
        if (optionalApp.isEmpty()) {
            return "redirect:/hospitalAdmin/dashboard"; // not found
        }
        Appointment app = optionalApp.get();

        // 3) Ensure appointment belongs to this admin's hospital
        if (!app.getHospital().getHospitalId().equals(hospitalId)) {
            return "redirect:/hospitalAdmin/dashboard"; // unauthorized
        }

        // ADD: Fetch all doctors for this hospital to populate the <select> 
        List<Doctor> allDoctors = doctorRepository.findByHospital_HospitalId(hospitalId);
        model.addAttribute("allDoctors", allDoctors);

        // 4) Put the appointment in model so we can show date/time
        model.addAttribute("appointmentToSchedule", app);

        // 5) Return the form page
        return "appointment-schedule"; // The .html file you created
    }

    @PostMapping("/hospitalAdmin/appointments/schedule")
    public String processScheduleForm(
        @ModelAttribute("appointmentToSchedule") Appointment formAppointment,
        HttpServletRequest request
    ) {
        // 1) Check admin
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // 2) Re-fetch from DB
        Optional<Appointment> optionalApp = appointmentRepository.findById(formAppointment.getAppointmentId());
        if (optionalApp.isPresent()) {
            Appointment dbApp = optionalApp.get();

            // 3) Check hospital match
            if (!dbApp.getHospital().getHospitalId().equals(hospitalId)) {
                return "redirect:/hospitalAdmin/dashboard";
            }

            // ADD: If user changed the doctor in the form
            if (formAppointment.getDoctor() != null && formAppointment.getDoctor().getDoctorId() != null) {
                Doctor newDoctor = doctorRepository.findById(formAppointment.getDoctor().getDoctorId())
                                   .orElse(dbApp.getDoctor());
                dbApp.setDoctor(newDoctor);
            }

            // 4) Compare old/new date/time
            LocalDateTime oldDateTime = dbApp.getAppointmentDatetime();
            LocalDateTime newDateTime = formAppointment.getAppointmentDatetime();

            if (!Objects.equals(oldDateTime, newDateTime)) {
                // The date/time was changed => decide new status
                AppointmentStatus oldStatus = dbApp.getStatus();
                AppointmentStatus newStatus = oldStatus;

                switch (oldStatus) {
                    case PENDING -> {
                        // If was PENDING, scheduling sets to SCHEDULED
                        newStatus = AppointmentStatus.SCHEDULED;
                    }
                    case SCHEDULED, CANCELLED, COMPLETED, RESCHEDULED -> {
                        // If it was any of these, it becomes RESCHEDULED
                        newStatus = AppointmentStatus.RESCHEDULED;
                    }
                    // If you have other statuses, handle them here
                }

                // Update date/time + new status
                dbApp.setAppointmentDatetime(newDateTime);
                dbApp.setStatus(newStatus);
                appointmentRepository.save(dbApp);

            } else {
                // If date/time didn't change, do nothing or keep old status
                // Possibly show a message or simply ignore
                appointmentRepository.save(dbApp); // in case doctor changed but not date/time
            }
        }

        // 5) Redirect back to dashboard
        return "redirect:/hospitalAdmin/dashboard";
    }

    
    @GetMapping("/hospitalAdmin/appointments/cancel/{appointmentId}")
    public String cancelAppointment(
        @PathVariable("appointmentId") Integer appointmentId,
        HttpServletRequest request
    ) {
        // 1) Get the logged-in admin from session
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/"; // not logged in
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // 2) Fetch the appointment from DB
        Optional<Appointment> optionalApp = appointmentRepository.findById(appointmentId);
        if (optionalApp.isPresent()) {
            Appointment app = optionalApp.get();

            // 3) Ensure the appointment belongs to this hospital
            if (!app.getHospital().getHospitalId().equals(hospitalId)) {
                return "redirect:/hospitalAdmin/dashboard"; 
            }

            // 4) Update status to CANCELLED
            app.setStatus(AppointmentStatus.CANCELLED);
            appointmentRepository.save(app);
        }

        // 5) Redirect back to the dashboard
        return "redirect:/hospitalAdmin/dashboard";
    }
    
    // =========================================================
    // MANAGE DOCTORS FEATURE
    // =========================================================

    // 1) GET /hospitalAdmin/doctors - Show all doctors for this hospital + create form
    @GetMapping("/hospitalAdmin/doctors")
    public String showDoctorsManagementPage(HttpServletRequest request, Model model) {
        // Check if admin is logged in
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/"; 
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // Fetch doctors for this hospital
        List<Doctor> doctors = doctorRepository.findByHospital_HospitalId(hospitalId);
        model.addAttribute("doctors", doctors);

        // Provide a blank Doctor object for create form
        Doctor newDoctor = new Doctor();
        model.addAttribute("newDoctor", newDoctor);

        return "manage-doctors"; // We'll create manage-doctors.html
    }

    // 2) POST /hospitalAdmin/doctors - Create a new doctor
    @PostMapping("/hospitalAdmin/doctors")
    public String createDoctor(@ModelAttribute Doctor newDoctor, HttpServletRequest request) {
        // Check admin
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        // Assign the same hospital to the new doctor
        newDoctor.setHospital(hospitalAdmin.getHospital());

        // Save
        doctorRepository.save(newDoctor);

        // Redirect back to the list
        return "redirect:/hospitalAdmin/doctors";
    }

    // 3) GET /hospitalAdmin/doctors/edit/{doctorId} - Show edit form
    @GetMapping("/hospitalAdmin/doctors/edit/{doctorId}")
    public String showEditDoctorForm(@PathVariable Integer doctorId,
                                     HttpServletRequest request,
                                     Model model) {
        // Check admin
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // Fetch the doctor
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        if (optionalDoctor.isEmpty()) {
            return "redirect:/hospitalAdmin/doctors"; 
        }
        Doctor doctor = optionalDoctor.get();

        // Ensure belongs to this hospital
        if (!doctor.getHospital().getHospitalId().equals(hospitalId)) {
            return "redirect:/hospitalAdmin/doctors";
        }

        // Put doctor in model
        model.addAttribute("doctor", doctor);

        // Return edit form page
        return "edit-doctor"; // We'll create edit-doctor.html
    }

    // 4) POST /hospitalAdmin/doctors/edit - Update an existing doctor
    @PostMapping("/hospitalAdmin/doctors/edit")
    public String updateDoctor(@ModelAttribute Doctor updatedDoctor,
                               HttpServletRequest request) {
        // Check admin
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // Re-fetch from DB to ensure we only update a doc in this hospital
        Optional<Doctor> optionalDoc = doctorRepository.findById(updatedDoctor.getDoctorId());
        if (optionalDoc.isPresent()) {
            Doctor dbDoctor = optionalDoc.get();

            if (!dbDoctor.getHospital().getHospitalId().equals(hospitalId)) {
                return "redirect:/hospitalAdmin/doctors";
            }

            // Update fields
            dbDoctor.setName(updatedDoctor.getName());
            dbDoctor.setSpecialization(updatedDoctor.getSpecialization());
            dbDoctor.setEmail(updatedDoctor.getEmail());
            dbDoctor.setPassword(updatedDoctor.getPassword());
            dbDoctor.setProfileImagePath(updatedDoctor.getProfileImagePath());
            // We do NOT change hospital, it remains the same

            doctorRepository.save(dbDoctor);
        }

        return "redirect:/hospitalAdmin/doctors";
    }

    // 5) GET /hospitalAdmin/doctors/delete/{doctorId} - Delete a doctor
    @GetMapping("/hospitalAdmin/doctors/delete/{doctorId}")
    public String deleteDoctor(@PathVariable Integer doctorId,
                               HttpServletRequest request) {
        // Check admin
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // Fetch doc
        Optional<Doctor> optionalDoc = doctorRepository.findById(doctorId);
        if (optionalDoc.isPresent()) {
            Doctor doctor = optionalDoc.get();

            if (doctor.getHospital().getHospitalId().equals(hospitalId)) {
                // OK to delete
                doctorRepository.delete(doctor);
            }
        }

        return "redirect:/hospitalAdmin/doctors";
    }
    
    // =========================================================
    // MANAGE BEDS FEATURE
    // =========================================================

    // 1) GET /hospitalAdmin/beds - Show all beds + create form
    @GetMapping("/hospitalAdmin/beds")
    public String showBedsManagementPage(HttpServletRequest request, Model model) {
        // Check if admin is logged in
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/"; 
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // Fetch beds for this hospital
        List<Bed> beds = bedRepository.findByHospital_HospitalId(hospitalId);
        model.addAttribute("beds", beds);

        // Provide a blank Bed object for create form
        Bed newBed = new Bed();
        model.addAttribute("newBed", newBed);

        return "manage-beds-hsp"; // We'll create manage-beds-hsp.html
    }

    // 2) POST /hospitalAdmin/beds - Create a new bed
    @PostMapping("/hospitalAdmin/beds")
    public String createBed(@ModelAttribute Bed newBed, HttpServletRequest request) {
        // Check admin
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        // Assign the same hospital to the new bed
        newBed.setHospital(hospitalAdmin.getHospital());

        // Save
        bedRepository.save(newBed);

        // Redirect back to the list
        return "redirect:/hospitalAdmin/beds";
    }

    // 3) GET /hospitalAdmin/beds/edit/{bedId} - Show edit form
    @GetMapping("/hospitalAdmin/beds/edit/{bedId}")
    public String showEditBedForm(@PathVariable Integer bedId,
                                  HttpServletRequest request,
                                  Model model) {
        // Check admin
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // Fetch the bed
        Optional<Bed> optionalBed = bedRepository.findById(bedId);
        if (optionalBed.isEmpty()) {
            return "redirect:/hospitalAdmin/beds"; 
        }
        Bed bed = optionalBed.get();

        // Ensure belongs to this hospital
        if (!bed.getHospital().getHospitalId().equals(hospitalId)) {
            return "redirect:/hospitalAdmin/beds";
        }

        // Put bed in model
        model.addAttribute("bed", bed);

        // Return edit form page
        return "edit-bed-hsp"; // We'll create edit-bed-hsp.html
    }

    // 4) POST /hospitalAdmin/beds/edit - Update an existing bed
    @PostMapping("/hospitalAdmin/beds/edit")
    public String updateBed(@ModelAttribute Bed updatedBed,
                            HttpServletRequest request) {
        // Check admin
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // Re-fetch from DB to ensure we only update a bed in this hospital
        Optional<Bed> optionalBed = bedRepository.findById(updatedBed.getBedId());
        if (optionalBed.isPresent()) {
            Bed dbBed = optionalBed.get();

            if (!dbBed.getHospital().getHospitalId().equals(hospitalId)) {
                return "redirect:/hospitalAdmin/beds";
            }

            // Update fields
            dbBed.setBedNumber(updatedBed.getBedNumber());
            dbBed.setWard(updatedBed.getWard());
            dbBed.setRoomNumber(updatedBed.getRoomNumber());
            dbBed.setBedType(updatedBed.getBedType());
            dbBed.setOccupied(updatedBed.isOccupied());
            dbBed.setBedStatus(updatedBed.getBedStatus());
            // We do NOT change hospital, it remains the same

            bedRepository.save(dbBed);
        }

        return "redirect:/hospitalAdmin/beds";
    }

    // 5) GET /hospitalAdmin/beds/delete/{bedId} - Delete a bed
    @GetMapping("/hospitalAdmin/beds/delete/{bedId}")
    public String deleteBed(@PathVariable Integer bedId,
                            HttpServletRequest request) {
        // Check admin
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // Fetch bed
        Optional<Bed> optionalBed = bedRepository.findById(bedId);
        if (optionalBed.isPresent()) {
            Bed bed = optionalBed.get();

            if (bed.getHospital().getHospitalId().equals(hospitalId)) {
                // OK to delete
                bedRepository.delete(bed);
            }
        }

        return "redirect:/hospitalAdmin/beds";
    }
    
 // =========================================================
    // MANAGE OPD QUEUE FEATURE
    // =========================================================

    // 1) GET /hospitalAdmin/opdQueue - Show all queue entries + create form
    @GetMapping("/hospitalAdmin/opdQueue")
    public String showOpdQueueManagementPage(HttpServletRequest request, Model model) {
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // 1) OPD Queue entries
        List<OpdQueue> queueList = opdQueueRepository.findByHospital_HospitalId(hospitalId);
        model.addAttribute("queueList", queueList);

        // 2) Blank OpdQueue for create form
        OpdQueue newQueue = new OpdQueue();
        newQueue.setRegistrationTime(LocalDateTime.now());
        model.addAttribute("newQueue", newQueue);

        // 3) Provide Doctor and Patient lists
        //    (Assuming you have findByHospital_HospitalId for doctors 
        //     and maybe all patients or same-hospital patients)
        List<Doctor> doctorList = doctorRepository.findByHospital_HospitalId(hospitalId);
        model.addAttribute("doctorList", doctorList);

        // If you store hospital ID in patients, do findByHospitalId;
        // else fetch all patients (depending on your logic).
        List<Patient> patientList = patientRepository.findAll(); 
        model.addAttribute("patientList", patientList);

        return "manage-opdqueue-hsp"; // or your page name
    }

    // 2) POST /hospitalAdmin/opdQueue - Create a new queue entry
    @PostMapping("/hospitalAdmin/opdQueue")
    public String createOpdQueue(@ModelAttribute OpdQueue newQueue, HttpServletRequest request) {
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        // Re-fetch actual Patient and Doctor from their IDs
        if (newQueue.getPatient() != null && newQueue.getPatient().getPatientId() != null) {
            Patient p = patientRepository.findById(newQueue.getPatient().getPatientId()).orElse(null);
            newQueue.setPatient(p);
        }
        if (newQueue.getDoctor() != null && newQueue.getDoctor().getDoctorId() != null) {
            Doctor d = doctorRepository.findById(newQueue.getDoctor().getDoctorId()).orElse(null);
            newQueue.setDoctor(d);
        }

        // Assign hospital
        newQueue.setHospital(hospitalAdmin.getHospital());

        // Set a default status if needed
        if (newQueue.getQueueStatus() == null) {
            newQueue.setQueueStatus(QueueStatus.WAITING);
        }

        opdQueueRepository.save(newQueue);
        return "redirect:/hospitalAdmin/opdQueue";
    }


    // 3) GET /hospitalAdmin/opdQueue/edit/{id} - Show edit form
    @GetMapping("/hospitalAdmin/opdQueue/edit/{queueId}")
    public String showEditOpdQueueForm(@PathVariable Integer queueId,
                                       HttpServletRequest request,
                                       Model model) {
        // 1) Check admin
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // 2) Fetch the queue entry
        Optional<OpdQueue> optionalQueue = opdQueueRepository.findById(queueId);
        if (optionalQueue.isEmpty()) {
            return "redirect:/hospitalAdmin/opdQueue"; 
        }
        OpdQueue opdQueue = optionalQueue.get();

        // Ensure belongs to this hospital
        if (!opdQueue.getHospital().getHospitalId().equals(hospitalId)) {
            return "redirect:/hospitalAdmin/opdQueue";
        }

        // 3) Fetch patientList & doctorList
        //    If you have a method findAll() or findByHospitalId, pick accordingly
        List<Patient> patientList = patientRepository.findAll(); // or a custom method
        List<Doctor> doctorList = doctorRepository.findByHospital_HospitalId(hospitalId);

        model.addAttribute("patientList", patientList);
        model.addAttribute("doctorList", doctorList);

        // 4) Put queue entry in model
        model.addAttribute("opdQueue", opdQueue);

        // 5) Return edit form page
        return "edit-opdqueue-hsp";
    }

    // 4) POST /hospitalAdmin/opdQueue/edit - Update an existing queue entry
    @PostMapping("/hospitalAdmin/opdQueue/edit")
    public String updateOpdQueue(@ModelAttribute OpdQueue updatedQueue,
                                 HttpServletRequest request) {
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        Optional<OpdQueue> optionalQ = opdQueueRepository.findById(updatedQueue.getOpdQueueId());
        if (optionalQ.isPresent()) {
            OpdQueue dbQueue = optionalQ.get();

            if (!dbQueue.getHospital().getHospitalId().equals(hospitalId)) {
                return "redirect:/hospitalAdmin/opdQueue";
            }

            // Re-fetch Patient
            if (updatedQueue.getPatient() != null && updatedQueue.getPatient().getPatientId() != null) {
                Patient p = patientRepository.findById(updatedQueue.getPatient().getPatientId()).orElse(null);
                dbQueue.setPatient(p);
            }

            // Re-fetch Doctor
            if (updatedQueue.getDoctor() != null && updatedQueue.getDoctor().getDoctorId() != null) {
                Doctor d = doctorRepository.findById(updatedQueue.getDoctor().getDoctorId()).orElse(null);
                dbQueue.setDoctor(d);
            }

            dbQueue.setRegistrationTime(updatedQueue.getRegistrationTime());
            dbQueue.setQueueStatus(updatedQueue.getQueueStatus());
            dbQueue.setTokenNumber(updatedQueue.getTokenNumber());

            opdQueueRepository.save(dbQueue);
        }

        return "redirect:/hospitalAdmin/opdQueue";
    }


    // 5) GET /hospitalAdmin/opdQueue/delete/{queueId} - Delete a queue entry
    @GetMapping("/hospitalAdmin/opdQueue/delete/{queueId}")
    public String deleteOpdQueue(@PathVariable Integer queueId,
                                 HttpServletRequest request) {
        // Check admin
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // Fetch
        Optional<OpdQueue> optionalQ = opdQueueRepository.findById(queueId);
        if (optionalQ.isPresent()) {
            OpdQueue q = optionalQ.get();

            if (q.getHospital().getHospitalId().equals(hospitalId)) {
                // OK to delete
                opdQueueRepository.delete(q);
            }
        }

        return "redirect:/hospitalAdmin/opdQueue";
    }
    
    // =========================================================
    // MANAGE Appointments FEATURE
    // =========================================================
 // 1) GET /hospitalAdmin/appointments - Show all appointments + create form
    @GetMapping("/hospitalAdmin/appointments")
    public String showAppointmentsManagementPage(HttpServletRequest request, Model model) {
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // Fetch all appointments for this hospital
        List<Appointment> appointmentList = appointmentRepository.findByHospital_HospitalId(hospitalId);
        model.addAttribute("appointmentList", appointmentList);

        // Provide a blank Appointment object for create form
        Appointment newAppointment = new Appointment();
        // Possibly set a default date/time or status
        // newAppointment.setAppointmentDatetime(LocalDateTime.now());
        // newAppointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);
        model.addAttribute("newAppointment", newAppointment);

        // (Optional) If you want patient/doctor dropdowns:
        List<Doctor> doctorList = doctorRepository.findByHospital_HospitalId(hospitalId);
        model.addAttribute("doctorList", doctorList);
        // If you only want patients from this hospital, do a custom method; else do findAll():
        List<Patient> patientList = patientRepository.findAll();
        model.addAttribute("patientList", patientList);

        return "manage-appointments-hsp"; // We'll create manage-appointments-hsp.html
    }

    // 2) POST /hospitalAdmin/appointments - Create a new appointment
    @PostMapping("/hospitalAdmin/appointments")
    public String createAppointment(@ModelAttribute Appointment newAppointment,
                                    HttpServletRequest request) {
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // Re-fetch Patient & Doctor if using dropdown IDs
        if (newAppointment.getPatient() != null && newAppointment.getPatient().getPatientId() != null) {
            Patient p = patientRepository.findById(newAppointment.getPatient().getPatientId()).orElse(null);
            newAppointment.setPatient(p);
        }
        if (newAppointment.getDoctor() != null && newAppointment.getDoctor().getDoctorId() != null) {
            Doctor d = doctorRepository.findById(newAppointment.getDoctor().getDoctorId()).orElse(null);
            newAppointment.setDoctor(d);
        }

        // Assign hospital
        newAppointment.setHospital(hospitalAdmin.getHospital());

        // Save
        appointmentRepository.save(newAppointment);
        return "redirect:/hospitalAdmin/appointments";
    }

    // 3) GET /hospitalAdmin/appointments/edit/{id} - Show edit form
    @GetMapping("/hospitalAdmin/appointments/edit/{appointmentId}")
    public String showEditAppointmentForm(@PathVariable Integer appointmentId,
                                          HttpServletRequest request,
                                          Model model) {
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        // Fetch the appointment
        Optional<Appointment> optionalApp = appointmentRepository.findById(appointmentId);
        if (optionalApp.isEmpty()) {
            return "redirect:/hospitalAdmin/appointments"; 
        }
        Appointment appointment = optionalApp.get();

        // Ensure belongs to this hospital
        if (!appointment.getHospital().getHospitalId().equals(hospitalId)) {
            return "redirect:/hospitalAdmin/appointments";
        }

        // If you want doctor/patient dropdown in edit form:
        List<Doctor> doctorList = doctorRepository.findByHospital_HospitalId(hospitalId);
        model.addAttribute("doctorList", doctorList);
        List<Patient> patientList = patientRepository.findAll();
        model.addAttribute("patientList", patientList);

        // Put appointment in model
        model.addAttribute("appointment", appointment);

        return "edit-appointment-hsp"; // We'll create edit-appointment-hsp.html
    }

    // 4) POST /hospitalAdmin/appointments/edit - Update an existing appointment
    @PostMapping("/hospitalAdmin/appointments/edit")
    public String updateAppointment(@ModelAttribute Appointment updatedApp,
                                    HttpServletRequest request) {
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        Optional<Appointment> optionalApp = appointmentRepository.findById(updatedApp.getAppointmentId());
        if (optionalApp.isPresent()) {
            Appointment dbApp = optionalApp.get();

            if (!dbApp.getHospital().getHospitalId().equals(hospitalId)) {
                return "redirect:/hospitalAdmin/appointments";
            }

            // Re-fetch patient & doctor if using dropdown IDs
            if (updatedApp.getPatient() != null && updatedApp.getPatient().getPatientId() != null) {
                Patient p = patientRepository.findById(updatedApp.getPatient().getPatientId()).orElse(null);
                dbApp.setPatient(p);
            }
            if (updatedApp.getDoctor() != null && updatedApp.getDoctor().getDoctorId() != null) {
                Doctor d = doctorRepository.findById(updatedApp.getDoctor().getDoctorId()).orElse(null);
                dbApp.setDoctor(d);
            }

            dbApp.setAppointmentDatetime(updatedApp.getAppointmentDatetime());
            dbApp.setStatus(updatedApp.getStatus());
            dbApp.setNotes(updatedApp.getNotes());
            // hospital remains the same

            appointmentRepository.save(dbApp);
        }

        return "redirect:/hospitalAdmin/appointments";
    }

    // 5) GET /hospitalAdmin/appointments/delete/{id} - Delete appointment
    @GetMapping("/hospitalAdmin/appointments/delete/{appointmentId}")
    public String deleteAppointment(@PathVariable Integer appointmentId,
                                    HttpServletRequest request) {
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null || hospitalAdmin.getHospital() == null) {
            return "redirect:/";
        }
        Integer hospitalId = hospitalAdmin.getHospital().getHospitalId();

        Optional<Appointment> optionalApp = appointmentRepository.findById(appointmentId);
        if (optionalApp.isPresent()) {
            Appointment appointment = optionalApp.get();
            if (appointment.getHospital().getHospitalId().equals(hospitalId)) {
                appointmentRepository.delete(appointment);
            }
        }

        return "redirect:/hospitalAdmin/appointments";
    }
    
    
    // =========================================================
    // MANAGE Patients FEATURE
    // =========================================================   
    
    // 1) GET /hospitalAdmin/patients - Show all patients + create form
    @GetMapping("/hospitalAdmin/patients")
    public String showPatientsManagementPage(HttpServletRequest request, Model model) {
        // 1) Check if admin is logged in (no hospital filter, since Patient not tied to Hospital)
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null) {
            return "redirect:/";
        }

        // 2) Fetch ALL patients (no hospital_id in Patient)
        List<Patient> patientList = patientRepository.findAll();
        model.addAttribute("patientList", patientList);

        // 3) Provide a blank patient for the create form
        Patient newPatient = new Patient();
        model.addAttribute("newPatient", newPatient);

        // 4) Return the manage-patients-hsp page
        return "manage-patients-hsp";
    }

    // 2) POST /hospitalAdmin/patients - Create a new patient
    @PostMapping("/hospitalAdmin/patients")
    public String createPatient(@ModelAttribute Patient newPatient, HttpServletRequest request) {
        // Check admin
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null) {
            return "redirect:/";
        }

        // Just save the new patient (no hospital assignment)
        patientRepository.save(newPatient);
        return "redirect:/hospitalAdmin/patients";
    }

    // 3) GET /hospitalAdmin/patients/edit/{id} - Show edit form
    @GetMapping("/hospitalAdmin/patients/edit/{patientId}")
    public String showEditPatientForm(@PathVariable Integer patientId,
                                      HttpServletRequest request,
                                      Model model) {
        // Check admin
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null) {
            return "redirect:/";
        }

        // Fetch the patient from DB
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);
        if (optionalPatient.isEmpty()) {
            return "redirect:/hospitalAdmin/patients";
        }
        Patient patient = optionalPatient.get();

        // Put patient in model
        model.addAttribute("patient", patient);

        // Return edit-patient-hsp page
        return "edit-patient-hsp";
    }

    // 4) POST /hospitalAdmin/patients/edit - Update an existing patient
    @PostMapping("/hospitalAdmin/patients/edit")
    public String updatePatient(@ModelAttribute Patient updatedPatient, HttpServletRequest request) {
        // Check admin
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null) {
            return "redirect:/";
        }

        // Re-fetch from DB
        Optional<Patient> optionalPat = patientRepository.findById(updatedPatient.getPatientId());
        if (optionalPat.isPresent()) {
            Patient dbPatient = optionalPat.get();

            // Update fields
            dbPatient.setName(updatedPatient.getName());
            dbPatient.setDob(updatedPatient.getDob());
            dbPatient.setGender(updatedPatient.getGender());
            dbPatient.setContactNumber(updatedPatient.getContactNumber());
            dbPatient.setEmail(updatedPatient.getEmail());
            dbPatient.setPassword(updatedPatient.getPassword());
            dbPatient.setAddress(updatedPatient.getAddress());

            patientRepository.save(dbPatient);
        }

        return "redirect:/hospitalAdmin/patients";
    }
    
    // =================================================================
    // Hospital Admin Profile Update
    // =================================================================
    
    @GetMapping("/hospitalAdmin/profile")
    public String showAdminProfile(HttpServletRequest request, Model model) {
        // 1) Check if admin is logged in
        HospitalAdmin hospitalAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (hospitalAdmin == null) {
            return "redirect:/";
        }

        // 2) Re-fetch from DB (to ensure we have the latest)
        Optional<HospitalAdmin> optionalHa = hospitalAdminRepository.findById(hospitalAdmin.getHospitalAdminId());
        if (optionalHa.isEmpty()) {
            return "redirect:/"; // or handle error differently
        }
        HospitalAdmin dbHa = optionalHa.get();

        // 3) Put in model
        model.addAttribute("adminProfile", dbHa);

        // 4) Return the profile page
        return "hospital-admin-profile-hsp";
    }

    @PostMapping("/hospitalAdmin/profile")
    public String updateAdminProfile(@ModelAttribute("adminProfile") HospitalAdmin updatedAdmin,
                                     HttpServletRequest request) {
        // 1) Check if admin is logged in
        HospitalAdmin sessionAdmin = (HospitalAdmin) request.getSession().getAttribute("loggedInHospitalAdmin");
        if (sessionAdmin == null) {
            return "redirect:/";
        }

        // 2) Re-fetch from DB
        Optional<HospitalAdmin> optionalHa = hospitalAdminRepository.findById(sessionAdmin.getHospitalAdminId());
        if (optionalHa.isPresent()) {
            HospitalAdmin dbHa = optionalHa.get();

            // 3) Update fields
            dbHa.setAdminName(updatedAdmin.getAdminName());
            dbHa.setEmail(updatedAdmin.getEmail());
            dbHa.setPassword(updatedAdmin.getPassword());
            dbHa.setPhoneNumber(updatedAdmin.getPhoneNumber());
            // We do NOT change the hospital reference

            hospitalAdminRepository.save(dbHa);

            // 4) Update session if you want the changes reflected immediately
            request.getSession().setAttribute("loggedInHospitalAdmin", dbHa);
        }

        // 5) Redirect back to the profile page
        return "redirect:/hospitalAdmin/profile";
    }


}
