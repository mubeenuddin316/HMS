package com.medorb.HMS.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "opd_queue")
public class OpdQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "queue_id") // **Match DB Column Name: queue_id**
    private Integer opdQueueId;
    
 // **If you DO want to include appointment_id, add this (if it's in your DB and you want to map it):**
    @ManyToOne // Or @OneToOne depending on relationship
    @JoinColumn(name = "appointment_id") // **If you want to map appointment_id**
    private Appointment appointment; // Add relationship to Appointment if needed
    
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false) // **Match DB Column Name: patient_id**
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false) // **Match DB Column Name: hospital_id**
    private Hospital hospital;

    // **If you DO want to include doctor_id, add this (if it's in your DB but not shown in image):**
    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false) // **If you add doctor_id to DB**
    private Doctor doctor;

    @Column(name = "queue_entry_time", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP") // **Match DB Column Name: queue_entry_time**
    private LocalDateTime registrationTime;

    @Column(name = "queue_status") // **Match DB Column Name: queue_status**
    private String queueStatus; // Keep as String in Java, we'll handle Enum values in code

    @Column(name = "queue_number") // **Match DB Column Name: queue_number**
    private Integer tokenNumber;


    public OpdQueue() {
        // Default constructor
    }

    // ... (Getters and Setters - keep them as they are, using Java-style field names like opdQueueId, registrationTime, tokenNumber) ...


	
	
    

    @Override
    public String toString() {
        return "OpdQueue{" +
               "opdQueueId=" + opdQueueId +
               ", hospital=" + (hospital != null ? hospital.getHospitalId() : null) +
               ", patient=" + (patient != null ? patient.getPatientId() : null) +
               ", doctor=" + (doctor != null ? doctor.getDoctorId() : null) + // If you include doctor
               ", registrationTime=" + registrationTime +
               ", queueStatus='" + queueStatus + '\'' +
               ", tokenNumber=" + tokenNumber +
               ", appointment=" + (appointment != null ? appointment.getAppointmentId() : null) + // If you include appointment
               '}';
    }

	public Integer getOpdQueueId() {
		return opdQueueId;
	}

	public void setOpdQueueId(Integer opdQueueId) {
		this.opdQueueId = opdQueueId;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public LocalDateTime getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(LocalDateTime registrationTime) {
		this.registrationTime = registrationTime;
	}

	public String getQueueStatus() {
		return queueStatus;
	}

	public void setQueueStatus(String queueStatus) {
		this.queueStatus = queueStatus;
	}

	public Integer getTokenNumber() {
		return tokenNumber;
	}

	public void setTokenNumber(Integer tokenNumber) {
		this.tokenNumber = tokenNumber;
	}
}