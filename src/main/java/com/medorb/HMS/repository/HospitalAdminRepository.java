package com.medorb.HMS.repository;

import com.medorb.HMS.model.HospitalAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface HospitalAdminRepository extends JpaRepository<HospitalAdmin, Integer> {

    // Custom query methods
    List<HospitalAdmin> findByHospital_HospitalId(Integer hospitalId);

    Optional<HospitalAdmin> findByEmail(String email); // Find admin by email - RETURNS Optional<HospitalAdmin>

    // Optional: Method to find by email and password (for authentication - use with caution and hash passwords properly!)
    // Optional<HospitalAdmin> findByEmailAndPassword(String email, String password); // Corrected to return Optional

    // You can add more custom queries as needed, e.g., find by admin name, phone number, etc.
}