 package com.medorb.HMS.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Integer appointmentId;

    // Many appointments -> One patient
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // Many appointments -> One doctor
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // Many appointments -> One hospital
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @Column(name = "appointment_datetime", nullable = false)
    private LocalDateTime appointmentDatetime; // Use LocalDateTime for DATETIME type

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED; // Default

    @Lob
    private String notes;
    
    private BigDecimal charges; // or double, int, etc.

    public BigDecimal getCharges() {
        return charges;
    }

    public void setCharges(BigDecimal charges) {
        this.charges = charges;
    }
    
    public enum AppointmentStatus {
        SCHEDULED,
        RESCHEDULED,
        CANCELLED,
        COMPLETED,
        PENDING
    }

    
    public Appointment() {
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