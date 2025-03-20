package com.medorb.HMS.dto;

import java.time.LocalDateTime;

public class PastVisitDTO {
    private LocalDateTime appointmentDatetime;
    private String doctorName;
    private String notes;     // or "treatment"
    private String status;
    private String visitType; // e.g. "APMT" or "OP"

    public PastVisitDTO(LocalDateTime appointmentDatetime,
                        String doctorName,
                        String notes,
                        String status,
                        String visitType) {
        this.appointmentDatetime = appointmentDatetime;
        this.doctorName = doctorName;
        this.notes = notes;
        this.status = status;
        this.visitType = visitType;
    }

    // Getters (and setters if needed)
    public LocalDateTime getAppointmentDatetime() {
        return appointmentDatetime;
    }
    public String getDoctorName() { return doctorName; }
    public String getNotes() { return notes; }
    public String getStatus() { return status; }
    public String getVisitType() { return visitType; }
}
