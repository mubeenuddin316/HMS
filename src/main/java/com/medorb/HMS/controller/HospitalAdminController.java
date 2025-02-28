package com.medorb.HMS.controller;

import com.medorb.HMS.model.HospitalAdmin;
import com.medorb.HMS.service.HospitalAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hospitalAdmins") // You can adjust the API endpoint path
public class HospitalAdminController {

    private final HospitalAdminService hospitalAdminService;

    @Autowired
    public HospitalAdminController(HospitalAdminService hospitalAdminService) {
        this.hospitalAdminService = hospitalAdminService;
    }

    // 1. POST /api/hospitalAdmins - Create a new Hospital Admin
    @PostMapping
    public ResponseEntity<HospitalAdmin> createHospitalAdmin(@RequestBody HospitalAdmin hospitalAdmin) {
        // **SECURITY NOTE: In a real application, you should NOT accept plain text passwords in request bodies!**
        // Passwords should be handled securely on the client-side and hashed before being sent, or use a secure authentication mechanism.
        HospitalAdmin createdAdmin = hospitalAdminService.createHospitalAdmin(hospitalAdmin);
        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }

    // 2. GET /api/hospitalAdmins - Get all Hospital Admins
    @GetMapping
    public ResponseEntity<List<HospitalAdmin>> getAllHospitalAdmins() {
        List<HospitalAdmin> admins = hospitalAdminService.getAllHospitalAdmins();
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }

    // 3. GET /api/hospitalAdmins/{hospitalAdminId} - Get Hospital Admin by ID
    @GetMapping("/{hospitalAdminId}")
    public ResponseEntity<HospitalAdmin> getHospitalAdminById(@PathVariable Integer hospitalAdminId) {
        Optional<HospitalAdmin> admin = hospitalAdminService.getHospitalAdminById(hospitalAdminId);
        return admin.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 4. PUT /api/hospitalAdmins/{hospitalAdminId} - Update an existing Hospital Admin
    @PutMapping("/{hospitalAdminId}")
    public ResponseEntity<HospitalAdmin> updateHospitalAdmin(@PathVariable Integer hospitalAdminId, @RequestBody HospitalAdmin hospitalAdmin) {
        // **SECURITY NOTE: In a real application, handle password updates securely. Do not allow plain text password updates if possible. Consider password change flows.**
        HospitalAdmin updatedAdmin = hospitalAdminService.updateHospitalAdmin(hospitalAdminId, hospitalAdmin);
        if (updatedAdmin != null) {
            return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 5. DELETE /api/hospitalAdmins/{hospitalAdminId} - Delete a Hospital Admin
    @DeleteMapping("/{hospitalAdminId}")
    public ResponseEntity<Void> deleteHospitalAdmin(@PathVariable Integer hospitalAdminId) {
        hospitalAdminService.deleteHospitalAdmin(hospitalAdminId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Custom Endpoints

    // 6. GET /api/hospitalAdmins/hospital/{hospitalId} - Get Hospital Admins by Hospital ID
    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<HospitalAdmin>> getHospitalAdminsByHospitalId(@PathVariable Integer hospitalId) {
        List<HospitalAdmin> admins = hospitalAdminService.getHospitalAdminsByHospitalId(hospitalId);
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }

    // 7. GET /api/hospitalAdmins/email/{email} - Get Hospital Admin by Email (for login/auth scenarios later)
    @GetMapping("/email/{email}")
    public ResponseEntity<HospitalAdmin> getHospitalAdminByEmail(@PathVariable String email) {
        Optional<HospitalAdmin> adminOptional = hospitalAdminService.getHospitalAdminByEmail(email); // Get Optional<HospitalAdmin>

        return adminOptional.map(admin -> new ResponseEntity<>(admin, HttpStatus.OK)) // Use map to return ResponseEntity<HospitalAdmin> if present
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Use orElseGet to return 404 if Optional is empty
    }
    
    @GetMapping("/login")
    public ResponseEntity<HospitalAdmin> loginHospitalAdmin(
            @RequestParam("email") String email,
            @RequestParam("password") String password) {

        Optional<HospitalAdmin> hospitalAdminOptional = hospitalAdminService.getHospitalAdminByEmail(email); // Get Optional<HospitalAdmin>

        if (hospitalAdminOptional.isPresent()) { // Check if HospitalAdmin is present in the Optional
            HospitalAdmin hospitalAdmin = hospitalAdminOptional.get(); // Get the HospitalAdmin object from Optional
            if (hospitalAdmin.getPassword().equals(password)) {
                return new ResponseEntity<>(hospitalAdmin, HttpStatus.OK); // Login Success
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Incorrect password
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Email not found (Optional is empty)
        }
    }
}