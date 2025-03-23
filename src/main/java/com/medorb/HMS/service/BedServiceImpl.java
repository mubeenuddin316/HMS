package com.medorb.HMS.service;

import com.medorb.HMS.model.Bed;
import com.medorb.HMS.model.Hospital;
import com.medorb.HMS.repository.BedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BedServiceImpl implements BedService {

    private final BedRepository bedRepository;

    @Autowired
    public BedServiceImpl(BedRepository bedRepository) {
        this.bedRepository = bedRepository;
    }

    @Override
    public List<Bed> getAllBeds() {
        return bedRepository.findAll();
    }

    @Override
    public Optional<Bed> getBedById(Integer bedId) {
        return bedRepository.findById(bedId);
    }

    @Override
    public Bed createBed(Bed bed) {
        return bedRepository.save(bed);
    }

    @Override
    public Bed updateBed(Integer bedId, Bed bed) {
        if (bedRepository.existsById(bedId)) {
            bed.setBedId(bedId);
            return bedRepository.save(bed);
        }
        return null; // Bed not found
    }

    @Override
    public void deleteBed(Integer bedId) {
        bedRepository.deleteById(bedId);
    }

    @Override
    public List<Bed> getBedsByHospitalId(Integer hospitalId) {
        return bedRepository.findByHospital_HospitalId(hospitalId);
    }

    @Override
    public List<Bed> getBedsByWard(String ward) {
        return bedRepository.findByWard(ward);
    }

    @Override
    public List<Bed> getBedsByBedStatus(String bedStatus) {
        return bedRepository.findByBedStatus(bedStatus);
    }

    @Override
    public List<Bed> getAvailableBedsByHospitalId(Integer hospitalId) {
        return bedRepository.findByHospital_HospitalIdAndIsOccupiedFalse(hospitalId);
    }
    
    @Override
    public long countByHospital(Hospital hospital) {
        return bedRepository.countByHospital(hospital);
    }
    
    @Override
    public long countByHospitalAndIsOccupied(Hospital hospital, boolean isOccupied) {
        return bedRepository.countByHospitalAndIsOccupied(hospital, isOccupied);
    }
}