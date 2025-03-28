package com.medorb.HMS.dto;

public class HospitalAppointmentCountDTO {
    private String hospitalName;
    private long appointmentCount;
    private long opdQueueCount;

    public HospitalAppointmentCountDTO(String hospitalName, long appointmentCount, long opdQueueCount) {
        this.hospitalName = hospitalName;
        this.appointmentCount = appointmentCount;
        this.opdQueueCount = opdQueueCount;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public long getAppointmentCount() {
        return appointmentCount;
    }

	public long getOpdQueueCount() {
		return opdQueueCount;
	}
    
    
}
