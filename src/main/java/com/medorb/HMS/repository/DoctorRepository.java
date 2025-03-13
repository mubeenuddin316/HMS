package com.medorb.HMS.repository;

import com.medorb.HMS.model.Doctor; // Import the Doctor entity

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository; // Import JpaRepository

public interface DoctorRepository extends JpaRepository<Doctor, Integer> { // Interface extends JpaRepository

	 // Custom query method to find a doctor by email address
    Optional<Doctor> findByEmail(String email);

    // Custom query method to find doctors by hospital's hospitalId
    List<Doctor> findByHospital_HospitalId(Integer hospitalId);

    // JpaRepository already provides basic CRUD operations like:
    // save(), findById(), findAll(), deleteById(), existsById(), etc.
    // We don't need to declare them again here.
    
    long countByHospital_HospitalId(Integer hospitalId);


}