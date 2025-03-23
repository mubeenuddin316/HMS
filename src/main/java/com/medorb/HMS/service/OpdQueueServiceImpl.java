package com.medorb.HMS.service;

import com.medorb.HMS.model.OpdQueue;
import com.medorb.HMS.repository.OpdQueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class OpdQueueServiceImpl implements OpdQueueService {

    private final OpdQueueRepository opdQueueRepository;

    @Autowired
    public OpdQueueServiceImpl(OpdQueueRepository opdQueueRepository) {
        this.opdQueueRepository = opdQueueRepository;
    }

    @Override
    public List<OpdQueue> getAllOpdQueueEntries() {
        return opdQueueRepository.findAll();
    }

    @Override
    public Optional<OpdQueue> getOpdQueueEntryById(Integer opdQueueId) {
        return opdQueueRepository.findById(opdQueueId);
    }

    @Override
    public OpdQueue createOpdQueueEntry(OpdQueue opdQueue) {
        opdQueue.setRegistrationTime(LocalDateTime.now()); // Set registration time on creation
        return opdQueueRepository.save(opdQueue);
    }

    @Override
    public OpdQueue updateOpdQueueEntry(Integer opdQueueId, OpdQueue opdQueue) {
        if (opdQueueRepository.existsById(opdQueueId)) {
            opdQueue.setOpdQueueId(opdQueueId);
            return opdQueueRepository.save(opdQueue);
        }
        return null; // Entry not found
    }

    @Override
    public void deleteOpdQueueEntry(Integer opdQueueId) {
        opdQueueRepository.deleteById(opdQueueId);
    }

    @Override
    public List<OpdQueue> getOpdQueueEntriesByHospitalId(Integer hospitalId) {
        return opdQueueRepository.findByHospital_HospitalId(hospitalId);
    }

    @Override
    public List<OpdQueue> getOpdQueueEntriesByDoctorId(Integer doctorId) {
        return opdQueueRepository.findByDoctor_DoctorId(doctorId); // If you keep Doctor relationship
    }

    @Override
    public List<OpdQueue> getOpdQueueEntriesByPatientId(Integer patientId) {
        return opdQueueRepository.findByPatient_PatientId(patientId);
    }

    @Override
    public List<OpdQueue> getOpdQueueEntriesByStatus(String queueStatus) {
        return opdQueueRepository.findByQueueStatus(queueStatus);
    }

    @Override
    public List<OpdQueue> getCurrentOpdQueueForDoctor(Integer doctorId) {
        // Get entries that are either "Pending" or "In Progress"
        List<String> currentQueueStatuses = Arrays.asList("Waiting", "BeingServed"); // Use "Waiting" and "BeingServed" as per your ENUM
        return opdQueueRepository.findByDoctor_DoctorIdAndQueueStatusIn(doctorId, currentQueueStatuses); // If you keep Doctor relationship
    }
    
    @Override
    public List<OpdQueue> filterOpdQueues(String patientName, Integer doctorId, Integer hospitalId) {
        // If you want a custom JPA query:
        return opdQueueRepository.filterOpdQueues(
            (patientName == null || patientName.trim().isEmpty()) ? null : patientName.trim(),
            doctorId,
            hospitalId
        );
    }
}