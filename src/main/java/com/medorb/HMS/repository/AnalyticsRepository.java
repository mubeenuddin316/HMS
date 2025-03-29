package com.medorb.HMS.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.medorb.HMS.model.Appointment;

@Repository
public interface AnalyticsRepository extends JpaRepository<Appointment, Integer> {

    @Query("""
        SELECT DATE_FORMAT(a.appointmentDatetime, '%Y-%m') AS periodLabel,
               COUNT(a) AS appointmentCount
        FROM Appointment a
        GROUP BY DATE_FORMAT(a.appointmentDatetime, '%Y-%m')
        ORDER BY periodLabel
    """)
    List<Object[]> getMonthlyAppointmentCounts();

    @Query("""
        SELECT DATE_FORMAT(q.registrationTime, '%Y-%m') AS periodLabel,
               COUNT(q) AS queueCount
        FROM OpdQueue q
        GROUP BY DATE_FORMAT(q.registrationTime, '%Y-%m')
        ORDER BY periodLabel
    """)
    List<Object[]> getMonthlyOpdQueueCounts();

}