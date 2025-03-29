package com.medorb.HMS.dto;

public class TimeSeriesDTO {
    private String periodLabel;       // e.g., "2025-03" or "2025-03-15"
    private long appointmentCount;
    private long opdQueueCount;

    public TimeSeriesDTO(String periodLabel, long appointmentCount, long opdQueueCount) {
        this.periodLabel = periodLabel;
        this.appointmentCount = appointmentCount;
        this.opdQueueCount = opdQueueCount;
    }

    public String getPeriodLabel() {
        return periodLabel;
    }

    public long getAppointmentCount() {
        return appointmentCount;
    }

    public long getOpdQueueCount() {
        return opdQueueCount;
    }
}
