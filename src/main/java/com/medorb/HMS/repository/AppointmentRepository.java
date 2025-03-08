package com.medorb.HMS.repository;

import com.medorb.HMS.dto.HospitalAppointmentCountDTO;
import com.medorb.HMS.model.Appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    // You can add custom query methods here if needed for Appointments in the future
    // For now, basic CRUD operations from JpaRepository are sufficient.
	List<Appointment> findByPatient_PatientId(Integer patientId);

    // Custom query method to find appointments by doctor's doctorId
    List<Appointment> findByDoctor_DoctorId(Integer doctorId);

    // Custom query method to find appointments by hospital's hospitalId
    List<Appointment> findByHospital_HospitalId(Integer hospitalId);
    
    List<Appointment> findByAppointmentDatetimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    @Query("SELECT new com.medorb.HMS.dto.HospitalAppointmentCountDTO(a.hospital.name, COUNT(a)) " +
            "FROM Appointment a " +
            "GROUP BY a.hospital.name")
     List<HospitalAppointmentCountDTO> findHospitalAppointmentCounts();

}