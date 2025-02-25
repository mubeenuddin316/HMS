package com.medorb.HMS.repository;

import com.medorb.HMS.model.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuperAdminRepository extends JpaRepository<SuperAdmin, Integer> {

    // Custom query methods
    SuperAdmin findByEmail(String email); // Find Super Admin by email
    // Optional: Method to find by email and password (for authentication - use with caution and hash passwords properly!)
    // SuperAdmin findByEmailAndPassword(String email, String password);
}