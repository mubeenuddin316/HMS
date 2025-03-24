package com.medorb.HMS.service;

import com.medorb.HMS.model.Doctor; // Import Doctor entity
import com.medorb.HMS.repository.DoctorRepository; // Import DoctorRepository
import org.springframework.beans.factory.annotation.Autowired; // Import Autowired
import org.springframework.stereotype.Service; // Import Service annotation

import java.util.List; // Import List
import java.util.Optional; // Import Optional

@Service // Marks this class as a Service component
public class DoctorServiceImpl implements DoctorService { // Implements DoctorService interface

    private final DoctorRepository doctorRepository; // Declare DoctorRepository

    @Autowired // Inject DoctorRepository using constructor injection
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<Doctor> getAllDoctors() { // Implement getAllDoctors method
        return doctorRepository.findAll(); // Use JpaRepository's findAll() method
    }

    @Override
    public Optional<Doctor> getDoctorById(Integer doctorId) { // Implement getDoctorById method
        return doctorRepository.findById(doctorId); // Use JpaRepository's findById()
    }

    @Override
    public Doctor createDoctor(Doctor doctor) { // Implement createDoctor method
        return doctorRepository.save(doctor); // Use JpaRepository's save() to create
    }

    @Override
    public Doctor updateDoctor(Integer doctorId, Doctor doctor) { // Implement updateDoctor method
        if (doctorRepository.existsById(doctorId)) { // Check if doctor exists
            doctor.setDoctorId(doctorId); // Ensure ID is set for update
            return doctorRepository.save(doctor); // Use JpaRepository's save() to update
        }
        return null; // Return null if doctor with given ID not found
    }

    @Override
    public void deleteDoctor(Integer doctorId) { // Implement deleteDoctor method
        doctorRepository.deleteById(doctorId); // Use JpaRepository's deleteById()
    }

    @Override
    public Optional<Doctor> getDoctorByEmail(String email) { //Implement getDoctorByEmail method
        return doctorRepository.findByEmail(email); // Use custom findByEmail method (will need to add to repository)
    }

    @Override
    public List<Doctor> getDoctorsByHospitalId(Integer hospitalId) { // Implement getDoctorsByHospitalId method
        return doctorRepository.findByHospital_HospitalId(hospitalId); // Use custom method (add to repo)
    }
    
    @Override
    public List<Doctor> findByHospitalId(Integer hospitalId) {
        return doctorRepository.findByHospital_HospitalId(hospitalId);
    }
    
    @Override
    public List<Doctor> findByName(String name) {
        return doctorRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Override
    public List<Doctor> findByHospitalIdAndName(Integer hospitalId, String name) {
        return doctorRepository.findByHospitalIdAndNameContainingIgnoreCase(hospitalId, name);
    }
    
    

	
}