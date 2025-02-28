package com.medorb.HMS.service;

import com.medorb.HMS.model.HospitalAdmin;
import com.medorb.HMS.repository.HospitalAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class HospitalAdminServiceImpl implements HospitalAdminService {

    private final HospitalAdminRepository hospitalAdminRepository;

    @Autowired
    public HospitalAdminServiceImpl(HospitalAdminRepository hospitalAdminRepository) {
        this.hospitalAdminRepository = hospitalAdminRepository;
    }

    @Override
    public List<HospitalAdmin> getAllHospitalAdmins() {
        return hospitalAdminRepository.findAll();
    }

    @Override
    public Optional<HospitalAdmin> getHospitalAdminById(Integer hospitalAdminId) {
        return hospitalAdminRepository.findById(hospitalAdminId);
    }

    @Override
    public HospitalAdmin createHospitalAdmin(HospitalAdmin hospitalAdmin) {
        // **IMPORTANT: In a real application, you MUST hash the password before saving!**
        // For simplicity in this example, we are NOT hashing passwords here.
        // **DO NOT STORE PLAIN TEXT PASSWORDS IN PRODUCTION!**
        return hospitalAdminRepository.save(hospitalAdmin);
    }

    @Override
    public HospitalAdmin updateHospitalAdmin(Integer hospitalAdminId, HospitalAdmin hospitalAdmin) {
        if (hospitalAdminRepository.existsById(hospitalAdminId)) {
            hospitalAdmin.setHospitalAdminId(hospitalAdminId);
            // **IMPORTANT: When updating passwords in a real application, you should hash the new password if it's being updated!**
            return hospitalAdminRepository.save(hospitalAdmin);
        }
        return null; // Admin not found
    }

    @Override
    public void deleteHospitalAdmin(Integer hospitalAdminId) {
        hospitalAdminRepository.deleteById(hospitalAdminId);
    }

    @Override
    public List<HospitalAdmin> getHospitalAdminsByHospitalId(Integer hospitalId) {
        return hospitalAdminRepository.findByHospital_HospitalId(hospitalId);
    }

    @Override
    public Optional<HospitalAdmin> getHospitalAdminByEmail(String email) {
        return hospitalAdminRepository.findByEmail(email);
    }

    // Optional: Service method implementation for authentication (if you uncommented in Repository and Service interface)
    // @Override
    // public HospitalAdmin authenticateHospitalAdmin(String email, String password) {
    //     // **IMPORTANT: In a real application, you MUST compare hashed passwords!**
    //     // This is a VERY INSECURE example - DO NOT USE IN PRODUCTION!
    //     HospitalAdmin admin = hospitalAdminRepository.findByEmailAndPassword(email, password);
    //     return admin;
    // }
}