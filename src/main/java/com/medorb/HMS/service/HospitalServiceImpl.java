package com.medorb.HMS.service;

import com.medorb.HMS.model.Hospital;
import com.medorb.HMS.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service // Mark as a service component
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;

    @Autowired // Inject HospitalRepository dependency
    public HospitalServiceImpl(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    @Override
    public Optional<Hospital> getHospitalById(Integer hospitalId) {
        return hospitalRepository.findById(hospitalId);
    }

    @Override
    public Hospital createHospital(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }
    
    @Override
    public Hospital updateHospital(Integer hospitalId, Hospital hospital) {
    	Optional<Hospital> existingHospitalOptional = hospitalRepository.findById(hospitalId);
    	if (existingHospitalOptional.isPresent()) {
    		Hospital existingHospital = existingHospitalOptional.get();
    		// Update fields that are allowed to be updated (e.g., name, address, etc.)
    		existingHospital.setName(hospital.getName());
            existingHospital.setAddress(hospital.getAddress());
            existingHospital.setCity(hospital.getCity());
            existingHospital.setPhoneNumber(hospital.getPhoneNumber());
            existingHospital.setEmail(hospital.getEmail());
            return hospitalRepository.save(existingHospital);
    	} else {
    		return null; // Or throw an exception indicating hospital not found
    	}
    }
    
    @Override
    public void deleteHospital(Integer hospitalId) {
        hospitalRepository.deleteById(hospitalId);
    }
}
