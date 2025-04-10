package com.medorb.HMS.controller;

import java.util.List; // Import List
import java.util.Map;
import java.util.Optional; // Import Optional

import org.springframework.beans.factory.annotation.Autowired; // Import Autowired
import org.springframework.http.HttpStatus; // Import HttpStatus
import org.springframework.http.ResponseEntity; // Import ResponseEntity
// Import Controller annotations
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medorb.HMS.model.Doctor; // Import Doctor entity
import com.medorb.HMS.service.DoctorService; // Import DoctorService

@RestController // Marks this class as a REST Controller
@RequestMapping("/api/doctors") // Base URL path for all endpoints in this controller
public class DoctorController {

    private final DoctorService doctorService; // Inject DoctorService

    @Autowired // Constructor injection of DoctorService
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // 1. POST /api/doctors - Create a new doctor
    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor createdDoctor = doctorService.createDoctor(doctor);
        return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED); // 201 Created status
    }

    // 2. GET /api/doctors - Get all doctors
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK); // 200 OK status
    }

    // 3. GET /api/doctors/{doctorId} - Get a doctor by ID
    @GetMapping("/{doctorId}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Integer doctorId) {
        Optional<Doctor> doctor = doctorService.getDoctorById(doctorId);
        return doctor.map(value -> new ResponseEntity<>(value, HttpStatus.OK)) // 200 OK if found
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // 404 Not Found if not found
    }

    // 4. PUT /api/doctors/{doctorId} - Update an existing doctor
    @PutMapping("/{doctorId}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Integer doctorId, @RequestBody Doctor doctor) {
        Doctor updatedDoctor = doctorService.updateDoctor(doctorId, doctor);
        if (updatedDoctor != null) {
            return new ResponseEntity<>(updatedDoctor, HttpStatus.OK); // 200 OK if updated
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found if doctor ID doesn't exist
        }
    }

    // 5. DELETE /api/doctors/{doctorId} - Delete a doctor by ID
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Integer doctorId) {
        doctorService.deleteDoctor(doctorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content after successful delete
    }

    // Custom Endpoints (Optional for now, but good to have based on DoctorService)

    // 6. GET /api/doctors/email/{email} - Get doctor by email
    @GetMapping("/email/{email}")
    public ResponseEntity<Doctor> getDoctorByEmail(@PathVariable String email) {
        Optional<Doctor> doctor = doctorService.getDoctorByEmail(email);
        return doctor.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 7. GET /api/doctors/hospital/{hospitalId} - Get doctors by hospital ID
    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<Doctor>> getDoctorsByHospitalId(@PathVariable Integer hospitalId) {
        List<Doctor> doctors = doctorService.getDoctorsByHospitalId(hospitalId);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<?> loginDoctor(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email and password are required"
            ));
        }

        Optional<Doctor> doctorOptional = doctorService.getDoctorByEmail(email);

        if (doctorOptional.isPresent() && doctorOptional.get().getPassword().equals(password)) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Login successful",
                    "doctorId", doctorOptional.get().getDoctorId(),
                    "redirect", "/doctor/dashboard"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "Invalid email or password"
            ));
        }
    }
}