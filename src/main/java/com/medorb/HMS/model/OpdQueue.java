package com.medorb.HMS.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "opd_queue")
public class OpdQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "queue_id")
    private Integer opdQueueId;

    // If you want to link to an appointment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = true)
    private Patient patient;
    
    
    @Column(name = "pname", nullable = true)
    private String patientName;       // Added New

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "queue_entry_time", nullable = false, updatable = false, 
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime registrationTime;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "queue_status", nullable = false)
    private QueueStatus queueStatus = QueueStatus.WAITING;;

    @Column(name = "queue_number")
    private Integer tokenNumber;
    
    public enum QueueStatus {
        BEINGSERVED,
        CANCELLED,
        WAITING,
        COMPLETED
    }

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

	public QueueStatus getQueueStatus() {
		return queueStatus;
	}

	public void setQueueStatus(QueueStatus queueStatus) {
		this.queueStatus = queueStatus;
	}

	public Integer getTokenNumber() {
		return tokenNumber;
	}

	public void setTokenNumber(Integer tokenNumber) {
		this.tokenNumber = tokenNumber;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	
	
}