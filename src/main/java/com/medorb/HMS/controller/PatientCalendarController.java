package com.medorb.HMS.controller;

import com.medorb.HMS.model.Appointment;
import com.medorb.HMS.model.Patient;
import com.medorb.HMS.repository.AppointmentRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patient")
public class PatientCalendarController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    /**
     * GET /patient/upcomingDates?year=2025&month=3
     * Returns a JSON list of local dates (yyyy-MM-dd) where the patient has appointments.
     */
    @GetMapping("/upcomingDates")
    public List<String> getUpcomingDates(
            @RequestParam int year,
            @RequestParam int month,
            HttpServletRequest request
    ) {
        // 1) Check if patient is logged in
        Patient loggedInPatient = (Patient) request.getSession().getAttribute("loggedInPatient");
        if (loggedInPatient == null) {
            // Not logged in => Return empty list or throw an exception
            return Collections.emptyList();
        }

        // 2) Build date range for the given month/year
        //    For example, if year=2025, month=3 => from 2025-03-01 to 2025-03-31
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth   = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        LocalDateTime startDateTime = startOfMonth.atStartOfDay();
        LocalDateTime endDateTime   = endOfMonth.atTime(23, 59, 59);

        // 3) Fetch appointments in that date range for this patient
        List<Appointment> appts = appointmentRepository.findAppointmentsByPatientAndDateRange(
                loggedInPatient.getPatientId(),
                startDateTime,
                endDateTime
        );

        // 4) Convert each appointment's date to a string "yyyy-MM-dd"
        List<String> dateStrings = new ArrayList<>();
        for (Appointment a : appts) {
            LocalDate apptDate = a.getAppointmentDatetime().toLocalDate();
            dateStrings.add(apptDate.toString()); // e.g. "2025-03-12"
        }

        // 5) Return the date list
        return dateStrings;
    }
}
