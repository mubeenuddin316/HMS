package com.medorb.HMS.repository;

import com.medorb.HMS.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
    // You can add custom query methods here if needed in the future
    // For now, JpaRepository provides basic CRUD operations
	
	
}