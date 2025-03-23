package com.medorb.HMS.service;

import com.medorb.HMS.model.Bed;
import com.medorb.HMS.model.Hospital;

import java.util.List;
import java.util.Optional;

public interface BedService {
    List<Bed> getAllBeds();
    Optional<Bed> getBedById(Integer bedId);
    Bed createBed(Bed bed);
    Bed updateBed(Integer bedId, Bed bed);
    void deleteBed(Integer bedId);

    // Custom service methods using custom repository methods
    List<Bed> getBedsByHospitalId(Integer hospitalId);
    List<Bed> getBedsByWard(String ward);
    List<Bed> getBedsByBedStatus(String bedStatus);
    List<Bed> getAvailableBedsByHospitalId(Integer hospitalId); // Example: Get available beds in a hospital
    public long countByHospital(Hospital hospital);
    public long countByHospitalAndIsOccupied(Hospital hospital, boolean isOccupied);
    
    

}