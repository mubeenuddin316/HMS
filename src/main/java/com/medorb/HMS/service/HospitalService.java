package com.medorb.HMS.service;

import com.medorb.HMS.model.Hospital;
import java.util.List;
import java.util.Optional;

public interface HospitalService {
    List<Hospital> getAllHospitals();
    Optional<Hospital> getHospitalById(Integer hospitalId);
    Hospital createHospital(Hospital hospital);
    Hospital updateHospital(Integer hospitalId, Hospital hospital);
    void deleteHospital(Integer hospitalId);
    
    
	//long getHospitalCount();
 
}