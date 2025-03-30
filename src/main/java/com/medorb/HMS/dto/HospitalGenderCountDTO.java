package com.medorb.HMS.dto;

public class HospitalGenderCountDTO {
    private String hospitalName;
    private long count;

    public HospitalGenderCountDTO(String hospitalName, long count) {
        this.hospitalName = hospitalName;
        this.count = count;
    }

    public String getHospitalName() {
        return hospitalName;
    }
    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
    public long getCount() {
        return count;
    }
    public void setCount(long count) {
        this.count = count;
    }
}
