package com.medorb.HMS.repository;

import com.medorb.HMS.model.OpdQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OpdQueueRepository extends JpaRepository<OpdQueue, Integer> {

    // Custom query methods (modified to use DB column names if needed, but JPA usually infers from field names)

    List<OpdQueue> findByHospital_HospitalId(Integer hospitalId);
    List<OpdQueue> findByDoctor_DoctorId(Integer doctorId); // If you keep Doctor relationship
    List<OpdQueue> findByPatient_PatientId(Integer patientId);
    List<OpdQueue> findByQueueStatus(String queueStatus);

    // Example custom method: Find current queue for a doctor (Pending and In Progress statuses)
    List<OpdQueue> findByDoctor_DoctorIdAndQueueStatusIn(Integer doctorId, List<String> statuses);
       
    @Query("""
    	       SELECT q
    	         FROM OpdQueue q
    	        WHERE q.patient.patientId = :patientId
    	          AND q.queueStatus = 'COMPLETED'
    	       ORDER BY q.registrationTime DESC
    	       """)
    List<OpdQueue> findCompletedQueueByPatientId(@Param("patientId") Integer patientId);
    
    @Query("""
    	    SELECT q
    	      FROM OpdQueue q
    	      LEFT JOIN q.patient p
    	      JOIN q.doctor d
    	      JOIN q.hospital h
    	     WHERE (
    	           :patientName IS NULL
    	           OR (
    	                p IS NOT NULL
    	                AND LOWER(p.name) LIKE CONCAT('%', LOWER(:patientName), '%')
    	              )
    	           OR (
    	                p IS NULL
    	                AND LOWER(q.patientName) LIKE CONCAT('%', LOWER(:patientName), '%')
    	              )
    	         )
    	       AND (:doctorId IS NULL OR d.doctorId = :doctorId)
    	       AND (:hospitalId IS NULL OR h.hospitalId = :hospitalId)
    	    ORDER BY q.opdQueueId DESC
    	    """)
    	List<OpdQueue> filterOpdQueues(
    	   @Param("patientName") String patientName,
    	   @Param("doctorId") Integer doctorId,
    	   @Param("hospitalId") Integer hospitalId
    	);


}