package com.medorb.HMS.repository;

import com.medorb.HMS.model.Bed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BedRepository extends JpaRepository<Bed, Integer> {

    // Custom query methods (as requested)

    List<Bed> findByHospital_HospitalId(Integer hospitalId); // Find beds by Hospital ID
    List<Bed> findByWard(String ward); // Find beds by Ward
    List<Bed> findByBedStatus(String bedStatus); // Find beds by Bed Status

    // Example of a more complex custom query - find available beds in a hospital
    List<Bed> findByHospital_HospitalIdAndIsOccupiedFalse(Integer hospitalId);
}