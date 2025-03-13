package com.medorb.HMS.model;


import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "bed_assignments")
public class BedAssignment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assignmentId;

    @ManyToOne
    @JoinColumn(name = "bed_id")
    private Bed bed;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String status; // e.g. "Active", "Discharged"
	
    public BedAssignment() {
        // Default constructor
    }

	public Integer getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Integer assignmentId) {
		this.assignmentId = assignmentId;
	}

	public Bed getBed() {
		return bed;
	}

	public void setBed(Bed bed) {
		this.bed = bed;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
    public String toString() {
        return "Bed{" +
               "assignmentId=" + assignmentId +
               ", bed='" + bed + '\'' +
               ", patient='" + patient + '\'' +
               ", startDateTime='" + startDateTime + '\'' +
               ", endDateTime='" + endDateTime + '\'' +
               ", status=" + status + '\'' +
               '}';
    }
}
