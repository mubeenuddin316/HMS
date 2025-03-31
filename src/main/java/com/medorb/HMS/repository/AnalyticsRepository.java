package com.medorb.HMS.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.medorb.HMS.dto.DoctorPerformanceDTO;
import com.medorb.HMS.dto.HospitalBedsDTO;
import com.medorb.HMS.model.Appointment;


@Repository
public interface AnalyticsRepository extends JpaRepository<Appointment, Integer> {

    @Query("""
        SELECT DATE_FORMAT(a.appointmentDatetime, '%Y-%m') AS periodLabel,
               COUNT(a) AS appointmentCount
        FROM Appointment a
        GROUP BY DATE_FORMAT(a.appointmentDatetime, '%Y-%m')
        ORDER BY periodLabel
    """)
    List<Object[]> getMonthlyAppointmentCounts();

    @Query("""
        SELECT DATE_FORMAT(q.registrationTime, '%Y-%m') AS periodLabel,
               COUNT(q) AS queueCount
        FROM OpdQueue q
        GROUP BY DATE_FORMAT(q.registrationTime, '%Y-%m')
        ORDER BY periodLabel
    """)
    List<Object[]> getMonthlyOpdQueueCounts();
    
 // In AppointmentRepository.java
    @Query("SELECT p.gender, COUNT(a) FROM Appointment a JOIN a.patient p GROUP BY p.gender")
    List<Object[]> findTotalAppointmentCountByGender();

    @Query("SELECT h.name, COUNT(a) FROM Appointment a JOIN a.patient p JOIN a.hospital h WHERE p.gender = :gender GROUP BY h.name")
    List<Object[]> findHospitalCountsByGender(@Param("gender") String gender);
    
 // ... your existing methods ...

    @Query("SELECT new com.medorb.HMS.dto.HospitalBedsDTO(b.hospital.name, COUNT(b), " +
           "SUM(CASE WHEN b.isOccupied = true THEN 1 ELSE 0 END)) " +
           "FROM Bed b GROUP BY b.hospital.name")
    List<HospitalBedsDTO> findHospitalBedCounts();
    
//    @Query("SELECT new com.medorb.HMS.dto.DoctorPerformanceDTO(" +
//            "d.doctorId, d.name, " +
//            "((SELECT COUNT(a) FROM Appointment a WHERE a.doctor.doctorId = d.doctorId) + " +
//            "(SELECT COUNT(q) FROM OpdQueue q WHERE q.doctor.doctorId = d.doctorId))" +
//            ") " +
//            "FROM Doctor d " +
//            "WHERE (:hospitalId IS NULL OR d.hospital.hospitalId = :hospitalId)")
//     List<DoctorPerformanceDTO> findDoctorPerformance(@Param("hospitalId") Integer hospitalId);
    
    @Query("SELECT new com.medorb.HMS.dto.DoctorPerformanceDTO(" +
    	       "d.doctorId, d.name, " +
    	       "((SELECT COUNT(a) FROM Appointment a WHERE a.doctor.doctorId = d.doctorId) + " +
    	       "(SELECT COUNT(q) FROM OpdQueue q WHERE q.doctor.doctorId = d.doctorId))" +
    	       ") " +
    	       "FROM Doctor d " +
    	       "WHERE (:hospitalId IS NULL OR d.hospital.hospitalId = :hospitalId)")
    	List<DoctorPerformanceDTO> findDoctorPerformance(@Param("hospitalId") Integer hospitalId);

 }


