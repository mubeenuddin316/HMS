package com.medorb.HMS.service;

import com.medorb.HMS.model.Doctor;
import java.util.List;
import java.util.Optional;

public interface DoctorService {
    List<Doctor> getAllDoctors();
    Optional<Doctor> getDoctorById(Integer doctorId);
    Doctor createDoctor(Doctor doctor);
    Doctor updateDoctor(Integer doctorId, Doctor doctor);
    void deleteDoctor(Integer doctorId);
    Optional<Doctor> getDoctorByEmail(String email); // Add find by email
    List<Doctor> getDoctorsByHospitalId(Integer hospitalId); // Get doctors for a hospital
	// long getDoctorCount();
}