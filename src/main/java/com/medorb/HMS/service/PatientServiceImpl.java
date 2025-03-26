package com.medorb.HMS.service;

import com.medorb.HMS.model.Patient;
import com.medorb.HMS.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> getPatientById(Integer patientId) {
        return patientRepository.findById(patientId);
    }

    @Override
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(Integer patientId, Patient patient) {
        if (patientRepository.existsById(patientId)) {
            patient.setPatientId(patientId); // Ensure ID is set for update
            return patientRepository.save(patient);
        }
        return null; // Patient not found
    }

    @Override
    public void deletePatient(Integer patientId) {
        patientRepository.deleteById(patientId);
    }

    @Override
    public Optional<Patient> getPatientByEmail(String email) {
        return patientRepository.findByEmail(email); // Use custom method from PatientRepository
    }
    
    @Override
    public List<Patient> findByName(String name) {
        return patientRepository.findByNameContainingIgnoreCase(name);
    }

	
}