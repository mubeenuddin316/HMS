package com.medorb.HMS.service;

import com.medorb.HMS.model.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientService {
    List<Patient> getAllPatients();
    Optional<Patient> getPatientById(Integer patientId);
    Patient createPatient(Patient patient);
    Patient updatePatient(Integer patientId, Patient patient);
    void deletePatient(Integer patientId);
    Optional<Patient> getPatientByEmail(String email); // Add find by email
	List<Patient> findByName(String name);
}