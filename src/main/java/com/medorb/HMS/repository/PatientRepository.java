package com.medorb.HMS.repository;

import com.medorb.HMS.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

    // Custom query method to find a patient by email
    Optional<Patient> findByEmail(String email);

    // You can add more custom query methods here if needed for Patients
    // For example, findByFirstNameAndLastName, findByContactNumber, etc.
    // For now, basic CRUD and findByEmail are sufficient.
}