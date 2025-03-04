package com.medorb.HMS.service;

import com.medorb.HMS.model.Appointment;
import com.medorb.HMS.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Optional<Appointment> getAppointmentById(Integer appointmentId) {
        return appointmentRepository.findById(appointmentId);
    }

    @Override
    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment updateAppointment(Integer appointmentId, Appointment appointment) {
        if (appointmentRepository.existsById(appointmentId)) {
            appointment.setAppointmentId(appointmentId); // Ensure ID is set for update
            return appointmentRepository.save(appointment);
        }
        return null; // Appointment not found
    }

    @Override
    public void deleteAppointment(Integer appointmentId) {
        appointmentRepository.deleteById(appointmentId);
    }

    // Implementations for the new service methods:

    @Override
    public List<Appointment> getAppointmentsByPatientId(Integer patientId) {
        return appointmentRepository.findByPatient_PatientId(patientId); // Use custom repository method
    }

    @Override
    public List<Appointment> getAppointmentsByDoctorId(Integer doctorId) {
        return appointmentRepository.findByDoctor_DoctorId(doctorId); // Use custom repository method
    }

    @Override
    public List<Appointment> getAppointmentsByHospitalId(Integer hospitalId) {
        return appointmentRepository.findByHospital_HospitalId(hospitalId); // Use custom repository method
    }
    
    @Override
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return appointmentRepository.findByAppointmentDatetimeBetween(startOfDay, endOfDay);
    }
    
    @Override
    public long countTodaysAppointments() {
        // Get today's start and end
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        // Use your existing repository method:
        List<Appointment> todays = appointmentRepository.findByAppointmentDatetimeBetween(startOfDay, endOfDay);
        return todays.size();
    }

}