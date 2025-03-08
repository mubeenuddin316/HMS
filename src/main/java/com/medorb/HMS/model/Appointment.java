 package com.medorb.HMS.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Integer appointmentId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;
    
    @Column(name = "appointment_datetime", nullable = false)
    private LocalDateTime appointmentDatetime; // Use LocalDateTime for DATETIME type
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED; // Default value
    @Lob
    private String notes;

    public Appointment() {
    }
    
    public enum AppointmentStatus {
        SCHEDULED,
        RESCHEDULED,
        CANCELLED,
        COMPLETED
    }

    

    public Integer getAppointmentId() {
		return appointmentId;
	}



	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}



	public Patient getPatient() {
		return patient;
	}



	public void setPatient(Patient patient) {
		this.patient = patient;
	}



	public Doctor getDoctor() {
		return doctor;
	}



	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}



	public Hospital getHospital() {
		return hospital;
	}



	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}



	public LocalDateTime getAppointmentDatetime() {
		return appointmentDatetime;
	}



	public void setAppointmentDatetime(LocalDateTime appointmentDatetime) {
		this.appointmentDatetime = appointmentDatetime;
	}



	public AppointmentStatus getStatus() {
		return status;
	}



	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}



	public String getNotes() {
		return notes;
	}



	public void setNotes(String notes) {
		this.notes = notes;
	}



	@Override
    public String toString() {
        return "Appointment{" +
               "appointmentId=" + appointmentId +
               ", patient=" + (patient != null ? patient.getPatientId() : null) +
               ", doctor=" + (doctor != null ? doctor.getDoctorId() : null) +
               ", hospital=" + (hospital != null ? hospital.getHospitalId() : null) +
               ", appointmentDatetime=" + appointmentDatetime +
               ", status='" + status + '\'' +
               ", notes='" + notes + '\'' +
               '}';
    }
	
	
}