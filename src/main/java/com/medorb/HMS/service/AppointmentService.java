package com.medorb.HMS.service;

import com.medorb.HMS.model.Appointment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    List<Appointment> getAllAppointments();
    Optional<Appointment> getAppointmentById(Integer appointmentId);
    Appointment createAppointment(Appointment appointment);
    Appointment updateAppointment(Integer appointmentId, Appointment appointment);
    void deleteAppointment(Integer appointmentId);
    List<Appointment> getAppointmentsByPatientId(Integer patientId); // Get appointments for a patient
    List<Appointment> getAppointmentsByDoctorId(Integer doctorId);   // Get appointments for a doctor
    List<Appointment> getAppointmentsByHospitalId(Integer hospitalId); // Get appointments for a hospital
    
    List<Appointment> getAppointmentsByDate(LocalDate date);
    
    long countTodaysAppointments();
	List<Appointment> filterAppointments(String patientName, Integer doctorId, Integer hospitalId, String status);


}