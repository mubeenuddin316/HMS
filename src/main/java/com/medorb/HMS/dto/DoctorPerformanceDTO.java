package com.medorb.HMS.dto;

public class DoctorPerformanceDTO {
    private Integer doctorId;
    private String doctorName;
    private long performanceCount; // Sum of appointments and opdQueue entries

    public DoctorPerformanceDTO(Integer doctorId, String doctorName, long performanceCount) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.performanceCount = performanceCount;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public long getPerformanceCount() {
        return performanceCount;
    }
}
