package com.medorb.HMS.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medorb.HMS.model.Hospital;
import com.medorb.HMS.model.HospitalAdmin;
import com.medorb.HMS.model.SuperAdmin;
import com.medorb.HMS.service.SuperAdminService;

@RestController
@RequestMapping("/api/superAdmin") // You can adjust the API endpoint path
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    @Autowired
    public SuperAdminController(SuperAdminService superAdminService) {
        this.superAdminService = superAdminService;
    }

    // --- Super Admin CRUD Operations ---

    // 1. POST /api/superAdmins - Create a new Super Admin
    @PostMapping
    public ResponseEntity<SuperAdmin> createSuperAdmin(@RequestBody SuperAdmin superAdmin) {
        // **SECURITY NOTE: Handle passwords securely! Hash before saving in service layer.**
        SuperAdmin createdAdmin = superAdminService.createSuperAdmin(superAdmin);
        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }

    // 2. GET /api/superAdmins - Get all Super Admins
    @GetMapping
    public ResponseEntity<List<SuperAdmin>> getAllSuperAdmins() {
        List<SuperAdmin> admins = superAdminService.getAllSuperAdmins();
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }

    // 3. GET /api/superAdmins/{superAdminId} - Get Super Admin by ID
    @GetMapping("/{superAdminId}")
    public ResponseEntity<SuperAdmin> getSuperAdminById(@PathVariable Integer superAdminId) {
        Optional<SuperAdmin> admin = superAdminService.getSuperAdminById(superAdminId);
        return admin.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 4. PUT /api/superAdmins/{superAdminId} - Update an existing Super Admin
    @PutMapping("/{superAdminId}")
    public ResponseEntity<SuperAdmin> updateSuperAdmin(@PathVariable Integer superAdminId, @RequestBody SuperAdmin superAdmin) {
        // **SECURITY NOTE: Handle password updates securely! Hash in service layer.**
        SuperAdmin updatedAdmin = superAdminService.updateSuperAdmin(superAdminId, superAdmin);
        if (updatedAdmin != null) {
            return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 5. DELETE /api/superAdmins/{superAdminId} - Delete a Super Admin
    @DeleteMapping("/{superAdminId}")
    public ResponseEntity<Void> deleteSuperAdmin(@PathVariable Integer superAdminId) {
        superAdminService.deleteSuperAdmin(superAdminId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 6. GET /api/superAdmins/email/{email} - Get Super Admin by Email
    @GetMapping("/email/{email}")
    public ResponseEntity<SuperAdmin> getSuperAdminByEmail(@PathVariable String email) {
        Optional<SuperAdmin> adminOptional = superAdminService.getSuperAdminByEmail(email); // Get Optional<SuperAdmin>

        return adminOptional.map(admin -> new ResponseEntity<>(admin, HttpStatus.OK)) // Use map for 200 OK if present
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Use orElseGet for 404 Not Found if empty
    }

    // --- Super Admin's API Endpoints for Managing Hospitals ---

    // 7. GET /api/superAdmins/hospitals - Get all Hospitals (Super Admin View)
    @GetMapping("/hospitals")
    public ResponseEntity<List<Hospital>> getAllHospitalsBySuperAdmin() {
        List<Hospital> hospitals = superAdminService.getAllHospitals();
        return new ResponseEntity<>(hospitals, HttpStatus.OK);
    }

    // 8. GET /api/superAdmins/hospitals/{hospitalId} - Get Hospital by ID (Super Admin View)
    @GetMapping("/hospitals/{hospitalId}")
    public ResponseEntity<Hospital> getHospitalBySuperAdminId(@PathVariable Integer hospitalId) {
        Optional<Hospital> hospital = superAdminService.getHospitalById(hospitalId);
        return hospital.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                       .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 9. POST /api/superAdmins/hospitals - Create a new Hospital (by Super Admin)
    @PostMapping("/hospitals")
    public ResponseEntity<Hospital> createHospitalBySuperAdmin(@RequestBody Hospital hospital) {
        Hospital createdHospital = superAdminService.createHospitalBySuperAdmin(hospital);
        return new ResponseEntity<>(createdHospital, HttpStatus.CREATED);
    }

    // 10. PUT /api/superAdmins/hospitals/{hospitalId} - Update Hospital (by Super Admin)
    @PutMapping("/hospitals/{hospitalId}")
    public ResponseEntity<Hospital> updateHospitalBySuperAdmin(@PathVariable Integer hospitalId, @RequestBody Hospital hospital) {
        Hospital updatedHospital = superAdminService.updateHospitalBySuperAdmin(hospitalId, hospital);
        if (updatedHospital != null) {
            return new ResponseEntity<>(updatedHospital, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 11. DELETE /api/superAdmins/hospitals/{hospitalId} - Delete Hospital (by Super Admin)
    @DeleteMapping("/hospitals/{hospitalId}")
    public ResponseEntity<Void> deleteHospitalBySuperAdmin(@PathVariable Integer hospitalId) {
        superAdminService.deleteHospitalBySuperAdmin(hospitalId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    // --- Super Admin's API Endpoints for Managing Hospital Admins ---

    // 12. GET /api/superAdmins/hospitalAdmins - Get all Hospital Admins (Super Admin View)
    @GetMapping("/hospitalAdmin")
    public ResponseEntity<List<HospitalAdmin>> getAllHospitalAdminsBySuperAdmin() {
        List<HospitalAdmin> hospitalAdmins = superAdminService.getAllHospitalAdmins();
        return new ResponseEntity<>(hospitalAdmins, HttpStatus.OK);
    }

    // 13. GET /api/superAdmins/hospitalAdmins/{hospitalAdminId} - Get Hospital Admin by ID (Super Admin View)
    @GetMapping("/hospitalAdmin/{hospitalAdminId}")
    public ResponseEntity<HospitalAdmin> getHospitalAdminBySuperAdminId(@PathVariable Integer hospitalAdminId) {
        Optional<HospitalAdmin> hospitalAdmin = superAdminService.getHospitalAdminById(hospitalAdminId);
        return hospitalAdmin.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                             .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 14. POST /api/superAdmins/hospitalAdmins - Create a new Hospital Admin (by Super Admin)
    @PostMapping("/hospitalAdmin")
    public ResponseEntity<HospitalAdmin> createHospitalAdminBySuperAdmin(@RequestBody HospitalAdmin hospitalAdmin) {
        // **SECURITY NOTE: Handle passwords securely! Hash before saving in service layer.**
        HospitalAdmin createdHospitalAdmin = superAdminService.createHospitalAdminBySuperAdmin(hospitalAdmin);
        return new ResponseEntity<>(createdHospitalAdmin, HttpStatus.CREATED);
    }

    // 15. PUT /api/superAdmins/hospitalAdmins/{hospitalAdminId} - Update Hospital Admin (by Super Admin)
    @PutMapping("/hospitalAdmin/{hospitalAdminId}")
    public ResponseEntity<HospitalAdmin> updateHospitalAdminBySuperAdmin(@PathVariable Integer hospitalAdminId, @RequestBody HospitalAdmin hospitalAdmin) {
        // **SECURITY NOTE: Handle password updates securely! Hash in service layer.**
        HospitalAdmin updatedHospitalAdmin = superAdminService.updateHospitalAdminBySuperAdmin(hospitalAdminId, hospitalAdmin);
        if (updatedHospitalAdmin != null) {
            return new ResponseEntity<>(updatedHospitalAdmin, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 16. DELETE /api/superAdmins/hospitalAdmins/{hospitalAdminId} - Delete Hospital Admin (by Super Admin)
    @DeleteMapping("/hospitalAdmin/{hospitalAdminId}")
    public ResponseEntity<Void> deleteHospitalAdminBySuperAdmin(@PathVariable Integer hospitalAdminId) {
        superAdminService.deleteHospitalAdminBySuperAdmin(hospitalAdminId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> loginSuperAdmin(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email and password are required"
            ));
        }

        Optional<SuperAdmin> superAdminOptional = superAdminService.getSuperAdminByEmail(email);

        if (superAdminOptional.isPresent() && superAdminOptional.get().getPassword().equals(password)) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Login successful",
                    "adminId", superAdminOptional.get().getSuperAdminId(),
                    "redirect", "/superAdmin/dashboard"
            ));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "Invalid email or password"
            ));
        }
    }

 // ✅ GET /api/superAdmin/dashboardData - Fetch Super Admin Dashboard Data
    @GetMapping("/dashboardData")
    public Map<String, Long> getDashboardData() {
        long totalHospitals = superAdminService.getTotalHospitals();
        long totalPatients = superAdminService.getTotalPatients();
        long totalDoctors = superAdminService.getTotalDoctors();
        long occupiedBeds = superAdminService.getOccupiedBeds();
        long totalBeds = superAdminService.getTotalBeds();
        long freeBeds = totalBeds - occupiedBeds;

        return Map.of(
            "totalHospitals", totalHospitals,
            "totalPatients", totalPatients,
            "totalDoctors", totalDoctors,
            "occupiedBeds", occupiedBeds,
            "freeBeds", freeBeds
        );

    }
    
}
