package com.medorb.HMS.controller;

import com.medorb.HMS.model.OpdQueue;
import com.medorb.HMS.service.OpdQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/opdqueues")
public class OpdQueueController {

    private final OpdQueueService opdQueueService;

    @Autowired
    public OpdQueueController(OpdQueueService opdQueueService) {
        this.opdQueueService = opdQueueService;
    }

    // 1. POST /api/opdqueues - Add a new entry to OPD Queue
    @PostMapping
    public ResponseEntity<OpdQueue> createOpdQueueEntry(@RequestBody OpdQueue opdQueue) {
        // **TODO: Validate queueStatus to be one of your ENUM values ("Waiting", "BeingServed", "Completed", "Cancelled") here or in Service layer
        OpdQueue createdEntry = opdQueueService.createOpdQueueEntry(opdQueue);
        return new ResponseEntity<>(createdEntry, HttpStatus.CREATED);
    }

    // 2. GET /api/opdqueues - Get all OPD Queue entries
    @GetMapping
    public ResponseEntity<List<OpdQueue>> getAllOpdQueueEntries() {
        List<OpdQueue> queueEntries = opdQueueService.getAllOpdQueueEntries();
        return new ResponseEntity<>(queueEntries, HttpStatus.OK);
    }

    // 3. GET /api/opdqueues/{opdQueueId} - Get an OPD Queue entry by ID
    @GetMapping("/{opdQueueId}")
    public ResponseEntity<OpdQueue> getOpdQueueEntryById(@PathVariable Integer opdQueueId) {
        Optional<OpdQueue> queueEntry = opdQueueService.getOpdQueueEntryById(opdQueueId);
        return queueEntry.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 4. PUT /api/opdqueues/{opdQueueId} - Update an existing OPD Queue entry
    @PutMapping("/{opdQueueId}")
    public ResponseEntity<OpdQueue> updateOpdQueueEntry(@PathVariable Integer opdQueueId, @RequestBody OpdQueue opdQueue) {
        // **TODO: Validate queueStatus to be one of your ENUM values ("Waiting", "BeingServed", "Completed", "Cancelled") here or in Service layer
        OpdQueue updatedEntry = opdQueueService.updateOpdQueueEntry(opdQueueId, opdQueue);
        if (updatedEntry != null) {
            return new ResponseEntity<>(updatedEntry, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 5. DELETE /api/opdqueues/{opdQueueId} - Delete an OPD Queue entry by ID
    @DeleteMapping("/{opdQueueId}")
    public ResponseEntity<Void> deleteOpdQueueEntry(@PathVariable Integer opdQueueId) {
        opdQueueService.deleteOpdQueueEntry(opdQueueId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Custom Endpoints

    // 6. GET /api/opdqueues/hospital/{hospitalId} - Get OPD Queue entries by Hospital ID
    @GetMapping("/hospital/{hospitalId}")
    public ResponseEntity<List<OpdQueue>> getOpdQueueEntriesByHospitalId(@PathVariable Integer hospitalId) {
        List<OpdQueue> queueEntries = opdQueueService.getOpdQueueEntriesByHospitalId(hospitalId);
        return new ResponseEntity<>(queueEntries, HttpStatus.OK);
    }

    // 7. GET /api/opdqueues/doctor/{doctorId} - Get OPD Queue entries by Doctor ID (if you kept Doctor relationship)
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<OpdQueue>> getOpdQueueEntriesByDoctorId(@PathVariable Integer doctorId) {
        List<OpdQueue> queueEntries = opdQueueService.getOpdQueueEntriesByDoctorId(doctorId);
        return new ResponseEntity<>(queueEntries, HttpStatus.OK);
    }

    // 8. GET /api/opdqueues/patient/{patientId} - Get OPD Queue entries by Patient ID
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<OpdQueue>> getOpdQueueEntriesByPatientId(@PathVariable Integer patientId) {
        List<OpdQueue> queueEntries = opdQueueService.getOpdQueueEntriesByPatientId(patientId);
        return new ResponseEntity<>(queueEntries, HttpStatus.OK);
    }

    // 9. GET /api/opdqueues/status/{queueStatus} - Get OPD Queue entries by Queue Status
    @GetMapping("/status/{queueStatus}")
    public ResponseEntity<List<OpdQueue>> getOpdQueueEntriesByStatus(@PathVariable String queueStatus) {
        List<OpdQueue> queueEntries = opdQueueService.getOpdQueueEntriesByStatus(queueStatus);
        return new ResponseEntity<>(queueEntries, HttpStatus.OK);
    }

     // 10. GET /api/opdqueues/doctor/{doctorId}/current - Get current OPD Queue for a Doctor (if you kept Doctor relationship)
    @GetMapping("/doctor/{doctorId}/current")
    public ResponseEntity<List<OpdQueue>> getCurrentOpdQueueForDoctor(@PathVariable Integer doctorId) {
        List<OpdQueue> currentQueue = opdQueueService.getCurrentOpdQueueForDoctor(doctorId);
        return new ResponseEntity<>(currentQueue, HttpStatus.OK);
    }
}