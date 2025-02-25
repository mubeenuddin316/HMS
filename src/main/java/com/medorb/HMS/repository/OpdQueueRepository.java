package com.medorb.HMS.repository;

import com.medorb.HMS.model.OpdQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OpdQueueRepository extends JpaRepository<OpdQueue, Integer> {

    // Custom query methods (modified to use DB column names if needed, but JPA usually infers from field names)

    List<OpdQueue> findByHospital_HospitalId(Integer hospitalId);
    List<OpdQueue> findByDoctor_DoctorId(Integer doctorId); // If you keep Doctor relationship
    List<OpdQueue> findByPatient_PatientId(Integer patientId);
    List<OpdQueue> findByQueueStatus(String queueStatus);

    // Example custom method: Find current queue for a doctor (Pending and In Progress statuses)
    List<OpdQueue> findByDoctor_DoctorIdAndQueueStatusIn(Integer doctorId, List<String> statuses); // If you keep Doctor relationship
}