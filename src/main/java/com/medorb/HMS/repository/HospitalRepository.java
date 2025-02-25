package com.medorb.HMS.repository;

import com.medorb.HMS.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
    // You can add custom query methods here if needed in the future
    // For now, JpaRepository provides basic CRUD operations
}