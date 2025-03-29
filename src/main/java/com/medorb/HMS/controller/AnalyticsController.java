package com.medorb.HMS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medorb.HMS.dto.TimeSeriesDTO;
import com.medorb.HMS.service.AnalyticsService;

@RestController
@RequestMapping("/api/superAdmin")
public class AnalyticsController {
	
	private final AnalyticsService analyticsService;
	
	@Autowired
	public AnalyticsController(AnalyticsService analyticsService) {
		this.analyticsService = analyticsService;
	}

	@GetMapping("/timeSeries/appointmentsVsQueue")
    public List<TimeSeriesDTO> getAppointmentsVsQueueTimeSeries() {
        // Return monthly or daily time-series
        return analyticsService.getMonthlyAppointmentsVsQueue();
    }
    
    
    
    
    
}
