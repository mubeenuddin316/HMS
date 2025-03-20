package com.medorb.HMS.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List; // Import List
import java.util.Map;
import java.util.Optional; // Import Optional

import org.springframework.beans.factory.annotation.Autowired; // Import Autowired
import org.springframework.http.HttpStatus; // Import HttpStatus
import org.springframework.http.ResponseEntity; // Import ResponseEntity
import org.springframework.ui.Model;
// Import Controller annotations
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medorb.HMS.model.Appointment;
import com.medorb.HMS.model.Patient; // Import Patient entity
import com.medorb.HMS.repository.AppointmentRepository;
import com.medorb.HMS.service.PatientService; // Import PatientService

import jakarta.servlet.http.HttpServletRequest;

@RestController // Marks this class as a REST Controller
@RequestMapping("/api/patients") // Base URL path for all endpoints in this controller
public class PatientController {

    private final PatientService patientService; // Inject PatientService
    private final AppointmentRepository appointmentRepository;

    @Autowired // Constructor injection of PatientService
    public PatientController(PatientService patientService, AppointmentRepository appointmentRepository) {
        this.patientService = patientService;
        this.appointmentRepository = appointmentRepository;
    }

    // 1. POST /api/patients - Create a new patient
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient createdPatient = patientService.createPatient(patient);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED); // 201 Created status
    }

    // 2. GET /api/patients - Get all patients
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK); // 200 OK status
    }

    // 3. GET /api/patients/{patientId} - Get a patient by ID
    @GetMapping("/{patientId}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Integer patientId) {
        Optional<Patient> patient = patientService.getPatientById(patientId);
        return patient.map(value -> new ResponseEntity<>(value, HttpStatus.OK)) // 200 OK if found
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found if not found
    }

    // 4. PUT /api/patients/{patientId} - Update an existing patient
    @PutMapping("/{patientId}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Integer patientId, @RequestBody Patient patient) {
        Patient updatedPatient = patientService.updatePatient(patientId, patient);
        if (updatedPatient != null) {
            return new ResponseEntity<>(updatedPatient, HttpStatus.OK); // 200 OK if updated
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found if patient ID doesn't exist
        }
    }

    // 5. DELETE /api/patients/{patientId} - Delete a patient by ID
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer patientId) {
        patientService.deletePatient(patientId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content after successful delete
    }

    // Custom Endpoints (Optional for now, but good to have based on PatientService)

    // 6. GET /api/patients/email/{email} - Get patient by email
    @GetMapping("/email/{email}")
    public ResponseEntity<Patient> getPatientByEmail(@PathVariable String email) {
        Optional<Patient> patient = patientService.getPatientByEmail(email);
        return patient.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginPatient(@RequestBody Map<String, String> credentials,
                                          HttpServletRequest request) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email and password are required"
            ));
        }

        Optional<Patient> patientOptional = patientService.getPatientByEmail(email);

        if (patientOptional.isPresent() && patientOptional.get().getPassword().equals(password)) {
            // 1) Store in session
            request.getSession().setAttribute("loggedInPatient", patientOptional.get());

            // 2) Return JSON success + redirect path
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Login successful",
                    "patientId", patientOptional.get().getPatientId(),
                    "redirect", "/patient/dashboard"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "Invalid email or password"
            ));
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerPatient(@RequestBody Patient patient) {
        Optional<Patient> existingPatient = patientService.getPatientByEmail(patient.getEmail());

        if (existingPatient.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email already exists. Try logging in."
            ));
        }

        Patient savedPatient = patientService.createPatient(patient);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Registration successful! Please login."
        ));
    }
    

}