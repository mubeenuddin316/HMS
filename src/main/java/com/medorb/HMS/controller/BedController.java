package com.medorb.HMS.controller;

import com.medorb.HMS.model.Bed;
import com.medorb.HMS.service.BedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/beds")
public class BedController {

    private final BedService bedService;

    @Autowired
    public BedController(BedService bedService) {
        this.bedService = bedService;
    }

    // 1. POST /api/beds - Create a new bed
    @PostMapping
    public ResponseEntity<Bed> createBed(@RequestBody Bed bed) {
        Bed createdBed = bedService.createBed(bed);
        return new ResponseEntity<>(createdBed, HttpStatus.CREATED);
    }

    // 2. GET /api/beds - Get all beds
    @GetMapping
    public ResponseEntity<List<Bed>> getAllBeds() {
        List<Bed> beds = bedService.getAllBeds();
        return new ResponseEntity<>(beds, HttpStatus.OK);
    }

    // 3. GET /api/beds/{bedId} - Get a bed by ID
    @GetMapping("/{bedId}")
    public ResponseEntity<Bed> getBedById(@PathVariable Integer bedId) {
        Optional<Bed> bed = bedService.getBedById(bedId);
        return bed.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 4. PUT /api/beds/{bedId} - Update an existing bed
    @PutMapping("/{bedId}")
    public ResponseEntity<Bed> updateBed(@PathVariable Integer bedId, @RequestBody Bed bed) {
        Bed updatedBed = bedService.updateBed(bedId, bed);
        if (updatedBed != null) {
            return new ResponseEntity<>(updatedBed, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 5. DELETE /api/beds/{bedId} - Delete a bed by ID
    @DeleteMapping("/{bedId}")
    public ResponseEntity<Void> deleteBed(@PathVariable Integer bedId) {
        bedService.deleteBed(bedId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Custom Endpoints (using custom service methods)

    // 6. GET /api/beds/hospital/{hospitalId} - Get beds by hospital ID
    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<Bed>> getBedsByHospitalId(@PathVariable Integer hospitalId) {
        List<Bed> beds = bedService.getBedsByHospitalId(hospitalId);
        return new ResponseEntity<>(beds, HttpStatus.OK);
    }

    // 7. GET /api/beds/ward/{ward} - Get beds by ward
    @GetMapping("/ward/{ward}")
    public ResponseEntity<List<Bed>> getBedsByWard(@PathVariable String ward) {
        List<Bed> beds = bedService.getBedsByWard(ward);
        return new ResponseEntity<>(beds, HttpStatus.OK);
    }

    // 8. GET /api/beds/status/{bedStatus} - Get beds by bed status (e.g., "Available")
    @GetMapping("/status/{bedStatus}")
    public ResponseEntity<List<Bed>> getBedsByBedStatus(@PathVariable String bedStatus) {
        List<Bed> beds = bedService.getBedsByBedStatus(bedStatus);
        return new ResponseEntity<>(beds, HttpStatus.OK);
    }

    // 9. GET /api/beds/hospital/{hospitalId}/available - Get available beds in a hospital
    @GetMapping("/hospital/{hospitalId}/available")
    public ResponseEntity<List<Bed>> getAvailableBedsByHospitalId(@PathVariable Integer hospitalId) {
        List<Bed> beds = bedService.getAvailableBedsByHospitalId(hospitalId);
        return new ResponseEntity<>(beds, HttpStatus.OK);
    }
}