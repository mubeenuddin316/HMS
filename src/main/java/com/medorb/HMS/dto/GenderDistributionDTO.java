package com.medorb.HMS.dto;

import java.util.List;

public class GenderDistributionDTO {
    private String gender;
    private long totalCount;
    private List<HospitalGenderCountDTO> hospitalCounts;

    public GenderDistributionDTO(String gender, long totalCount, List<HospitalGenderCountDTO> hospitalCounts) {
        this.gender = gender;
        this.totalCount = totalCount;
        this.hospitalCounts = hospitalCounts;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public long getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }
    public List<HospitalGenderCountDTO> getHospitalCounts() {
        return hospitalCounts;
    }
    public void setHospitalCounts(List<HospitalGenderCountDTO> hospitalCounts) {
        this.hospitalCounts = hospitalCounts;
    }
}
