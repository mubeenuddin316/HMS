package com.medorb.HMS.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "patient_reports")
public class PatientReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId;

    // Many reports -> One patient
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // Many reports -> One doctor
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    private LocalDate reportDate;
    private String reportType;
    private String reportFilePath;

    public PatientReport() {
    }

    

    public Integer getReportId() {
		return reportId;
	}



	public void setReportId(Integer reportId) {
		this.reportId = reportId;
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



	public LocalDate getReportDate() {
		return reportDate;
	}



	public void setReportDate(LocalDate reportDate) {
		this.reportDate = reportDate;
	}



	public String getReportType() {
		return reportType;
	}



	public void setReportType(String reportType) {
		this.reportType = reportType;
	}



	public String getReportFilePath() {
		return reportFilePath;
	}



	public void setReportFilePath(String reportFilePath) {
		this.reportFilePath = reportFilePath;
	}



	@Override
    public String toString() {
        return "PatientReport{" +
               "reportId=" + reportId +
               ", patient=" + (patient != null ? patient.getPatientId() : null) +
               ", doctor=" + (doctor != null ? doctor.getDoctorId() : null) +
               ", reportDate=" + reportDate +
               ", reportType='" + reportType + '\'' +
               ", reportFilePath='" + reportFilePath + '\'' +
               '}';
    }
}