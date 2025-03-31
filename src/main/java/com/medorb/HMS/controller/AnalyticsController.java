package com.medorb.HMS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medorb.HMS.dto.DoctorPerformanceDTO;
import com.medorb.HMS.dto.GenderDistributionDTO;
import com.medorb.HMS.dto.HospitalBedsDTO;
import com.medorb.HMS.dto.TimeSeriesDTO;
import com.medorb.HMS.repository.AnalyticsRepository;
import com.medorb.HMS.repository.BedRepository;
import com.medorb.HMS.service.AnalyticsService;

@RestController
@RequestMapping("/api/superAdmin")
public class AnalyticsController {
	
	private final AnalyticsService analyticsService;
	private final BedRepository bedRepository;
	private final AnalyticsRepository analyticsRepository;
	
	@Autowired
	public AnalyticsController(AnalyticsService analyticsService, BedRepository bedRepository, AnalyticsRepository analyticsRepository) {
		this.analyticsService = analyticsService;
		this.bedRepository = bedRepository;
		this.analyticsRepository = analyticsRepository;
	}

	@GetMapping("/timeSeries/appointmentsVsQueue")
    public List<TimeSeriesDTO> getAppointmentsVsQueueTimeSeries() {
        // Return monthly or daily time-series
        return analyticsService.getMonthlyAppointmentsVsQueue();
    }
	
	@GetMapping("/genderDistribution")
    public List<GenderDistributionDTO> getGenderDistribution() {
        return analyticsService.getGenderDistributionData();
    }
	
	// New endpoint for hospital bed counts:
	@GetMapping("/hospitalBeds")
	public List<HospitalBedsDTO> getHospitalBeds() {
	    return analyticsRepository.findHospitalBedCounts();
	}
	
	// Endpoint for doctor performance data with optional hospital filter
    @GetMapping("/doctorPerformance")
    public List<DoctorPerformanceDTO> getDoctorPerformance(@RequestParam(required = false) Integer hospitalId) {
        return analyticsRepository.findDoctorPerformance(hospitalId);
    }
    
}
