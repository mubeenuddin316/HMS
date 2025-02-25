package com.medorb.HMS.service;

import com.medorb.HMS.model.PatientReport;
import java.util.List;
import java.util.Optional;

public interface PatientReportService {
    List<PatientReport> getAllPatientReports();
    Optional<PatientReport> getPatientReportById(Integer reportId);
    PatientReport createPatientReport(PatientReport patientReport);
    PatientReport updatePatientReport(Integer reportId, PatientReport patientReport);
    void deletePatientReport(Integer reportId);
    List<PatientReport> getPatientReportsByPatientId(Integer patientId); // Get reports for a patient
    List<PatientReport> getPatientReportsByDoctorId(Integer doctorId);   // Get reports for a doctor
}