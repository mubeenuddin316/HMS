package com.medorb.HMS.controller;

import com.medorb.HMS.model.Hospital;
import com.medorb.HMS.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    @Autowired
    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    // 1. Create a new Hospital (POST /api/hospitals)
    @PostMapping
    public ResponseEntity<Hospital> createHospital(@RequestBody Hospital hospital) {
        Hospital createdHospital = hospitalService.createHospital(hospital);
        return new ResponseEntity<>(createdHospital, HttpStatus.CREATED); // 201 Created
    }

    // 2. Get all Hospitals (GET /api/hospitals)
    @GetMapping
    public ResponseEntity<List<Hospital>> getAllHospitals() {
        List<Hospital> hospitals = hospitalService.getAllHospitals();
        return new ResponseEntity<>(hospitals, HttpStatus.OK); // 200 OK
    }

    // 3. Get Hospital by ID (GET /api/hospitals/{hospitalId})
    @GetMapping("/{hospitalId}")
    public ResponseEntity<Hospital> getHospitalById(@PathVariable Integer hospitalId) {
        Optional<Hospital> hospital = hospitalService.getHospitalById(hospitalId);
        return hospital.map(ResponseEntity::ok) // 200 OK if hospital found
                       .orElse(ResponseEntity.notFound().build()); // 404 Not Found if not found
    }

    // 4. Update an existing Hospital (PUT /api/hospitals/{hospitalId})
    @PutMapping("/{hospitalId}")
    public ResponseEntity<Hospital> updateHospital(@PathVariable Integer hospitalId, @RequestBody Hospital hospital) {
        Hospital updatedHospital = hospitalService.updateHospital(hospitalId, hospital);
        if (updatedHospital != null) {
            return new ResponseEntity<>(updatedHospital, HttpStatus.OK); // 200 OK if updated
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found if hospital with given ID not found
        }
    }

    // 5. Delete a Hospital (DELETE /api/hospitals/{hospitalId})
    @DeleteMapping("/{hospitalId}")
    public ResponseEntity<Void> deleteHospital(@PathVariable Integer hospitalId) {
        hospitalService.deleteHospital(hospitalId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content - successful deletion
    }
}