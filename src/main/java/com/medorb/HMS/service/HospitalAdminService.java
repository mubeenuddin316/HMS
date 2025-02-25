package com.medorb.HMS.service;

import com.medorb.HMS.model.HospitalAdmin;
import java.util.List;
import java.util.Optional;

public interface HospitalAdminService {
    List<HospitalAdmin> getAllHospitalAdmins();
    Optional<HospitalAdmin> getHospitalAdminById(Integer hospitalAdminId);
    HospitalAdmin createHospitalAdmin(HospitalAdmin hospitalAdmin);
    HospitalAdmin updateHospitalAdmin(Integer hospitalAdminId, HospitalAdmin hospitalAdmin);
    void deleteHospitalAdmin(Integer hospitalAdminId);

    // Custom service methods
    List<HospitalAdmin> getHospitalAdminsByHospitalId(Integer hospitalId);
    HospitalAdmin getHospitalAdminByEmail(String email); // Find admin by email

    // Optional: Service method for authentication (if you uncommented in Repository)
    // HospitalAdmin authenticateHospitalAdmin(String email, String password);
}