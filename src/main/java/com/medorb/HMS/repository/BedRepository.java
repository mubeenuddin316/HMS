package com.medorb.HMS.repository;

import com.medorb.HMS.dto.HospitalBedsDTO;
import com.medorb.HMS.model.Bed;
import com.medorb.HMS.model.Hospital;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BedRepository extends JpaRepository<Bed, Integer> {

    // Custom query methods (as requested)

    List<Bed> findByHospital_HospitalId(Integer hospitalId); // Find beds by Hospital ID
    List<Bed> findByWard(String ward); // Find beds by Ward
    List<Bed> findByBedStatus(String bedStatus); // Find beds by Bed Status

    // Example of a more complex custom query - find available beds in a hospital
    List<Bed> findByHospital_HospitalIdAndIsOccupiedFalse(Integer hospitalId);
    
 // ✅ Fetch Count of Occupied Beds
    @Query("SELECT COUNT(b) FROM Bed b WHERE b.isOccupied = true")
    long countByIsOccupiedTrue();

    // ✅ Add this method to count total beds
    long count();
    
    long countByHospital_HospitalId(Integer hospitalId);
    long countByHospital_HospitalIdAndIsOccupiedTrue(Integer hospitalId);
    
    long countByHospital(Hospital hospital);
    long countByHospitalAndIsOccupied(Hospital hospital, boolean isOccupied);
    
    // ... your existing methods ...

//    @Query("SELECT new com.medorb.HMS.dto.HospitalBedsDTO(b.hospital.name, COUNT(b), " +
//           "SUM(CASE WHEN b.isOccupied = true THEN 1 ELSE 0 END)) " +
//           "FROM Bed b GROUP BY b.hospital.name")
//    List<HospitalBedsDTO> findHospitalBedCounts();

}