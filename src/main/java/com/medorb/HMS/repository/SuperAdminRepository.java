package com.medorb.HMS.repository;

import com.medorb.HMS.model.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SuperAdminRepository extends JpaRepository<SuperAdmin, Integer> {

    // Custom query methods
    Optional<SuperAdmin> findByEmail(String email); // Find Super Admin by email - RETURNS Optional<SuperAdmin>

    // Optional: Method to find by email and password (for authentication - use with caution and hash passwords properly!)
    // Optional<SuperAdmin> findByEmailAndPassword(String email, String password);
}