package com.medorb.HMS.service;

import com.medorb.HMS.model.Hospital;
import com.medorb.HMS.model.HospitalAdmin;
import com.medorb.HMS.model.SuperAdmin;
import java.util.List;
import java.util.Optional;

public interface SuperAdminService {
    List<SuperAdmin> getAllSuperAdmins();
    Optional<SuperAdmin> getSuperAdminById(Integer superAdminId);
    SuperAdmin createSuperAdmin(SuperAdmin superAdmin);
    SuperAdmin updateSuperAdmin(Integer superAdminId, SuperAdmin superAdmin);
    void deleteSuperAdmin(Integer superAdminId);
    Optional<SuperAdmin> getSuperAdminByEmail(String email); // Returns Optional<SuperAdmin>

    // --- Super Admin's Control over Hospitals ---
    List<Hospital> getAllHospitals(); // Super Admin can view all hospitals
    Optional<Hospital> getHospitalById(Integer hospitalId); // Super Admin can view any hospital by ID
    Hospital createHospitalBySuperAdmin(Hospital hospital); // Super Admin can create hospitals
    Hospital updateHospitalBySuperAdmin(Integer hospitalId, Hospital hospital); // Super Admin can update any hospital
    void deleteHospitalBySuperAdmin(Integer hospitalId); // Super Admin can delete any hospital

    // --- Super Admin's Control over Hospital Admins ---
    List<HospitalAdmin> getAllHospitalAdmins(); // Super Admin can view all hospital admins system-wide
    Optional<HospitalAdmin> getHospitalAdminById(Integer hospitalAdminId); // View any hospital admin by ID
    HospitalAdmin createHospitalAdminBySuperAdmin(HospitalAdmin hospitalAdmin); // Super Admin can create hospital admins
    HospitalAdmin updateHospitalAdminBySuperAdmin(Integer hospitalAdminId, HospitalAdmin hospitalAdmin); // Super Admin can update any hospital admin
    void deleteHospitalAdminBySuperAdmin(Integer hospitalAdminId); // Super Admin can delete any hospital admin
}