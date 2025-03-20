package com.medorb.HMS.dto;

import java.time.LocalDateTime;

public class AppointmentForm {
    private Integer hospitalId;
    private Integer doctorId;
    private String status;
	private LocalDateTime appointmentDatetime;
    private String notes;
    
    
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getAppointmentDatetime() {
		return appointmentDatetime;
	}
	public void setAppointmentDatetime(LocalDateTime appointmentDatetime) {
		this.appointmentDatetime = appointmentDatetime;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

    
}
