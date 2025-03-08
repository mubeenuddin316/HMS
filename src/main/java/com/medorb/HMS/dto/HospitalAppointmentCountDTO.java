package com.medorb.HMS.dto;

public class HospitalAppointmentCountDTO {
    private String hospitalName;
    private long appointmentCount;

    public HospitalAppointmentCountDTO(String hospitalName, long appointmentCount) {
        this.hospitalName = hospitalName;
        this.appointmentCount = appointmentCount;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public long getAppointmentCount() {
        return appointmentCount;
    }
}
