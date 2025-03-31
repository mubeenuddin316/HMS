package com.medorb.HMS.dto;

public class HospitalBedsDTO {
    private String hospitalName;
    private long totalBeds;
    private long occupiedBeds;
    private long availableBeds;

    public HospitalBedsDTO(String hospitalName, long totalBeds, long occupiedBeds) {
        this.hospitalName = hospitalName;
        this.totalBeds = totalBeds;
        this.occupiedBeds = occupiedBeds;
        this.availableBeds = totalBeds - occupiedBeds;
    }

    // Getters
    public String getHospitalName() {
        return hospitalName;
    }

    public long getTotalBeds() {
        return totalBeds;
    }

    public long getOccupiedBeds() {
        return occupiedBeds;
    }

    public long getAvailableBeds() {
        return availableBeds;
    }
}
