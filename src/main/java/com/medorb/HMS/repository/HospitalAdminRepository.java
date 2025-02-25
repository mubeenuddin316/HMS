package com.medorb.HMS.repository;

import com.medorb.HMS.model.HospitalAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HospitalAdminRepository extends JpaRepository<HospitalAdmin, Integer> {

    // Custom query methods

    List<HospitalAdmin> findByHospital_HospitalId(Integer hospitalId); // Find admins for a specific hospital
    HospitalAdmin findByEmail(String email); // Find admin by email

    // Optional: Method to find by email and password (for authentication - use with caution and hash passwords properly!)
    // HospitalAdmin findByEmailAndPassword(String email, String password);

    // You can add more custom queries as needed, e.g., find by admin name, phone number, etc.
}